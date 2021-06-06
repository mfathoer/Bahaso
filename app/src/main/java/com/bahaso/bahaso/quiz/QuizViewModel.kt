package com.bahaso.bahaso.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.data.QuizRepository
import com.bahaso.bahaso.core.domain.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizViewModel @Inject constructor(private val quizRepository: QuizRepository) : ViewModel() {

    private val _quiz = MutableStateFlow<LoadResult<List<Quiz>>>(LoadResult.Loading())
    private var _listQuizSize = 0
    private var _viewPagerCurrentPage = 0
    private var _userCorrectAnswers = 0

    val quiz: StateFlow<LoadResult<List<Quiz>>>
        get() = _quiz

    val listQuizSize: Int
        get() = _listQuizSize

    val viewPagerCurrentPage: Int
        get() = _viewPagerCurrentPage

    val userScore: Float
        get() = _userCorrectAnswers * 100f / listQuizSize

    fun setListQuizSize(size: Int) {
        _listQuizSize = size
    }

    fun incrementViewPagerCurrentPage() {
        ++_viewPagerCurrentPage
    }

    fun incrementUserCorrectAnswer() {
        ++_userCorrectAnswers
    }

    fun getAllQuizByTopic(topicId: String) = viewModelScope.launch {
        try {
            quizRepository.getAllQuizByTopic(topicId).collectLatest {
                _quiz.value = it
            }
        } catch (e: Exception) {
            _quiz.value = LoadResult.Error(e.message.toString())
        }
    }

    fun saveUserScore(topicId: String) = viewModelScope.launch {
        quizRepository.saveUserScore(topicId, userScore)
    }
}