package com.bahaso.bahaso.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.home.HomeViewModel
import com.bahaso.bahaso.quiz.QuizViewModel
import com.bahaso.bahaso.theory.TheoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuizViewModel::class)
    abstract fun bindQuizViewModel(viewModel: QuizViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TheoryViewModel::class)
    abstract fun bindTheoryViewModel(viewModel: TheoryViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}