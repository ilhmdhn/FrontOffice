package livs.code.frontoffice.view.fragment.reporting.mysales

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
import livs.code.frontoffice.MyApp
import livs.code.frontoffice.R
import livs.code.frontoffice.databinding.FragmentMySalesReportParentBinding
import livs.code.frontoffice.view.fragment.reporting.ReportViewModel

class MySalesReportParentFragment : Fragment() {

    private var _binding: FragmentMySalesReportParentBinding? = null
    private val binding get() = _binding!!
    private var url = ""
    private var username = ""
    private lateinit var reportViewModel: ReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMySalesReportParentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = (requireActivity().applicationContext as MyApp).baseUrl
        val getUserFO = (requireActivity().applicationContext as MyApp).userFo
        username = getUserFO.userId
        val mySalesSectionPagerAdapter = MySalesSectionPagerAdapter(requireActivity() as AppCompatActivity)

        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = mySalesSectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.lySwipe.setOnRefreshListener {
            binding.lySwipe.isRefreshing = false
            reportViewModel.getSalesWeekly(url, username)
            reportViewModel.getSalesToday(url, username)
            reportViewModel.getSalesMonthly(url, username)
        }

        reportViewModel.getSalesWeekly(url, username)
        reportViewModel.getSalesToday(url, username)
        reportViewModel.getSalesMonthly(url, username)
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