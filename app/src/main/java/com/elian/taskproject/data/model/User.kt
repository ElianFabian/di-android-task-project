package com.elian.taskproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val email: String,
    val password: String,

    @PrimaryKey
    val id: Long = 0,
    
    var firebaseId: String = ""
)