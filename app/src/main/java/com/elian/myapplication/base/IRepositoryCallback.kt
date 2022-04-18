package com.elian.myapplication.base

import com.elian.myapplication.data.model.Task

interface IRepositoryCallback
{
    fun onSuccess(list: List<Task>)
    fun onNoData()
}