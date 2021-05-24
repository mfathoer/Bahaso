package com.bahaso.bahaso.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.MyApplication
import com.bahaso.bahaso.R
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.util.showLongToastMessage
import com.bahaso.bahaso.databinding.FragmentQuizBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class QuizFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentQuizBinding? = null

    private val binding
        get() = _binding

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: QuizViewModel by viewModels {
        factory
    }

    private val arguments: QuizFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.answerA?.setOnClickListener(this)
        binding?.answerB?.setOnClickListener(this)
        binding?.answerC?.setOnClickListener(this)
        binding?.answerD?.setOnClickListener(this)
        observeQuizData()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.answer_a -> {}
            R.id.answer_b -> {}
            R.id.answer_c -> {}
            R.id.answer_d -> {}
        }
    }

    private fun observeQuizData(){
        arguments.topic?.let { viewModel.getAllQuizByTopic(it.id) }
        launch {
            viewModel.quiz.collect { loadResult ->
                when (loadResult) {
                    is LoadResult.Loading -> {
                        showLoadingState(true)
                    }
                    is LoadResult.Success -> {
                        showLoadingState(false)
                    }
                    is LoadResult.Error -> {
                        showLoadingState(false)
                    }
                }
            }
        }
    }

    private fun showLoadingState(state: Boolean) {
        if (state) {
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.cardQuestion?.visibility = View.GONE
            binding?.answerA?.visibility = View.GONE
            binding?.answerB?.visibility = View.GONE
            binding?.answerC?.visibility = View.GONE
            binding?.answerD?.visibility = View.GONE
        } else {
            binding?.progressBar?.visibility = View.GONE
            binding?.cardQuestion?.visibility = View.VISIBLE
            binding?.answerA?.visibility = View.VISIBLE
            binding?.answerB?.visibility = View.VISIBLE
            binding?.answerC?.visibility = View.VISIBLE
            binding?.answerD?.visibility = View.VISIBLE
        }
    }
}