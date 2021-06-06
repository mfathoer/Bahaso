package com.bahaso.bahaso.di

import com.bahaso.bahaso.core.di.CoreComponent
import com.bahaso.bahaso.home.HomeFragment
import com.bahaso.bahaso.quiz.QuizFragment
import com.bahaso.bahaso.signup.SignUpFragment
import com.bahaso.bahaso.theory.TheoryFragment
import dagger.Component

@AppScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [ViewModelModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(fragment: HomeFragment)
    fun inject(fragment: QuizFragment)
    fun inject(fragment: SignUpFragment)
    fun inject(fragment: TheoryFragment)
}