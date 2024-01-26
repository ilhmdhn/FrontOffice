package com.ihp.frontoffice.view.fragment.reporting.kas

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import es.dmoral.toasty.Toasty
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.DataStatusKas
import com.ihp.frontoffice.data.remote.respons.StatusKasResponse
import com.ihp.frontoffice.data.repository.IhpRepository
import com.ihp.frontoffice.databinding.FragmentDetailReportKasBinding
import com.ihp.frontoffice.helper.Printer58
import com.ihp.frontoffice.helper.utils
import com.ihp.frontoffice.view.fragment.reporting.ReportViewModel

class DetailReportKasFragment : Fragment() {

    private var _binding: FragmentDetailReportKasBinding? = null
    private val binding get() = _binding!!
    private var url = ""
    private lateinit var reportViewModel: ReportViewModel
    private var tanggal = ""
    private var shift = ""
    private var username = ""
    private var chusr = ""
    private val ihpRepository = IhpRepository()
    private lateinit var dataKas: StatusKasResponse

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailReportKasBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = (requireActivity().applicationContext as MyApp).baseUrl
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        tanggal = DetailReportKasFragmentArgs.fromBundle(arguments as Bundle).tanggal
        shift = DetailReportKasFragmentArgs.fromBundle(arguments as Bundle).shift
        username = DetailReportKasFragmentArgs.fromBundle(arguments as Bundle).username
        chusr = (requireActivity().applicationContext as MyApp).userFo.userId

        binding.lySwipe.setOnRefreshListener {
            binding.lySwipe.isRefreshing = false
            reportViewModel.getStatusKas(url, tanggal, shift, username)
        }
        binding.tvShift.text = "Shift $shift"
        binding.tvTanggal.setText("Tanggal: $tanggal")
        binding.tvShiftPiutang.text = "Piutang Shift $shift"
        reportViewModel.statusKas.observe(viewLifecycleOwner, { data ->
            dataKas = data
            if (data.state == true) {
                if (data.dataStatusKas != null) {
                    binding.ltEmpty.visibility = View.GONE
                    binding.viewPager.visibility = View.VISIBLE
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
            } else if(data.state == false){
                binding.viewPager.visibility = View.GONE
                binding.ltEmpty.setAnimation("erroranimation.json")
                binding.ltEmpty.playAnimation()
                Toasty.error(requireActivity(),  data.message, Toasty.LENGTH_SHORT, true).show()
                binding.ltEmpty.visibility = View.VISIBLE
            } else{
                binding.viewPager.visibility = View.GONE
                binding.ltEmpty.setAnimation("loading.json")
                binding.ltEmpty.playAnimation()
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

        binding.fabPrint.setOnClickListener {

            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage(R.string.print_kas)
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            //    ihpRepository.printKas(url, tanggal, shift, chusr, requireActivity())
                dataKas.dataStatusKas?.let { it1 -> Printer58().printerKas(shift.toInt(), it1, chusr) }
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
                dialog.dismiss()
            }

            if(dataKas.state==true){
                builder.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reportViewModel.getStatusKas(url, tanggal, shift, username)
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