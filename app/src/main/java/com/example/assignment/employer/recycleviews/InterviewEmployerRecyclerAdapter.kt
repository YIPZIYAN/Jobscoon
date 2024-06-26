package com.example.assignment.employer.recycleviews

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.provider.ContactsContract.RawContacts.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.CustomDialog
import com.example.assignment.R
import com.example.assignment.databinding.FragmentFindJobsEmployeeBinding
import com.example.assignment.databinding.ItemInterviewEmployerBinding
import com.example.assignment.databinding.ItemJobPostBinding
import com.example.assignment.databinding.NavigationEmployeeBinding
import com.example.assignment.dataclass.JobInterviewItem
import com.example.assignment.dataclass.JobPostItem
import com.example.assignment.employee.EmployeeNavHost
import com.example.assignment.employee.FindJobsEmployeeViewModel
import com.example.assignment.employee.JobDetailsEmployeeFragment
import com.example.assignment.employer.InterviewEmployerViewModel
import com.example.assignment.employer.JobPostedEmployerViewModel

class InterviewEmployerRecyclerAdapter(
    private val viewModel: InterviewEmployerViewModel,
    private val context: Context
) :
    RecyclerView.Adapter<InterviewEmployerRecyclerAdapter.ViewHolder>() {

    lateinit var binding: ItemInterviewEmployerBinding
    private var dataList = listOf<JobInterviewItem>()
    fun setItem(JobInterviewItem: List<JobInterviewItem>) {
        this.dataList = JobInterviewItem
    }

    inner class ViewHolder(val binding: ItemInterviewEmployerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JobInterviewItem) {
            binding.interviewItem = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemInterviewEmployerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
        if (item.type == "virtual") {
            holder.binding.iconLocation.setImageResource(R.drawable.icon_link)
            holder.binding.locationOrLink.text = item.link
        } else if (item.type == "physical") {
            holder.binding.iconLocation.setImageResource(R.drawable.icon_location)
            holder.binding.locationOrLink.text = item.location
        }

        //status
        val textColorId = when(item.status) {
            "accept" -> R.color.accepted_text_color
            "pending" -> R.color.pending_text_color
            "declined" -> R.color.rejected_text_color
            else -> R.color.pending_text_color
        }
        val textBackgroundTintId = when(item.status) {
            "accept" -> R.color.accepted_layout_color
            "pending" -> R.color.pending_layout_color
            "declined" -> R.color.rejected_layout_color
            else -> R.color.pending_layout_color
        }

        val textColor = ContextCompat.getColor(holder.binding.status1.context, textColorId)
        val backgroundTintColor = ContextCompat.getColor(holder.binding.status1.context, textBackgroundTintId)
        holder.binding.status1.apply {
            setTextColor(textColor)
            backgroundTintList = ColorStateList.valueOf(backgroundTintColor)
        }

        if(item.status == "declined"){
            holder.binding.acceptButton.apply {
                text = "RESET"
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_update_24, 0, 0, 0)
            }
        }else {
            holder.binding.acceptButton.apply {
                text = "EDIT"
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_edit, 0, 0, 0)
            }
        }

        holder.apply {

            binding.declinedButton.setOnClickListener {
                val dialog = CustomDialog.customDialog(
                    context,
                    "Delete Interview",
                    "Are You Sure To Delete Interview?"
                )
                dialog.show()
                dialog.findViewById<Button>(R.id.btn_done).setOnClickListener {
                    viewModel.id=item.id!!
                    viewModel.delete()
                    it.isClickable = false
                    binding.declinedButton.isEnabled = false
                    dialog.dismiss()
                }

                dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
                    dialog.dismiss()
                    it.isClickable = false
                    binding.declinedButton.isEnabled = false
                }
            }

            binding.acceptButton.setOnClickListener {
                viewModel.navigating.value=true
                viewModel.id=item.id!!
                it.findNavController().navigate(R.id.action_interviewMainFragment_to_editInterviewFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}
