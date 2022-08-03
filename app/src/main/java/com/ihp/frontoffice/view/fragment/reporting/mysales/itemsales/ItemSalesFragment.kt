package com.ihp.frontoffice.view.fragment.reporting.mysales.itemsales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.databinding.FragmentItemSalesBinding
import com.ihp.frontoffice.view.fragment.reporting.ReportViewModel

class ItemSalesFragment : Fragment() {

    private var _binding: FragmentItemSalesBinding? = null
    private val binding get() = _binding!!
    private var url = ""
    private var username = ""
    private var user = User()
    private lateinit var reportViewModel: ReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentItemSalesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = (requireActivity().applicationContext as MyApp).baseUrl
        user = (requireActivity().applicationContext as MyApp).userFo
        username = user.userId

        val pagerAdapter = SalesItemSectionPagerAdapter(requireActivity() as AppCompatActivity)
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text =resources.getString(TAB_TITLES[position])
        }.attach()

        reportViewModel.getSaleItemToday(url, username)
        reportViewModel.getSaleItemWeekly(url, username)
        reportViewModel.getSaleItemMonthly(url, username)

        binding.lySwipe.setOnRefreshListener {
            binding.lySwipe.isRefreshing = false
            reportViewModel.getSaleItemToday(url, username)
            reportViewModel.getSaleItemWeekly(url, username)
            reportViewModel.getSaleItemMonthly(url, username)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.today,
            R.string.weekly,
            R.string.monthly
        )
    }
}