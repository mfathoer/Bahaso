package com.bahaso.bahaso.theory

import androidx.lifecycle.ViewModel
import com.bahaso.bahaso.core.data.local.DataTheory
import com.bahaso.bahaso.core.domain.Theory
import javax.inject.Inject

class TheoryViewModel @Inject constructor() : ViewModel() {

    private var _viewPagerCurrentPage: Int = 0
    private var _listDataTheory = ArrayList<Theory>()

    val viewPagerCurrentPage: Int
        get() = _viewPagerCurrentPage

    val listDataTheory: List<Theory>
        get() = _listDataTheory

    fun setViewPagerCurrentPage(newValue: Int) {
        _viewPagerCurrentPage = newValue
    }

    fun getDataTheory() {
        _listDataTheory.addAll(DataTheory.dailyWordTheory())
    }
}