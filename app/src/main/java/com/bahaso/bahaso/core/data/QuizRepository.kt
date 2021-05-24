package com.bahaso.bahaso.core.data

import com.bahaso.bahaso.core.data.remote.FireStoreTopicAndQuizDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val fireStoreTopicAndQuizDataSource: FireStoreTopicAndQuizDataSource,
    private val ioDispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope,
) {

    suspend fun getAllLearningTopic() = withContext(ioDispatcher) {
        return@withContext fireStoreTopicAndQuizDataSource.allLearningTopic.shareIn(coroutineScope,
            SharingStarted.WhileSubscribed(1000))
    }

    suspend fun getAllQuizByTopic(topicId: String) = withContext(ioDispatcher) {
        return@withContext fireStoreTopicAndQuizDataSource.getAllQuizByTopic(topicId)
    }
}