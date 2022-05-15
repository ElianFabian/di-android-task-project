package com.elian.taskproject.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elian.taskproject.data.database.dao.TaskDAO
import com.elian.taskproject.data.database.dao.UserDAO
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.model.User

@Database(version = 6, entities = [Task::class, User::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract val userDAO: UserDAO
    abstract val taskDAO: TaskDAO

    companion object
    {
        val instance get() = INSTANCE as AppDatabase

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase
        {
            synchronized(this)
            {
                return INSTANCE
                    ?: Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                        .also { INSTANCE = it }
            }
        }

        fun create(context: Context)
        {
            INSTANCE = getDatabase(context)
        }
    }
}