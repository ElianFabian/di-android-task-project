package com.elian.myapplication.data.model

data class Task(
    val name: String,
    val description: String,
    val importance: Importance,
    val endDateEstimated: Long,
    val endDate: Long?,
)
{
    enum class Importance
    {
        LOW, MEDIUM, HIGH
    }
}
