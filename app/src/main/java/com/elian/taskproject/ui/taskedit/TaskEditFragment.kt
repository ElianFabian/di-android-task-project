package com.elian.taskproject.ui.taskedit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.taskproject.R
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.utils.DataUtils
import com.elian.taskproject.databinding.FragmentTaskEditBinding
import com.elian.taskproject.ui.taskadd.TaskAddFragment
import java.time.LocalDate
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

    //endregion

    //region Methods

    private fun fillFieldsWithSelectedTask(task: Task) = with(binding)
    {
        tieName.setText(task.name)
        tieDescription.setText(task.description)
        spImportance.setSelection(task.importance.ordinal)
        etDate.setText(DataUtils.dateFormat.format(Date(task.endDate as Long)))
    }

    //endregion
}