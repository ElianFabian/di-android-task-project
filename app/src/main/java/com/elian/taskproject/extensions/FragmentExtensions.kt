package com.elian.taskproject.extensions

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

fun Fragment.navigate(@IdRes action: Int)
{
    NavHostFragment.findNavController(this).navigate(action)
}

fun Fragment.navigate(@IdRes action: Int, args: Bundle?)
{
    NavHostFragment.findNavController(this).navigate(action, args)
}

fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(context, text, duration).show()
}