package com.elian.taskproject

import android.app.Application
import com.elian.taskproject.data.TaskDatabase

// This class must be added into the AndroidManifest.xml in order to work
/*
<application
            android:name=".TaskApplication"
            ...
 */
class TaskApplication : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        TaskDatabase.create(this)
    }
}