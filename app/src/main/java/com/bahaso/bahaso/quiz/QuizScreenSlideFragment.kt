package com.bahaso.bahaso.quiz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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
                answerCorrection(v.findViewById(R.id.option_a),
                    v.findViewById(R.id.tv_option_a),
                    binding?.tvOptionA?.text)
            }
            R.id.option_b -> {
                answerCorrection(v.findViewById(R.id.option_b),
                    v.findViewById(R.id.tv_option_b),
                    binding?.tvOptionB?.text)
            }
            R.id.option_c -> {
                answerCorrection(v.findViewById(R.id.option_c),
                    v.findViewById(R.id.tv_option_c),
                    binding?.tvOptionC?.text)
            }
            R.id.option_d -> {
                answerCorrection(v.findViewById(R.id.option_d),
                    v.findViewById(R.id.tv_option_d),
                    binding?.tvOptionD?.text)
            }
        }
    }

    private fun answerCorrection(
        cardView: CardView,
        textView: TextView,
        userChoice: CharSequence?,
    ) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)

        val textViewAnimator =
            ObjectAnimator.ofPropertyValuesHolder(textView, scaleX, scaleY, alpha)
                .apply {
                    interpolator = OvershootInterpolator()
                }

        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        if (userChoice == quiz?.answer) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(),
                R.color.light_green))

            textViewAnimator.apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        setFragmentResult(QuizFragment.QUIZ_PAGE_INTERACTION_KEY,
                            bundleOf(QuizFragment.EXTRA_ANSWER_CHECK to true))
                        super.onAnimationEnd(animation)
                    }
                })
            }.start()
        } else {
            cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(),
                R.color.red))

            textViewAnimator.apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        setFragmentResult(QuizFragment.QUIZ_PAGE_INTERACTION_KEY,
                            bundleOf(QuizFragment.EXTRA_ANSWER_CHECK to false))
                        super.onAnimationEnd(animation)
                    }
                })
            }.start()
        }
    }
}