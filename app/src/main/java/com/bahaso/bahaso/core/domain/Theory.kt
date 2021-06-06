package com.bahaso.bahaso.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Theory(
    val id: Int,
    val imageDrawable: Int,
    val text: String,
    val translation: String,
) : Parcelable
