package com.bahaso.bahaso.core.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore

}