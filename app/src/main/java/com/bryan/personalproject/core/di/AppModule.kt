package com.bryan.personalproject.core.di

import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.core.service.AuthServiceImpl
import com.bryan.personalproject.core.service.StorageService
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthServiceImpl()
    }

    @Provides
    @Singleton
    fun provideStorageService(): StorageService {
        return StorageService()
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


}