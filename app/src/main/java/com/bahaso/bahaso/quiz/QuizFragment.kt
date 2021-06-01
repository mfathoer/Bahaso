package com.bahaso.bahaso.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.MyApplication
import com.bahaso.bahaso.R
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.databinding.FragmentQuizBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizFragment : BaseFragment() {

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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding?.viewPager?.currentItem = viewModel.viewPagerCurrentPage
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.tvTopic?.text = arguments.topic?.topic
        observeQuizData()
        binding?.viewPager?.isUserInputEnabled = false

        binding?.viewPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding?.tvCountQuestion?.text =
                    getString(R.string.count_question, position + 1, viewModel.listQuizSize)
                binding?.questionProgressBar?.progress = position + 1
            }
        })

        childFragmentManager.setFragmentResultListener(QUIZ_PAGE_INTERACTION_KEY,
            viewLifecycleOwner) { _, bundle ->
            val isUserCorrect = bundle.getBoolean(EXTRA_ANSWER_CHECK)
            if (isUserCorrect) {
                viewModel.incrementUserCorrectAnswer()
            }
            val currentPagePosition = viewModel.viewPagerCurrentPage

            if (currentPagePosition < viewModel.listQuizSize - 1) {
                binding?.viewPager?.currentItem = currentPagePosition + 1
                viewModel.incrementViewPagerCurrentPage()
            } else {
                arguments.topic?.id?.let { viewModel.saveUserScore(it) }
                // show score
            }
        }
    }

    private fun observeQuizData() {
        arguments.topic?.let { viewModel.getAllQuizByTopic(it.id) }
        launch {
            viewModel.quiz.collect { loadResult ->
                when (loadResult) {
                    is LoadResult.Loading -> {
                        showLoadingState(true)
                    }
                    is LoadResult.Success -> {
                        showLoadingState(false)
                        loadResult.data?.let {
                            binding?.viewPager?.adapter =
                                QuizPagerAdapter(childFragmentManager, lifecycle, it.shuffled())
                            viewModel.setListQuizSize(it.size)
                            binding?.questionProgressBar?.max = viewModel.listQuizSize

                            binding?.questionProgressBar?.progress = 1
                            binding?.tvCountQuestion?.text =
                                getString(R.string.count_question, 1, viewModel.listQuizSize)
                        }
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
            binding?.viewPager?.visibility = View.GONE
        } else {
            binding?.progressBar?.visibility = View.GONE
            binding?.viewPager?.visibility = View.VISIBLE
        }
    }

    companion object {
        const val QUIZ_PAGE_INTERACTION_KEY = "quiz_page_interaction_key"
        const val EXTRA_ANSWER_CHECK = "extra_answer_check"
    }
}