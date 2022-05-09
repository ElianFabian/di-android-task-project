package com.elian.taskproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elian.taskproject.data.database.TaskDatabase

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        TaskDatabase.create(this)
    }
}