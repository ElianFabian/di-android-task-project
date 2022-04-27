package com.elian.taskproject.ui.taskedit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.utils.DataUtils
import com.elian.taskproject.databinding.FragmentTaskEditBinding
import java.util.*


class TaskEditFragment : Fragment()
{
    //region Fragment Methods

    private lateinit var binding: FragmentTaskEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTaskEditBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val selectedTask = arguments?.getSerializable("selectedTask") as Task

        fillFieldsWithSelectedTask(selectedTask)
    }

    //endregion

    //region Methods

    private fun fillFieldsWithSelectedTask(task: Task) = with(binding)
    {
        tieName.setText(task.name)
        tieDescription.setText(task.description)
        spImportance.setSelection(task.importance.ordinal)
        etDate.setText(DataUtils.dateFormat.format(Date(task.estimatedEndDate as Long)))
    }

    //endregion
}