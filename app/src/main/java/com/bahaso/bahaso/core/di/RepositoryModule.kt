package com.bahaso.bahaso.core.di

import com.bahaso.bahaso.core.data.QuizRepository
import com.bahaso.bahaso.core.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class RepositoryModule {

    @Provides
    fun provideQuizRepository(remoteDataSource: RemoteDataSource): QuizRepository =
        QuizRepository(remoteDataSource)
}