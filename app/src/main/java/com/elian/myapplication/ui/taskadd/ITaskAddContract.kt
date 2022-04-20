package com.elian.myapplication.ui.taskadd

import com.elian.myapplication.data.model.Task

interface ITaskAddContract
{
    interface IView
    {
        
    }
    
    interface IPresenter
    {
        
    }
    
    interface IInteractor
    {
        
    }
    
    interface IRepository
    {
        
    }

    interface IOnRepositoryCallback
    {
        fun onSuccess()
        fun onFailure()
    }

    interface IOnInteractorListener : IOnRepositoryCallback
}
