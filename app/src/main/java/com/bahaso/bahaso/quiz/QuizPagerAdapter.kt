package com.bahaso.bahaso.quiz

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bahaso.bahaso.core.domain.Quiz

class QuizPagerAdapter(
    fragment: FragmentManager,
    lifeCycle: Lifecycle,
    private val listQuiz: List<Quiz>,
) : FragmentStateAdapter(fragment, lifeCycle) {

    override fun getItemCount(): Int {
        return listQuiz.size
    }

    override fun createFragment(position: Int): Fragment {
        return QuizScreenSlideFragment.newInstance(listQuiz[position])
    }
}