package com.elian.taskproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "task_table")
data class Task(
    var name: String = "",
    var description: String = "",
    var importance: Importance = Importance.LOW,
    var endDate: Long? = null,
    var isCompleted: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val firebaseId: String = UUID.randomUUID().toString()
) :
    Serializable
{
    enum class Importance
    {
        LOW, MEDIUM, HIGH
    }
}
