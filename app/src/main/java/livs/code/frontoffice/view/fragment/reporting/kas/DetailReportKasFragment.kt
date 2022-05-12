package livs.code.frontoffice.view.fragment.reporting.kas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import livs.code.frontoffice.R
import livs.code.frontoffice.databinding.FragmentDetailReportKasBinding


class DetailReportKasFragment : Fragment() {

    private var _binding: FragmentDetailReportKasBinding? = null
    private val binding get() = _binding!!
    private var BASE_URL = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        _binding = FragmentDetailReportKasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tanggal = DetailReportKasFragmentArgs.fromBundle(arguments as Bundle).tanggal
        val shift = DetailReportKasFragmentArgs.fromBundle(arguments as Bundle).shift
        val username = DetailReportKasFragmentArgs.fromBundle(arguments as Bundle).username

        val sectionPagerAdapter = ReportKasSectionPagerAdapter(requireActivity() as AppCompatActivity)
        sectionPagerAdapter.tanggal = tanggal
        sectionPagerAdapter.shift = shift
        sectionPagerAdapter.username = username

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter= sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        (activity as AppCompatActivity?)!!.supportActionBar?.elevation = 0f
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_pembayaran,
            R.string.tab_penjualan,
            R.string.tab_status_kamar
        )
    }
}