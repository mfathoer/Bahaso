package com.bahaso.bahaso.core.data.remote

import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.domain.Quiz
import com.bahaso.bahaso.core.domain.Topic
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireStoreTopicAndQuizDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher,
    private val externalScope: CoroutineScope = GlobalScope,
) {

    companion object {

        private const val TOPICS_COLLECTION = "topics"
        private const val TOPIC_FIELD_TOPIC = "topic"
        private const val TOPIC_FIELD_IMAGE_URL = "image_url"
        private const val TOPIC_FIELD_DESCRIPTION = "description"

        private const val QUESTIONS_COLLECTION = "questions"
        private const val QUESTION_FIELD_QUESTION = "question"
        private const val QUESTION_FIELD_TRANSLATION = "translation"
        private const val QUESTION_FIELD_ANSWER = "answer"
        private const val QUESTION_FIELD_OPTION_1 = "option1"
        private const val QUESTION_FIELD_OPTION_2 = "option2"
        private const val QUESTION_FIELD_OPTION_3 = "option3"
        private const val QUESTION_USER_SCORE = "users_score"
        private const val SCORE = "score"
    }

    @ExperimentalCoroutinesApi
    val allLearningTopic = callbackFlow<LoadResult<List<Topic>>> {
        val dataRef = fireStore.collection(TOPICS_COLLECTION)

        val listTopics = ArrayList<Topic>()
        val subscription = dataRef.addSnapshotListener { querySnapshot, error ->

            if (error == null) {
                querySnapshot?.forEach { queryDocumentSnapshot ->
                    listTopics.add(Topic(
                        id = queryDocumentSnapshot.id,
                        topic = queryDocumentSnapshot.getString(TOPIC_FIELD_TOPIC) ?: "",
                        description = queryDocumentSnapshot.getString(TOPIC_FIELD_DESCRIPTION)
                            ?: "",
                        imageUrl = queryDocumentSnapshot.getString(TOPIC_FIELD_IMAGE_URL) ?: "",
                    ))
                }
                offer(LoadResult.Success(listTopics))
            } else {
                error.message?.let { offer(LoadResult.Error(it)) }
            }

        }

        awaitClose { subscription.remove() }
    }.flowOn(ioDispatcher)

    @ExperimentalCoroutinesApi
    fun getAllQuizByTopic(topicId: String) = callbackFlow<LoadResult<List<Quiz>>> {

        val dataRef = fireStore.collection(TOPICS_COLLECTION).document(topicId).collection(
            QUESTIONS_COLLECTION)
        val listQuiz = ArrayList<Quiz>()
        val subscription = dataRef.addSnapshotListener { querySnapshot, error ->
            if (error == null) {
                querySnapshot?.forEach { queryDocumentSnapshot ->
                    listQuiz.add(Quiz(
                        id = queryDocumentSnapshot.id,
                        question = queryDocumentSnapshot.getString(QUESTION_FIELD_QUESTION) ?: "",
                        translation = queryDocumentSnapshot.getString(QUESTION_FIELD_TRANSLATION)
                            ?: "",
                        answer = queryDocumentSnapshot.getString(QUESTION_FIELD_ANSWER) ?: "",
                        option1 = queryDocumentSnapshot.getString(QUESTION_FIELD_OPTION_1) ?: "",
                        option2 = queryDocumentSnapshot.getString(QUESTION_FIELD_OPTION_2) ?: "",
                        option3 = queryDocumentSnapshot.getString(QUESTION_FIELD_OPTION_3) ?: "",
                    )
                    )
                }
                offer(LoadResult.Success(listQuiz))
            } else {
                error.message?.let { offer(LoadResult.Error(it)) }
            }
        }
        awaitClose { subscription.remove() }
    }.flowOn(ioDispatcher)

    fun saveUserScore(topicId: String, score: Float) =
        externalScope.launch {
            auth.currentUser?.let {
                val dataRef = fireStore.collection(TOPICS_COLLECTION).document(topicId).collection(
                    QUESTION_USER_SCORE).document(it.uid)

                val data = hashMapOf(
                    SCORE to score
                )

                dataRef.set(data)
            }
        }

}