package com.elian.myapplication.ui.datepicker

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment

class DatePickerFragment(val listener: (day: Int, month: Int, year: Int) -> Unit) :

    DialogFragment(), DatePickerDialog.OnDateSetListener
{
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
    {
        
    }
}