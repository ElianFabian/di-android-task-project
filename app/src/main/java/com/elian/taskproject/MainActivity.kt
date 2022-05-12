package com.elian.taskproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.elian.taskproject.data.dao.UserDAO
import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
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

        if (userDAO.selectAll().isEmpty())
        {
            createUser()
        }
        else setContentView(R.layout.activity_main)
    }

    private fun createUser()
    {
        val randomLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".random()
        val uuid = UUID.randomUUID().toString().replace("-", "")

        val user = User(email = "$randomLetter$uuid@gmail.com")

        addUserToFirebase(user).addOnCompleteListener()
        {
            userDAO.insert(user)
            setContentView(R.layout.activity_main)
        }
    }

    private fun addUserToFirebase(user: User): Task<DocumentReference>
    {
        val collectionPath = "users"

        return Firebase.firestore
            .collection(collectionPath)
            .add(user)
            .addOnCompleteListener {
                if (it.isSuccessful)
                {
                    user.firebaseId = it.result.id
                    this.updateUser(user)
                }
            }
    }

    private fun updateUser(user: User): Task<Void>
    {
        val documentPath = "users/${user.firebaseId}"

        return Firebase.firestore
            .document(documentPath)
            .set(user)
    }
}
