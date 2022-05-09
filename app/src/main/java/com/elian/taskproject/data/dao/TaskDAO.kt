package com.elian.taskproject.data.dao

import androidx.room.Insert
import com.elian.taskproject.data.model.Task

interface TaskDAO
{
    @Insert
    fun insert(task: Task)
}