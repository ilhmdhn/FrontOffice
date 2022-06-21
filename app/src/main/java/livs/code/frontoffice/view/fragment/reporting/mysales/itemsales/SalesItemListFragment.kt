package livs.code.frontoffice.view.fragment.reporting.mysales.itemsales

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.dmoral.toasty.Toasty
import livs.code.frontoffice.data.remote.respons.SaleItemList
import livs.code.frontoffice.data.remote.respons.SalesItemListResponse
import livs.code.frontoffice.databinding.FragmentSalesItemListBinding
import livs.code.frontoffice.view.fragment.reporting.ReportViewModel
import java.util.*

class SalesItemListFragment : Fragment() {

    private var _binding: FragmentSalesItemListBinding? = null
    private val binding get() = _binding!!
    private lateinit var reportViewModel: ReportViewModel
    private var kodePage = 0
    private val salesItemAdapter = SalesItemAdapter()
    private lateinit var listItem : MutableList<SaleItemList>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentSalesItemListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
    }
    override fun onResume() {
        super.onResume()

        kodePage = arguments?.getString(KODE_PAGE).toString().toInt()

        if (kodePage == 1){
            reportViewModel.saleItemToday.observe(viewLifecycleOwner, {data ->
                showData(data)
            })
        }else if (kodePage == 2){
            reportViewModel.saleItemWeekly.observe(viewLifecycleOwner, {data ->
                showData(data)
            })
        }else if (kodePage == 3){
            reportViewModel.saleItemMonthly.observe(viewLifecycleOwner, {data ->
                showData(data)
            })
        }
    }


    private fun showData(dataSales: SalesItemListResponse){
        when(dataSales.state){
            true -> {
                if (dataSales.data != null){

                    listItem = dataSales.data
                    var click = 0
                    binding.hdItemName.setOnClickListener {
                        click++
                        if (click% 2==0){
                            listItem.sortBy { it.namaItem }
                        } else{
                            listItem.sortByDescending { it.namaItem }
                        }
                        salesItemAdapter.setData(listItem, kodePage)
                    }

                    binding.hdJumlah.setOnClickListener {
                        click++
                        if (click% 2==0){
                            listItem.sortWith(compareBy<SaleItemList>{it.jumlah}.thenBy { it.namaItem })
                        } else{
                            listItem.sortWith(compareBy<SaleItemList>{it.jumlah}.thenBy { it.namaItem })
                            listItem.reverse()
                        }
                        salesItemAdapter.setData(listItem, kodePage)
                    }
                    binding.hdPrice.setOnClickListener {
                        click++
                        if (click% 2==0){
                            listItem.sortWith(compareBy<SaleItemList>{it.total}.thenBy{it.namaItem})
                        } else{
                            listItem.sortWith(compareBy<SaleItemList>{it.total}.thenBy{it.namaItem})
                            listItem.reverse()
                        }
                        salesItemAdapter.setData(listItem, kodePage)
                    }

                    salesItemAdapter.setData(listItem, kodePage)
                    binding.rvSalesItem.visibility = View.VISIBLE
                    binding.ltEmpty.visibility = View.GONE
                } else{
                    binding.rvSalesItem.visibility = View.GONE
                    binding.ltEmpty.visibility = View.VISIBLE
                    Toasty.warning(requireActivity(),  dataSales.message, Toast.LENGTH_SHORT).show()
                    binding.ltEmpty.setAnimation("emptybox.json")
                    binding.ltEmpty.playAnimation()
                }
            }
            false -> {
                binding.rvSalesItem.visibility = View.GONE
                binding.ltEmpty.visibility = View.VISIBLE
                Toasty.error(requireActivity(),  dataSales.message, Toast.LENGTH_SHORT).show()
                binding.ltEmpty.setAnimation("erroranimation.json")
                binding.ltEmpty.playAnimation()
            }
        }

        with(binding.rvSalesItem){
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = salesItemAdapter
        }
    }

    companion object{
        const val KODE_PAGE =  "kode"
    }
}