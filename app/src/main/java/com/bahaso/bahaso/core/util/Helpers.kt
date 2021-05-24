package com.bahaso.bahaso.core.util

import android.content.Context
import android.widget.Toast

fun Context.showLongToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showShortToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}