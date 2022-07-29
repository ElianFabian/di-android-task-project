package com.elian.taskproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.elian.taskproject.R
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.util.DataUtils
import com.elian.taskproject.databinding.FragmentTaskManagerBinding
import com.elian.taskproject.util.ArgKeys
import com.elian.taskproject.view_model.TaskManagerViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TaskManagerFragment : Fragment(),
    DatePickerFragment.OnDateSelectedListener
{
    private lateinit var binding: FragmentTaskManagerBinding

    private val viewModel: TaskManagerViewModel by viewModels()

    private val taskFromFields: Task
        get() = with(binding)
        {
            var endDate: Long? = null

            if (etDate.text.toString().isNotEmpty())
            {
                endDate = DataUtils.dateFormat.parse(etDate.text.toString())?.time
            }

            Task(
                name = tieName.text.toString().trim(),
                description = tieDescription.text.toString().trim(),
                importance = Task.Importance.values()[spnImportance.selectedItemPosition],
                endDate = endDate,
            )
        }

    //region Fragment Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        initializeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTaskManagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val selectedTask = arguments?.getSerializable(ArgKeys.SelectedTask) as Task?
        val selectedTaskPosition = arguments?.getInt(ArgKeys.SelectedTaskPosition)

        initializeUI()

        val isTaskToEdit = selectedTask != null

        if (isTaskToEdit)
        {
            initUIForEditAction(selectedTask!!, selectedTaskPosition!!)
        }
        else initUIForAddAction()
    }

    //endregion

    //region Methods

    private fun initializeUI()
    {
        // For some reason it doesn't work if I put it in the layout file
        // this is to avoid pasting text in it
        binding.etDate.isClickable = false
        binding.etDate.isLongClickable = false

        binding.ibDate.setOnClickListener { showDatePickerDialog(this) }
    }

    private fun initializeViewModel()
    {
        viewModel.apply()
        {
            onTaskAdded = ::onTaskAdded
            onTaskUpdated = ::onTaskUpdated

            onValidateTask = ::onValidateTask
            onNameEmptyError = ::onNameEmptyError
            onDateEmptyError = ::onDateEmptyError
        }
    }

    private fun initUIForAddAction()
    {
        binding.fab.setOnClickListener { viewModel.add(taskFromFields) }
    }

    private fun initUIForEditAction(selectedTask: Task, position: Int)
    {
        binding.fab.setOnClickListener()
        {
            viewModel.update(
                updatedTask = taskFromFields.copy(
                    id = selectedTask.id, firebaseId = selectedTask.firebaseId
                ),
                position
            )
        }

        fillFieldsWithSelectedTask(selectedTask)
    }

    private fun fillFieldsWithSelectedTask(task: Task) = with(binding)
    {
        tieName.setText(task.name)
        tieDescription.setText(task.description)
        spnImportance.setSelection(task.importance.ordinal)
        etDate.setText(DataUtils.dateFormat.format(Date(task.endDate!!)))
    }

    private fun cleanInputFieldsErrors() = with(binding)
    {
        tilName.error = null
        tilDescription.error = null
        etDate.error = null
    }

    //endregion

    //region DatePickerFragment.OnDateSelectedListener

    override fun onDateSelected(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
    {
        // 1 is added because month is in range 0 to 11
        val monthFormatted = String.format("%02d", month + 1)
        val dayOfMonthFormatted = String.format("%02d", dayOfMonth)

        binding.etDate.setText("$year/$monthFormatted/$dayOfMonthFormatted")
    }

    //endregion

    //region Events

    private fun onNameEmptyError()
    {
        binding.tieName.error = getString(R.string.error_emptyField)
    }

    private fun onDateEmptyError()
    {
        binding.etDate.error = getString(R.string.error_emptyField)
    }

    private fun onValidateTask()
    {
        cleanInputFieldsErrors()
    }

    private fun onTaskAdded(addedTask: Task)
    {
        NavHostFragment.findNavController(this).navigateUp()
    }

    private fun onTaskUpdated(updatedTask: Task)
    {
        NavHostFragment.findNavController(this).navigateUp()
    }

    //endregion
}