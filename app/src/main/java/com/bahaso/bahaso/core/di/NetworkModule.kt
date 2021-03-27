package com.bahaso.bahaso.core.di

import com.bahaso.bahaso.core.data.remote.FireStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideFireStore(): FireStore = FireStore()

}