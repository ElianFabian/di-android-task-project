package com.elian.myapplication.ui.taskadd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.elian.myapplication.R
import com.elian.myapplication.data.model.Task
import com.elian.myapplication.databinding.FragmentTaskAddBinding
import com.elian.myapplication.ui.datepicker.DatePickerFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
        // 1 is added because month is on range 0 to 11
        val monthWithFormat = String.format("%02d", month + 1)
        val dayOfMonthWithFormat = String.format("%02d", dayOfMonth)

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
            var endDateEstimated: Long? = null

            if (etDate.text.toString().isNotEmpty())
            {
                endDateEstimated = dateFormat.parse(etDate.text.toString()).time
            }

            Task(
                name = tieName.text.toString(),
                description = tieDescription.text.toString(),
                importance = Task.Importance.values()[spImportance.selectedItemPosition],
                endDateEstimated = endDateEstimated
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


    override fun onSuccess()
    {
        NavHostFragment.findNavController(this).navigateUp()

        Toast.makeText(context, "The task was successfully added.", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure()
    {
        Toast.makeText(context, "Error when adding.", Toast.LENGTH_SHORT).show()
    }

    //endregion
}