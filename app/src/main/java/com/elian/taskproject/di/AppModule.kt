package com.elian.taskproject.di

import android.content.Context
import androidx.room.Room
import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.database.dao.TaskDAO
import com.elian.taskproject.data.database.dao.UserDAO
import com.elian.taskproject.data.model.User
import com.elian.taskproject.data.repository.TaskRoomRepository
import com.elian.taskproject.domain.repository.TaskRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
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
    fun provideUserDAO(database: AppDatabase) = database.userDAO

    @Singleton
    @Provides
    fun provideTaskRepository(taskDAO: TaskDAO): TaskRepository = TaskRoomRepository(taskDAO)

    @Singleton
    @Provides
    fun provideCurrentUser(userDAO: UserDAO) = getCurrentUser(userDAO)
}

private fun createUser(): User
{
    val randomLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".random()
    val uuid = UUID.randomUUID().toString().replace("-", "")

    return User(email = "$randomLetter$uuid@gmail.com")
}

private fun getCurrentUser(userDAO: UserDAO): User
{
    val userExists = AppDatabase.executorService.submit(userDAO::userExists).get()

    if (userExists)
    {
        return AppDatabase.executorService.submit(userDAO::getUser).get()
    }
    else
    {
        val newUser = createUser()

        val documentPath = "users/${newUser.email}"

        Firebase.firestore.document(documentPath).set(newUser).addOnCompleteListener()
        {
            AppDatabase.executorService.execute { userDAO.insert(newUser) }
        }

        return newUser
    }
}