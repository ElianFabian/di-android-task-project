package com.elian.taskproject.ui.task_manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.fragment.NavHostFragment
import com.elian.taskproject.R
import com.elian.taskproject.base.BaseFragment
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.util.DataUtils
import com.elian.taskproject.databinding.FragmentTaskManagerBinding
import com.elian.taskproject.util.extensions.toast
import com.elian.taskproject.ui.date_picker.DatePickerFragment
import com.elian.taskproject.util.ArgKeys
import java.util.*

class TaskManagerFragment : BaseFragment(),
    TaskManagerContract.View,
    DatePickerFragment.OnDateSelectedListener
{
    private lateinit var binding: FragmentTaskManagerBinding
    override lateinit var presenter: TaskManagerContract.Presenter

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

        presenter = TaskManagerPresenter(this)
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

        initUI()

        val isTaskToEdit = selectedTask != null

        if (isTaskToEdit)
        {
            initUIForEditAction(selectedTask!!, selectedTaskPosition!!)
        }
        else initUIForAddAction()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        // For some reason it doesn't work if I put it in the layout file
        // this is to avoid pasting text in it
        binding.etDate.isClickable = false
        binding.etDate.isLongClickable = false

        binding.ibDate.setOnClickListener { showDatePickerDialog(this) }
    }

    private fun initUIForAddAction()
    {
        binding.fab.setOnClickListener { presenter.add(taskFromFields) }
    }

    private fun initUIForEditAction(selectedTask: Task, position: Int)
    {
        binding.fab.setOnClickListener()
        {
            presenter.edit(
                editedTask = taskFromFields.copy(
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

    //region TaskManagerContract.View

    override fun setNameEmptyError()
    {
        binding.tieName.error = getString(R.string.error_emptyField)
    }

    override fun setDescriptionEmptyError()
    {
        binding.tieDescription.error = getString(R.string.error_emptyField)
    }

    override fun setDateEmptyError()
    {
        binding.etDate.error = getString(R.string.error_emptyField)
    }

    override fun cleanInputFieldsErrors() = with(binding)
    {
        tilName.error = null
        tilDescription.error = null
        etDate.error = null
    }

    override fun onAddSuccess()
    {
        NavHostFragment.findNavController(this).navigateUp()

        toast("The task was successfully added.")
    }

    override fun onAddFailure()
    {
        toast("Error when adding.")
    }

    override fun onEditFailure()
    {
        toast("Error when editing.")
    }

    override fun onEditSuccess()
    {
        NavHostFragment.findNavController(this).navigateUp()

        toast("The task was successfully edited.")
    }

    //endregion
}