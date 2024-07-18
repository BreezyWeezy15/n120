package com.nbc.nbcreplicate.di

import android.app.Application
import android.content.Context
import com.nbc.nbcreplicate.repository.AppRepository
import com.nbc.nbcreplicate.repository.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppRepository(@ApplicationContext context: Context): AppRepositoryImpl {
        return AppRepository(context)
    }



}