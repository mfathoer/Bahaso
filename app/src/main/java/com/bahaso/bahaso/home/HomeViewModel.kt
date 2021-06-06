package com.bahaso.bahaso.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.data.QuizRepository
import com.bahaso.bahaso.core.domain.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val quizRepository: QuizRepository) : ViewModel() {

    private val _topics = MutableStateFlow<LoadResult<List<Topic>>>(LoadResult.Loading())

    val topic: StateFlow<LoadResult<List<Topic>>>
        get() = _topics

    fun getAllLearningTopic() = viewModelScope.launch {
        try {
            quizRepository.getAllLearningTopic().collect {
                _topics.value = it
            }
        } catch (e: Exception) {
            _topics.value = LoadResult.Error(e.message.toString())
        }
    }
}