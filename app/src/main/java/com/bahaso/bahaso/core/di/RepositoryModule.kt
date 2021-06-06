package com.bahaso.bahaso.core.di

import com.bahaso.bahaso.core.data.QuizRepository
import com.bahaso.bahaso.core.data.remote.FireStoreTopicAndQuizDataSource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module(includes = [NetworkModule::class])
class RepositoryModule {

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideCoroutineScope(coroutineDispatcher: CoroutineDispatcher) =
        CoroutineScope(coroutineDispatcher)

    @Provides
    fun provideQuizRepository(
        fireStoreTopicAndQuizDataSource: FireStoreTopicAndQuizDataSource,
        ioDispatcher: CoroutineDispatcher,
        coroutineScope: CoroutineScope,
    ): QuizRepository =
        QuizRepository(fireStoreTopicAndQuizDataSource, ioDispatcher, coroutineScope)

}