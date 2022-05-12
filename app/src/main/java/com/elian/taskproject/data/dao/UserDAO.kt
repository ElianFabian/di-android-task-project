package com.elian.taskproject.data.dao

import androidx.room.*
import com.elian.taskproject.data.model.User

@Dao
interface UserDAO
{
    @Query("SELECT * FROM user_table WHERE id = :id")
    fun select(id: Long): User

    @Query("SELECT * FROM user_table")
    fun selectAll(): List<User>

    @Insert
    fun insert(user: User): Long
}