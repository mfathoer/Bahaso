package com.bahaso.bahaso.theory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bahaso.bahaso.core.domain.Theory

class TheoryPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val listTheory: List<Theory>,
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return listTheory.size
    }

    override fun createFragment(position: Int): Fragment {
        return ScreenSlideTheoryFragment.newInstance(listTheory[position])
    }
}