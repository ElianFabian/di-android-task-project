package com.elian.taskproject.ui.taskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.elian.taskproject.R
import com.elian.taskproject.base.BaseFragment
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.utils.DataUtils
import com.elian.taskproject.databinding.FragmentTaskManagerBinding
import com.elian.taskproject.ui.datepicker.DatePickerFragment
import java.util.*

class TaskManagerFragment : BaseFragment(),
    ITaskManagerContract.IView,
    DatePickerFragment.IOnDateSelectedListener
{
    private lateinit var binding: FragmentTaskManagerBinding
    override lateinit var presenter: ITaskManagerContract.IPresenter

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

        val selectedTask = arguments?.getSerializable(getString(R.string.bundleKey_selectedTask)) as Task?
        val selectedTaskPosition = arguments?.getInt(getString(R.string.bundleKey_selectedTask_position))

        initUI()

        if (selectedTask == null)
        {
            initUIForAddAction()
        }
        else initUIForEditAction(selectedTask, selectedTaskPosition!!)
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        //initSpinnerAdapter()

        binding.ibDate.setOnClickListener { showDatePickerDialog(this) }
    }

    private fun initUIForAddAction()
    {
        binding.fab.setOnClickListener { presenter.add(getTaskFromFields()) }
    }

    private fun initUIForEditAction(selectedTask: Task, position: Int)
    {
        binding.fab.setOnClickListener { presenter.edit(getTaskFromFields(), position) }
        fillFieldsWithSelectedTask(selectedTask)
    }

    private fun getTaskFromFields(): Task = with(binding)
    {
        var endDateEstimated: Long? = null

        if (etDate.text.toString().isNotEmpty())
        {
            endDateEstimated = DataUtils.dateFormat.parse(etDate.text.toString())?.time
        }

        Task(
            name = tieName.text.toString(),
            description = tieDescription.text.toString(),
            importance = Task.Importance.values()[spnImportance.selectedItemPosition],
            estimatedEndDate = endDateEstimated
        )
    }

    private fun fillFieldsWithSelectedTask(task: Task) = with(binding)
    {
        tieName.setText(task.name)
        tieDescription.setText(task.description)
        spnImportance.setSelection(task.importance.ordinal)
        etDate.setText(DataUtils.dateFormat.format(Date(task.estimatedEndDate as Long)))
    }

////  As we use android:entries in the Spinner we don't have to use this function,
////  but if we want more functionality we can then use it.
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

    //region ITaskManagerContract.IView

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

        Toast.makeText(context, "The task was successfully added.", Toast.LENGTH_SHORT).show()
    }

    override fun onEditSuccess()
    {
        NavHostFragment.findNavController(this).navigateUp()

        Toast.makeText(context, "The task was successfully edited.", Toast.LENGTH_SHORT).show()
    }

    override fun onAddFailure()
    {
        Toast.makeText(context, "Error when adding.", Toast.LENGTH_SHORT).show()
    }

    override fun onEditFailure()
    {
        Toast.makeText(context, "Error when editing.", Toast.LENGTH_SHORT).show()
    }

    //endregion

    //region DatePickerFragment.IOnDateSelectedListener

    override fun onDateSelected(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
    {
        // 1 is added because month is in range 0 to 11
        val monthWithFormat = String.format("%02d", month + 1)
        val dayOfMonthWithFormat = String.format("%02d", dayOfMonth)

        binding.etDate.setText("$year/$monthWithFormat/$dayOfMonthWithFormat")
    }

    //endregion
}