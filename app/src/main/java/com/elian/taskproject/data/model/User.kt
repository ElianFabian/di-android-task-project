package com.elian.taskproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_table")
data class User(
    val email: String = "",

    @PrimaryKey
    val id: Long = 0,
) :
    Serializable