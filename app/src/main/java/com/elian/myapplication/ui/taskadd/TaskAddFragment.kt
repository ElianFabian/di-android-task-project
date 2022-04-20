package com.elian.myapplication.ui.taskadd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.elian.myapplication.data.model.Task
import com.elian.myapplication.databinding.FragmentTaskAddBinding
import com.elian.myapplication.ui.datepicker.DatePickerFragment
import java.text.SimpleDateFormat

class TaskAddFragment : Fragment(),
    ITaskAddContract.IView
{
    private lateinit var binding: FragmentTaskAddBinding
    private lateinit var presenter: ITaskAddContract.IPresenter

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd")

    //region Fragment Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        presenter = TaskAddPresenter(this)
    }

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

        initUI()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        // initSpinnerAdapter()

        binding.fab.setOnClickListener { presenter.onValidateFields(getTask()) }

        binding.ibDate.setOnClickListener { showDatePickerDialog() }
    }

    private fun showDatePickerDialog()
    {
        val datePicker = DatePickerFragment(::onDateSelected)

        datePicker.show(parentFragmentManager, "datePicker")

////      This is another way of doing it.
//        val datePicker = DatePickerFragment()
//        { year, month, dayOfMonth ->
//
//            onDateSelected(year, month, dayOfMonth)
//        }
    }

    private fun onDateSelected(year: Int, month: Int, dayOfMonth: Int)
    {
        val monthWithFormat = month.toString().format("%02d")
        val dayOfMonthWithFormat = dayOfMonth.toString().format("%02d")

        binding.etDate.setText(
            "$year/${monthWithFormat}/${dayOfMonthWithFormat}"
        )
    }

    /**
     * Gets a task using the information from the form.
     */
    private fun getTask(): Task
    {
        return with(binding)
        {
            Task(
                name = tieName.text.toString(),
                description = tieDescription.text.toString(),
                importance = Task.Importance.valueOf(spImportance.selectedItem.toString()),
                endDateEstimated = dateFormat.parse(etDate.text.toString())!!.time
            )
        }
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

    //region ITaskAddContract.IView

    override fun setNameEmptyError()
    {
        TODO("Not yet implemented")
    }

    override fun setDescriptionEmptyError()
    {
        TODO("Not yet implemented")
    }

    override fun setDateEmptyError()
    {
        TODO("Not yet implemented")
    }

    override fun cleanInputFieldsErrors()
    {
        TODO("Not yet implemented")
    }

    override fun onSuccess()
    {
        NavHostFragment.findNavController(this).navigateUp()
    }

    override fun onFailure()
    {
        TODO("Not yet implemented")
    }

    //endregion
}