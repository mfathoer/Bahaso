package com.bahaso.bahaso.core.di

import com.bahaso.bahaso.core.data.QuizRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class]
)
interface CoreComponent {

    @Component.Factory
    interface Factory {
        fun create(): CoreComponent
    }

    fun provideQuizRepository(): QuizRepository
}