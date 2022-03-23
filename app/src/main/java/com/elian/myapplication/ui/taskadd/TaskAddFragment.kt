package com.elian.myapplication.ui.taskadd

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.NavHostFragment
import com.elian.myapplication.R
import com.elian.myapplication.databinding.FragmentTaskAddBinding

class TaskAddFragment : Fragment()
{
    private lateinit var binding: FragmentTaskAddBinding

    //region Fragment Methods

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTaskAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        // initSpinnerAdapter()

        binding.fab.setOnClickListener()
        {
            NavHostFragment.findNavController(this).navigateUp()
        }
    }

    //endregion

    //region Methods

    private fun showDatePickerDialog()
    {
        
    }

    //    private fun initSpinnerAdapter()
//    {
//        val spinnerAdapter = ArrayAdapter(
//            activity as Context,
//            android.R.layout.simple_spinner_dropdown_item,
//            resources.getStringArray(R.array.importance)
//        )
//
//        binding.spImportance.adapter = spinnerAdapter
//    }

    //endregion
}