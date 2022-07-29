package com.elian.taskproject.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elian.taskproject.data.database.dao.TaskDAO
import com.elian.taskproject.data.database.dao.UserDAO
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.model.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(version = 8, entities = [Task::class, User::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract val userDAO: UserDAO
    abstract val taskDAO: TaskDAO

    companion object
    {
        const val name = "app_database"
    }
}
