package com.example.assignment.employer

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.assignment.AddCareerDevelopmentEmployerViewModel
import com.example.assignment.PostJobEmployerViewModel
import com.example.assignment.R
import com.example.assignment.databinding.FragmentAddCareerDevelopmentEmployerBinding
import com.example.assignment.dataclass.CareerDevelopmentItem
import com.example.assignment.dataclass.JobPostItem
import java.util.*

class AddCareerDevelopmentEmployerFragment : Fragment() {

    companion object {
        fun newInstance() = AddCareerDevelopmentEmployerFragment()
    }

    private lateinit var viewModel: AddCareerDevelopmentEmployerViewModel
    private lateinit var binding: FragmentAddCareerDevelopmentEmployerBinding
    val types = arrayOf("physical", "virtual")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate<FragmentAddCareerDevelopmentEmployerBinding>(
            inflater,
            R.layout.fragment_add_career_development_employer, container, false
        )

        binding.iconArrowback.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.editType.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                types
            )
        )

        binding.editType.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Do something after the text is changed
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do something before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.editType.text.toString() == "physical") {
                    binding.textLocationS.visibility = View.VISIBLE
                    binding.textLinkS.visibility = View.GONE
                }
                else if(binding.editType.text.toString() == "virtual") {
                    binding.textLocationS.visibility = View.GONE
                    binding.textLinkS.visibility = View.VISIBLE
                }
            }
        })

        val calendar = Calendar.getInstance()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        var clickedDate : Int = 0
        var clickedTime : Int = 0

        val today = Calendar.getInstance().apply {
            clear(Calendar.HOUR_OF_DAY)
            clear(Calendar.MINUTE)
            clear(Calendar.SECOND)
            clear(Calendar.MILLISECOND)
        }.timeInMillis

        binding.editDate.keyListener = null
        binding.editEndDate.keyListener = null
        binding.editStartTime.keyListener = null
        binding.editEndTime.keyListener = null

        if (clickedDate == 0) {
            binding.editEndDate.setOnClickListener{
                Toast.makeText(requireContext(), "Please select your start date first.", Toast.LENGTH_LONG).show()
            }
        }
        if (clickedTime == 0) {
            binding.editEndTime.setOnClickListener{
                Toast.makeText(requireContext(), "Please select your start time first.", Toast.LENGTH_LONG).show()
            }
        }

        binding.editDate.setOnClickListener{
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.editDate.setText("$year-${month + 1}-$dayOfMonth")
                clickedDate = 1
                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val selectedDate = selectedCalendar.timeInMillis

                binding.editEndDate.setOnClickListener {
                    val endDatePicker = DatePickerDialog(requireContext(), { _, endYear, endMonth, endDayOfMonth ->
                        binding.editEndDate.setText("$endYear-${endMonth + 1}-$endDayOfMonth")
                    }, initialYear, initialMonth, initialDay)

                    endDatePicker.datePicker.minDate = selectedDate // Set minimum date to the selected date from editDate
                    endDatePicker.show()
                }
            }, initialYear, initialMonth, initialDay)
            datePicker.datePicker.minDate = today
            datePicker.show()
        }

        binding.editStartTime.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                if(selectedHour in 0..9 && selectedMinute in 0 .. 9) {
                    binding.editStartTime.setText("0$selectedHour:0$selectedMinute")
                }
                else if (selectedHour in 0..9) {
                    binding.editStartTime.setText("0$selectedHour:$selectedMinute")
                }
                else if (selectedMinute in 0 .. 9) {
                    binding.editStartTime.setText("$selectedHour:0$selectedMinute")
                }
                else {
                    binding.editStartTime.setText("$selectedHour:$selectedMinute")
                }
                clickedTime = 1

                binding.editEndTime.setOnClickListener{
                    val endTimePicker = TimePickerDialog(requireContext(), { _, endHour, endMinute ->

                        if (endHour > selectedHour || ((endHour == selectedHour) && (endMinute > selectedMinute))) {
                            if(endHour in 0..9 && endMinute in 0 .. 9) {
                                binding.editEndTime.setText("0$endHour:0$endMinute")
                            }
                            else if (endHour in 0..9) {
                                binding.editEndTime.setText("0$endHour:$endMinute")
                            }
                            else if (endMinute in 0 .. 9) {
                                binding.editEndTime.setText("$endHour:0$endMinute")
                            }
                            else {
                                binding.editEndTime.setText("$endHour:$endMinute")
                            }
                        }
                        else
                            Toast.makeText(requireContext(), "End Time must greater than Start Time", Toast.LENGTH_LONG).show()

                    }, hour, minute, false)

                    endTimePicker.show()
                }

            }, hour, minute, false)


            timePicker.show()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddCareerDevelopmentEmployerViewModel::class.java)

        binding.confirmInterviewBtn.setOnClickListener{
            var capacity: Int? = null

            if (binding.editCapacity.text.toString() != "") {
                capacity = binding.editCapacity.text.toString().toInt()
            }

            viewModel.createAddCareer(
                CareerDevelopmentItem(
                    title = binding.editTitle.text.toString(),
                    date_start = binding.editDate.text.toString(),
                    date_end = binding.editEndDate.text.toString(),
                    start_time = binding.editStartTime.text.toString(),
                    end_time = binding.editEndTime.text.toString(),
                    type = binding.editType.text.toString(),
                    location = binding.editLocation.text.toString(),
                    link = binding.editLink.text.toString(),
                    capacity = capacity,
                    description = binding.editDescription.text.toString()

                )
            )
        }

        viewModel.validationResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it.success) {
                Toast.makeText(
                    requireContext(),
                    "New Career Posted Successfully!",
                    Toast.LENGTH_LONG
                ).show()
                view?.findNavController()?.popBackStack()
            } else {
                Toast.makeText(requireContext(), it.errorMsg, Toast.LENGTH_LONG).show()
            }
        })

    }

}