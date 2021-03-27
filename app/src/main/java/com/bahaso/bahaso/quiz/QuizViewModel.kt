package com.bahaso.bahaso.quiz

import androidx.lifecycle.ViewModel
import com.bahaso.bahaso.core.data.QuizRepository
import javax.inject.Inject

class QuizViewModel @Inject constructor(private val quizRepository: QuizRepository) : ViewModel() {

}