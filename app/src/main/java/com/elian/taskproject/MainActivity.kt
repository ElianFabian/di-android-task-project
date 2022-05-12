package com.elian.taskproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elian.taskproject.data.database.AppDatabase

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        AppDatabase.create(this)
    }
}