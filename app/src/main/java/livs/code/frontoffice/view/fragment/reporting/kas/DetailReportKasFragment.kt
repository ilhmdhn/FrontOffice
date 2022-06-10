package livs.code.frontoffice.view.fragment.reporting.kas

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import livs.code.frontoffice.MyApp
import livs.code.frontoffice.R
import livs.code.frontoffice.data.remote.respons.StatusKasResponse
import livs.code.frontoffice.databinding.FragmentDetailReportKasBinding
import livs.code.frontoffice.helper.utils
import livs.code.frontoffice.view.fragment.reporting.ReportViewModel

class DetailReportKasFragment : Fragment() {

    private var _binding: FragmentDetailReportKasBinding? = null
    private val binding get() = _binding!!
    private var url = ""
    private lateinit var reportViewModel: ReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailReportKasBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = (requireActivity().applicationContext as MyApp).baseUrl
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        val tanggal = DetailReportKasFragmentArgs.fromBundle(arguments as Bundle).tanggal
        val shift = DetailReportKasFragmentArgs.fromBundle(arguments as Bundle).shift
        val username = DetailReportKasFragmentArgs.fromBundle(arguments as Bundle).username

        binding.lySwipe.setOnRefreshListener {
            binding.lySwipe.isRefreshing = false
            reportViewModel.getStatusKas(url, tanggal, shift, username)
        }
        reportViewModel.getStatusKas(url, tanggal, shift, username)
        binding.tvShift.text = "Shift $shift"
        binding.tvShiftPiutang.text = "Piutang Shift $shift"
        reportViewModel.statusKas.observe(viewLifecycleOwner, { data ->
            if (data.state == true) {
                if (data.dataStatusKas != null) {
                    binding.ltEmpty.visibility = View.GONE
                    binding.viewPager.visibility = View.VISIBLE
                    binding.tvTanggal.setText("Tanggal: ${data.dataStatusKas.tanggal.trim()}")
                    binding.tvHitungPiutang.setText(
                        "(Room ${utils.getCurrency(data.dataStatusKas.piutangRoom)} + FnB ${
                            utils.getCurrency(
                                data.dataStatusKas.piutangFnb
                            )
                        }) - UM ${utils.getCurrency(data.dataStatusKas.uangMuka)}"
                    )
                    binding.tvTotalPiutang.setText(utils.getCurrency(data.dataStatusKas.piutangRoom + data.dataStatusKas.piutangFnb - data.dataStatusKas.uangMuka))
                } else {
                    binding.viewPager.visibility = View.GONE
                    binding.ltEmpty.visibility = View.VISIBLE
                }
            } else{
                binding.viewPager.visibility = View.GONE
                binding.ltEmpty.setAnimation("erroranimation.json")
                binding.ltEmpty.visibility = View.VISIBLE
            }
        })

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
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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