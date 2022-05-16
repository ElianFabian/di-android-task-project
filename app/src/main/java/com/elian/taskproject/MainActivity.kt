package com.elian.taskproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.database.dao.UserDAO
import com.elian.taskproject.data.model.AppInformation
import com.elian.taskproject.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity()
{
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        initApplication()
    }

    private fun initApplication()
    {
        AppDatabase.create(this)

        userDAO = AppDatabase.instance.userDAO

        val userExists = AppDatabase.executorService.submit(userDAO::userExists).get()

        if (userExists)
        {
            AppInformation.currentUser = AppDatabase.executorService.submit(userDAO::getUser).get()

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
            AppInformation.currentUser = user

            AppDatabase.executorService.execute { userDAO.insert(user) }

            setContentView(R.layout.activity_main)
        }
    }
}
