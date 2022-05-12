package com.elian.taskproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task_table")
data class Task(
    var name: String = "",
    var description: String = "",
    var importance: Importance = Importance.LOW,
    var estimatedEndDate: Long? = null,
    var endDate: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var firebaseId: String = ""
) :
    Serializable
{
    enum class Importance
    {
        LOW, MEDIUM, HIGH
    }
}
