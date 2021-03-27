package com.bahaso.bahaso.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bahaso.bahaso.di.AppScope
import javax.inject.Inject
import javax.inject.Provider

@AppScope
class ViewModelFactory @Inject constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value
        ?: throw IllegalArgumentException("Unknown view model class: ${modelClass.simpleName}")

        return creator.get() as T
    }
}