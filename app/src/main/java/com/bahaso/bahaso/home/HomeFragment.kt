package com.bahaso.bahaso.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.MyApplication
import com.bahaso.bahaso.R
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.session.Preferences
import com.bahaso.bahaso.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding
        get() = _binding

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: HomeViewModel by viewModels {
        factory
    }

    private lateinit var adapter: LearningTopicListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LearningTopicListAdapter()
        observeQuizTopic()

        // Menampilkan nama user
        val pref = Preferences(requireContext())
        binding?.username?.text = "Halo, ${pref.name}"

        val requestOptions = RequestOptions().placeholder(R.drawable.ic_user_profile_image)
            .error(R.drawable.ic_user_profile_image)

        binding?.imgAvatar?.let {
            Glide.with(this).setDefaultRequestOptions(requestOptions).load("").into(it)
        }

        binding?.rvLearningTopic?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }

        adapter.onItemClicked = {
            val action = HomeFragmentDirections.actionHomeFragmentToTheoryFragment()
            action.topic = it

            findNavController().navigate(action)
        }
    }

    private fun observeQuizTopic() = launch {
        viewModel.getAllLearningTopic()
        viewModel.topic.collect { loadResult ->
            when (loadResult) {
                is LoadResult.Loading -> {
                    showLoadingState(true)
                }
                is LoadResult.Success -> {
                    showLoadingState(false)
                    adapter.submitList(loadResult.data)
                }
                is LoadResult.Error -> {
                    showLoadingState(false)
                }
            }
        }
    }

    private fun showLoadingState(state: Boolean) {
        if (state) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

}