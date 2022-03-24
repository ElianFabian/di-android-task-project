package com.elian.myapplication.data.model

data class Task(val name: String, val importance: Importance, val endDate: Long)
{
    enum class Importance
    {
        LOW, MEDIUM, HIGH
    }
}
