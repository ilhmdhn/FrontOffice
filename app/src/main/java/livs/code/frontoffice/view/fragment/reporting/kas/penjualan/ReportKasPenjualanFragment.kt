package livs.code.frontoffice.view.fragment.reporting.kas.penjualan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import livs.code.frontoffice.R
import livs.code.frontoffice.data.remote.respons.DataStatusKas
import livs.code.frontoffice.databinding.FragmentReportKasPenjualanBinding
import livs.code.frontoffice.view.fragment.reporting.kas.statuskamar.ReportKasStatusKamarFragment

class ReportKasPenjualanFragment : Fragment() {

    private var _binding: FragmentReportKasPenjualanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding= FragmentReportKasPenjualanBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val DATA_TANGGAL = "tanggal"
        const val DATA_SHIFT = "shift"
        const val DATA_USERNAME = "username"
    }
}