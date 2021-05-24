package com.bahaso.bahaso.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    val id: String,
    val topic: String,
    val description: String,
    val imageUrl: String,
) : Parcelable