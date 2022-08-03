package com.ihp.frontoffice.view.fragment.reporting.mysales.itemsales.detailsales

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.dmoral.toasty.Toasty
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.databinding.FragmentSaleperItemListBinding
import com.ihp.frontoffice.view.fragment.reporting.ReportViewModel

class SaleperItemListFragment : Fragment() {

    private var _binding: FragmentSaleperItemListBinding?=null
    private val binding get() = _binding!!
    var namaItem = ""
    var kodeWaktu = 0
    var namaWaktu = ""
    var url = ""
    var user = ""
    private val saleperItemAdapter = SaleperItemAdapter()
    private lateinit var reportViewModel: ReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentSaleperItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)

        namaItem = arguments?.getString(item_name).toString()
        kodeWaktu = arguments?.getString(waktu).toString().toInt()

        when(kodeWaktu){
            1 -> namaWaktu = "dialy"
            2 -> namaWaktu = "weekly"
            3 -> namaWaktu = "monthly"
        }

        url = (requireActivity().applicationContext as MyApp).baseUrl
        user = (requireActivity().applicationContext as MyApp).userFo.userId

        getData()
        binding.lySwipe.setOnRefreshListener {
            getData()
            binding.lySwipe.isRefreshing = false
        }
    }

    private fun getData(){
        reportViewModel.getSaleperItems(url, namaWaktu, namaItem, user).observe(viewLifecycleOwner, {data ->
            if (data.state == true){
                if (data.data.isNullOrEmpty()){
                    Toasty.warning(requireActivity(), data.message.toString(), Toasty.LENGTH_SHORT, true).show()
                } else{
                    saleperItemAdapter.setData(data.data)
                }
            }else{
                Toasty.error(requireActivity(), data.message.toString(), Toasty.LENGTH_LONG, true).show()
            }
        })

        with(binding.rvItem){
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = saleperItemAdapter
        }
    }

    companion object{
        const val item_name = "item_name"
        const val waktu = "waktu"
    }
}