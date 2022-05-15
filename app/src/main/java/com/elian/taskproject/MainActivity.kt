package com.elian.taskproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elian.taskproject.data.database.dao.UserDAO
import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity()
{
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        AppDatabase.create(this)

        userDAO = AppDatabase.getDatabase().userDAO

        if (userDAO.userExists())
        {
            setContentView(R.layout.activity_main)
        }
        else createUser()
    }

    private fun createUser()
    {
        val randomLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".random()
        val uuid = UUID.randomUUID().toString().replace("-", "")

        val user = User(email = "$randomLetter$uuid@gmail.com")

        val documentPath = "users/${user.email}"

        Firebase.firestore.document(documentPath).set(user).addOnCompleteListener()
        {
            userDAO.insert(user)
            setContentView(R.layout.activity_main)
        }
    }
}
