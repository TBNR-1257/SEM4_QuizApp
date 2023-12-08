package com.bryan.personalproject.core.di

import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.data.repo.QuestionRepo
import com.bryan.personalproject.data.repo.QuestionRepoImpl
import com.bryan.personalproject.data.repo.QuizRepo
import com.bryan.personalproject.data.repo.QuizRepoImpl
import com.bryan.personalproject.data.repo.ScoreRepo
import com.bryan.personalproject.data.repo.ScoreRepoImpl
import com.bryan.personalproject.data.repo.UserRepo
import com.bryan.personalproject.data.repo.UserRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Provides
    @Singleton
    fun providesUserRepo(authService: AuthService): UserRepo {
        return UserRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun providesQuizRepo(authService: AuthService): QuizRepo {
        return QuizRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun providesQuestionRepo(authService: AuthService): QuestionRepo {
        return QuestionRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun providesScoreRepo(authService: AuthService): ScoreRepo {
        return ScoreRepoImpl(authService = authService)
    }

}