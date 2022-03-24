package com.elian.myapplication.data.model

data class Task(val name: String, val importance: Importance)
{
    enum class Importance
    {
        LOW, MEDIUM, HIGH
    }
}
