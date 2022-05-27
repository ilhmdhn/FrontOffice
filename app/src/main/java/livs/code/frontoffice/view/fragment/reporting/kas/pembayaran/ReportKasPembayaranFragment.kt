package livs.code.frontoffice.view.fragment.reporting.kas.pembayaran

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import livs.code.frontoffice.MyApp
import livs.code.frontoffice.databinding.FragmentReportKasPembayaranBinding
import livs.code.frontoffice.helper.utils
import livs.code.frontoffice.view.fragment.reporting.ReportViewModel
import livs.code.frontoffice.view.fragment.reporting.kas.DetailReportKasFragmentDirections

class ReportKasPembayaranFragment : Fragment() {

    private var _binding: FragmentReportKasPembayaranBinding? = null
    private val binding get() = _binding!!
    private lateinit var reportViewModel: ReportViewModel
    private var tanggal = ""
    private var shift = ""
    private var username = ""
    private var totalCash = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportKasPembayaranBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tanggal = arguments?.getString(DATA_TANGGAL).toString()
        shift = arguments?.getString(DATA_SHIFT).toString()
        username = arguments?.getString(DATA_USERNAME).toString()


        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        reportViewModel.statusKas.observe(viewLifecycleOwner, { data ->
            if(data.state == true){
            binding.tvJumlahTransfer.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranTransfer)
            binding.tvJumlahPoinMembership.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranPoinMembership)
            binding.tvJumlahEmoney.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranEmoney)
            binding.tvJumlahTunai.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranCash)
            binding.tvJumlahCreditCard.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranCreditCard)
            binding.tvJumlahDebitCard.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranDebetCard)
            binding.tvJumlahVoucher.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranVoucher)
            binding.tvJumlahPiutang.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranPiutang)
            binding.tvJumlahEntertainment.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranComplimentary)
            binding.tvJumlahUangMuka.text = utils.getCurrency(data.dataStatusKas.jumlahPembayaranUangMuka)
            binding.tvJumlahTotal.text = utils.getCurrency(data.dataStatusKas.totalPembayaran)
            totalCash = data.dataStatusKas.jumlahPembayaranCash
                if (data.dataStatusKas.jumlahPembayaranCash != 0){
                    binding.btnCashList.isEnabled = true
                }
            }
        })

        binding.btnCashList.setOnClickListener {
            val toPecahanFragment = DetailReportKasFragmentDirections.actionDetailReportKasFragmentToPecahanFragment()
            toPecahanFragment.tanggal = tanggal
            toPecahanFragment.shift = shift
            toPecahanFragment.totalTunai = totalCash
            Navigation.findNavController(it).navigate(toPecahanFragment)
        }

    }
    companion object {
        const val DATA_TANGGAL = "tanggal"
        const val DATA_SHIFT = "shift"
        const val DATA_USERNAME = "username"
    }
}