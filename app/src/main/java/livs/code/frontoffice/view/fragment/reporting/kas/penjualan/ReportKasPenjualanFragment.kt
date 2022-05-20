package livs.code.frontoffice.view.fragment.reporting.kas.penjualan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import livs.code.frontoffice.MyApp
import livs.code.frontoffice.databinding.FragmentReportKasPenjualanBinding
import livs.code.frontoffice.helper.utils
import livs.code.frontoffice.view.fragment.reporting.ReportViewModel

class ReportKasPenjualanFragment : Fragment() {

    private var _binding: FragmentReportKasPenjualanBinding? = null
    private val binding get() = _binding!!
    private var BASE_URL = ""
    private var tanggal = ""
    private var shift = ""
    private var username = ""
    private lateinit var reportViewModel: ReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding= FragmentReportKasPenjualanBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BASE_URL = (requireActivity().applicationContext as MyApp).baseUrl
        tanggal = arguments?.getString(DATA_TANGGAL).toString()
        shift = arguments?.getString(DATA_SHIFT).toString()
        username = arguments?.getString(DATA_USERNAME).toString()

        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        reportViewModel.getStatusKas(BASE_URL, tanggal, shift, username).observe(viewLifecycleOwner, {data ->

            binding.tvPendapatanLain.text = utils.getCurrency(data.dataStatusKas.jumlahPendapatanLain)

            binding.tvUangMukaCinBelumBayar.text = utils.getCurrency(data.dataStatusKas.jumlahUangMukaCheckinSudahBelumBayar)

            binding.tvUangMukaRsvBelumCheckin.text = utils.getCurrency(data.dataStatusKas.jumlahReservasiBelumCheckin)
            binding.tvUangMukaRsvSudahCheckin.text = utils.getCurrency(data.dataStatusKas.jumlahReservasiSudahCheckin)
            binding.tvUangMukaRsvCinBelumBayar.text = utils.getCurrency(data.dataStatusKas.jumlahReservasiSudahCheckinBelumBayar)

            binding.tvTotalHutangRsv.text = utils.getCurrency(data.dataStatusKas.totalHutangReservasi)
            binding.tvTotalKamar.text = utils.getCurrency(data.dataStatusKas.jumlahNilaiKamar)
            binding.tvValueFnb.text = utils.getCurrency(data.dataStatusKas.makananMinuman)
            binding.tvValueSmartCard.text = utils.getCurrency(0)

            binding.tvTotal.text = utils.getCurrency(data.dataStatusKas.totalPenjualan)
        })
    }

    companion object {
        const val DATA_TANGGAL = "tanggal"
        const val DATA_SHIFT = "shift"
        const val DATA_USERNAME = "username"
    }
}