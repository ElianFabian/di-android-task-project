package com.elian.myapplication.ui.taskadd

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.elian.myapplication.R
import com.elian.myapplication.databinding.FragmentTaskAddBinding

class TaskAddFragment : Fragment()
{
    private lateinit var binding: FragmentTaskAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTaskAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart()
    {
        super.onStart()

        initSpinnerAdapter()
    }

    private fun initSpinnerAdapter()
    {
        val spinnerAdapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.importance)
        )

        binding.spImportance.adapter = spinnerAdapter
    }
}