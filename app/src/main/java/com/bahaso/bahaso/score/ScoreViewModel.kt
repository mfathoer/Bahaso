package com.bahaso.bahaso.home

import androidx.lifecycle.ViewModel
import com.bahaso.bahaso.core.data.QuizRepository
import javax.inject.Inject


class ScoreViewModel @Inject constructor(private val quizRepository: QuizRepository) : ViewModel() {

    private val grade : Float = 0F
}