package com.elian.taskproject.data.database.dao

import androidx.room.*
import com.elian.taskproject.data.model.User

@Dao
interface UserDAO
{
    @Query("SELECT * FROM user_table WHERE id = 0")
    fun getUser(): User

    @Query("SELECT * FROM user_table")
    fun selectAll(): List<User>

    @Insert
    fun insert(user: User): Long
}
