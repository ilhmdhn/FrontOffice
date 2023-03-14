package com.ihp.frontoffice.view.fragment.reporting.cancelation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.dmoral.toasty.Toasty
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.databinding.FragmentCancelReportBinding
import com.ihp.frontoffice.view.fragment.reporting.ReportViewModel

class CancelReportFragment : Fragment() {

    private var _binding:  FragmentCancelReportBinding?=null
    private val binding get() = _binding!!
    private lateinit var reportViewModel: ReportViewModel
    private var url = ""
    private val cancelItemsReportAdapter = CancelItemsReportAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentCancelReportBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = (requireActivity().applicationContext as MyApp).baseUrl
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)

        binding.lySwipe.setOnRefreshListener {
            binding.lySwipe.isRefreshing = false
            getData()
        }

        getData()
    }

    private fun getData(){
        reportViewModel.getCancelItems(url).observe(viewLifecycleOwner, {data ->
            if (data.state == false){
                Toasty.error(requireActivity(), data.message.toString(), Toast.LENGTH_SHORT, true).show()
            }else{
                if (data.data.isNullOrEmpty()){
                    Toasty.warning(requireActivity(), "Data Kosong", Toasty.LENGTH_SHORT, true).show()
                } else{
                    cancelItemsReportAdapter.setData(data.data)
                    with(binding.rvCancelItems){
                        layoutManager = LinearLayoutManager(requireActivity())
                        setHasFixedSize(true)
                        adapter = cancelItemsReportAdapter
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}