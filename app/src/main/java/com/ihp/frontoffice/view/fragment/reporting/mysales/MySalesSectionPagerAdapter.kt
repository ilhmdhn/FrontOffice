package com.ihp.frontoffice.view.fragment.reporting.mysales

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MySalesSectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = MySalesReportFragment()
        when(position){
            0 -> {
                fragment.arguments = Bundle().apply {
                    putString(MySalesReportFragment.KODE_PAGE, "1")
                }
            }
            1 -> {
                fragment.arguments = Bundle().apply {
                    putString(MySalesReportFragment.KODE_PAGE, "2")
                }
            }
            2 -> {
                fragment.arguments = Bundle().apply {
                    putString(MySalesReportFragment.KODE_PAGE, "3")
                }
            }
        }
        return fragment as Fragment
    }
}