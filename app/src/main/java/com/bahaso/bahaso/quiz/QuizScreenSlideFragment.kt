package com.bahaso.bahaso.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.bahaso.bahaso.R
import com.bahaso.bahaso.core.domain.Quiz
import com.bahaso.bahaso.databinding.FragmentQuizScreenSlideBinding


class QuizScreenSlideFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentQuizScreenSlideBinding? = null

    private val binding
        get() = _binding

    private var quiz: Quiz? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuizScreenSlideBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val EXTRA_QUIZ = "extra_quiz"

        @JvmStatic
        fun newInstance(quiz: Quiz): Fragment {
            return QuizScreenSlideFragment().apply {
                arguments = bundleOf(
                    EXTRA_QUIZ to quiz
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.optionA?.setOnClickListener(this)
        binding?.optionB?.setOnClickListener(this)
        binding?.optionC?.setOnClickListener(this)
        binding?.optionD?.setOnClickListener(this)

        quiz = arguments?.getParcelable(EXTRA_QUIZ)

        quiz?.let {
            val options = mutableListOf(it.option1, it.option2, it.option3, it.answer)
            options.shuffle()
            binding?.tvQuestion?.text = it.question
            binding?.tvOptionA?.text = options[0]
            binding?.tvOptionB?.text = options[1]
            binding?.tvOptionC?.text = options[2]
            binding?.tvOptionD?.text = options[3]
            binding?.tvTranslation?.text = it.translation
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.option_a -> {
                answerCorrection(binding?.tvOptionA?.text)
            }
            R.id.option_b -> {
                answerCorrection(binding?.tvOptionB?.text)
            }
            R.id.option_c -> {
                answerCorrection(binding?.tvOptionC?.text)
            }
            R.id.option_d -> {
                answerCorrection(binding?.tvOptionD?.text)
            }
        }
    }

    private fun answerCorrection(userChoice: CharSequence?) {
        if (userChoice == quiz?.answer) {
            setFragmentResult(QuizFragment.QUIZ_PAGE_INTERACTION_KEY,
                bundleOf(QuizFragment.EXTRA_ANSWER_CHECK to true))
        } else {
            setFragmentResult(QuizFragment.QUIZ_PAGE_INTERACTION_KEY,
                bundleOf(QuizFragment.EXTRA_ANSWER_CHECK to false))
        }
    }
}