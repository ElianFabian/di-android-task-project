package com.elian.taskproject.data.model

data class Task(
    var name: String,
    var description: String,
    var importance: Importance,
    var endDateEstimated: Long?,
    var endDate: Long? = null,
)
{
    enum class Importance
    {
        LOW, MEDIUM, HIGH
    }
}
