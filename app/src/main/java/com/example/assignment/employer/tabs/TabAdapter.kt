package com.example.assignment.employer.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.assignment.employer.InterviewEmployerFragment
import com.example.assignment.employer.InterviewHistoryFragment

class TabAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment = if (position==0){
        InterviewEmployerFragment()
    }else  {
        InterviewHistoryFragment()
    }
}