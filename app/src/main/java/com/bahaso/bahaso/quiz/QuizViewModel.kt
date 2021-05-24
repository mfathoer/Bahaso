package com.bahaso.bahaso.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.data.QuizRepository
import com.bahaso.bahaso.core.domain.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizViewModel @Inject constructor(private val quizRepository: QuizRepository) : ViewModel() {

    private val _quiz = MutableStateFlow<LoadResult<List<Quiz>>>(LoadResult.Loading())

    val quiz: StateFlow<LoadResult<List<Quiz>>>
        get() = _quiz

    fun getAllQuizByTopic(topicId: String) = viewModelScope.launch {
        try {
            quizRepository.getAllQuizByTopic(topicId).collect {
                _quiz.value = it
            }
        } catch (e: Exception) {
            _quiz.value = LoadResult.Error(e.message.toString())
        }
    }
}