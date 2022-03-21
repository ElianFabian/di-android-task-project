package com.elian.myapplication.ui.taskadd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.myapplication.R
import com.elian.myapplication.databinding.FragmentTaskAddBinding

class TaskAddFragment : Fragment()
{
    private lateinit var binding: FragmentTaskAddBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentTaskAddBinding.inflate(inflater)
        return binding.root
    }
}