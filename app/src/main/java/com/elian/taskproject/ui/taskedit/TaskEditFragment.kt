package com.elian.taskproject.ui.taskedit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.taskproject.R
import com.elian.taskproject.databinding.FragmentTaskEditBinding


class TaskEditFragment : Fragment()
{
    private lateinit var binding: FragmentTaskEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTaskEditBinding.inflate(inflater)
        return binding.root
    }
}