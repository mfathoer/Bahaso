package com.bahaso.bahaso.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quiz(
    val id: String,
    val question: String,
    val answer: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val translation: String,
    var userChoice: String = ""
): Parcelable
