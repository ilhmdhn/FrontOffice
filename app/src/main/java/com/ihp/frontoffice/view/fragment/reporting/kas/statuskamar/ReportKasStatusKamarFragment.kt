package com.ihp.frontoffice.view.fragment.reporting.kas.statuskamar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ihp.frontoffice.databinding.FragmentReportKasStatusKamarBinding
import com.ihp.frontoffice.view.fragment.reporting.ReportViewModel

class ReportKasStatusKamarFragment : Fragment() {
    private var _fragmentReportKasStatusKamarBinding: FragmentReportKasStatusKamarBinding? = null
    private val  binding get() = _fragmentReportKasStatusKamarBinding!!
    private lateinit var reportViewModel: ReportViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentReportKasStatusKamarBinding = FragmentReportKasStatusKamarBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        reportViewModel.statusKas.observe(viewLifecycleOwner, {data ->
            if(data.dataStatusKas != null) {
                binding.tvCinPaid.text = data.dataStatusKas.jumlahCheckinSudahBayar.toString()
                binding.tvJamPaid.text = data.dataStatusKas.jumlahJamSudahBayar.toString()
                binding.tvGuestPaid.text = data.dataStatusKas.jumlahTamuSudahBayar.toString()
                binding.tvCinPiutang.text = data.dataStatusKas.jumlahCheckinPiutang.toString()
                binding.tvJamPiutang.text = data.dataStatusKas.jumlahJamPiutang.toString()
                binding.tvGuestPiutang.text = data.dataStatusKas.jumlahTamuPiutang.toString()
            }
        })
    }
}