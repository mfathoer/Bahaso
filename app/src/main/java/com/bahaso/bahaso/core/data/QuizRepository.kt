package com.bahaso.bahaso.core.data

import com.bahaso.bahaso.core.data.remote.RemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

}