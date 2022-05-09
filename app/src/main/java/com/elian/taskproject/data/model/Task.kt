package com.elian.taskproject.data.model

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "task_table")
data class Task(
    var name: String,
    var description: String,
    var importance: Importance,
    var estimatedEndDate: Long?,
    var endDate: Long? = null,
) :
    Serializable
{
    enum class Importance
    {
        LOW, MEDIUM, HIGH
    }
}
