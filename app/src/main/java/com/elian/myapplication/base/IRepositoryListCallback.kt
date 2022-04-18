package com.elian.myapplication.base

import com.elian.myapplication.data.model.Task

interface IRepositoryListCallback
{
    fun onSuccess(list: List<Task>)
    fun onNoData()
}