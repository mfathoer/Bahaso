package com.bahaso.bahaso.core.session

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    companion object {
        private const val PREF_NAME = "mypref"
        private const val KEY_NAME = "nama"
        private const val KEY_EMAIL = "email"
        private const val KEY_GENDER = "gender"
        private const val KEY_BIRTH = "birth"
        private const val KEY_REMINDER_ID = "key_reminder_id"
        private const val KEY_SHOULD_FETCH_SAVED_KAJIAN_FROM_REMOTE = "key_should_fetch"
    }

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    val name = preferences.getString(KEY_NAME, "")
    val email = preferences.getString(KEY_EMAIL, "")
    val gender = preferences.getString(KEY_GENDER, "")
    val birth = preferences.getString(KEY_BIRTH, "")
    val reminderId = preferences.getLong(KEY_REMINDER_ID, 1)
    val shouldFetchSavedKajianFromRemote = preferences.getBoolean(
        KEY_SHOULD_FETCH_SAVED_KAJIAN_FROM_REMOTE, true
    )

    fun setShouldFetch(newState: Boolean) {
        editor.putBoolean(KEY_SHOULD_FETCH_SAVED_KAJIAN_FROM_REMOTE, newState).apply()
    }

    fun setName(name: String) {
        editor.putString(KEY_NAME, name).apply()
    }

    fun setEmail(email: String) {
        editor.putString(KEY_EMAIL, email).apply()
    }

    fun setGender(gender: String) {
        editor.putString(KEY_GENDER, gender).apply()
    }

    fun setBirth(birth: String) {
        editor.putString(KEY_BIRTH, birth).apply()
    }

    fun incrementReminderId() {
        var id = reminderId
        editor.putLong(KEY_REMINDER_ID, ++id).apply()
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}