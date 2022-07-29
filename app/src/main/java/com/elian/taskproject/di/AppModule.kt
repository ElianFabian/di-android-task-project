package com.elian.taskproject.di

import android.content.Context
import androidx.room.Room
import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.database.dao.TaskDAO
import com.elian.taskproject.data.repository.TaskRoomRepository
import com.elian.taskproject.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, AppDatabase.name)
        .build()

    @Singleton
    @Provides
    fun provideTaskDAO(database: AppDatabase) = database.taskDAO

    @Singleton
    @Provides
    fun provideTaskRepository(taskDAO: TaskDAO): TaskRepository = TaskRoomRepository(taskDAO)
}
