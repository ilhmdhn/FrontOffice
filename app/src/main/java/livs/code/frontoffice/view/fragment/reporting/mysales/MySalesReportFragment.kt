package livs.code.frontoffice.view.fragment.reporting.mysales

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import livs.code.frontoffice.R
import livs.code.frontoffice.data.remote.respons.DataItemSales
import livs.code.frontoffice.databinding.FragmentMySalesReportBinding
import livs.code.frontoffice.helper.utils
import livs.code.frontoffice.view.fragment.reporting.ReportViewModel

class MySalesReportFragment : Fragment() {

    private var _binding: FragmentMySalesReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var reportViewModel: ReportViewModel
    var kodePage = 0
    private var totalPenjualan = 0L
    private var totalTaxService = 0L
    private var totalDiscount = 0L
    private var totalAll = 0L
    private lateinit var lineChart: LineChart
    lateinit var dataList: ArrayList<DataItemSales>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMySalesReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        lineChart = binding.lineChart
    }

    override fun onResume() {
        super.onResume()
        kodePage = arguments?.getString(KODE_PAGE).toString().toInt()

        totalPenjualan = 0L
        totalTaxService = 0L
        totalDiscount = 0L
        totalAll = 0L
        dataList = arrayListOf()

        if (kodePage == 1){
            salesToday()
        } else if(kodePage == 2){
            salesWeekly()
        } else if (kodePage == 3){
            salesMonthly()
        }
    }

    private fun salesMonthly() {
        reportViewModel.salesMonthly.observe(viewLifecycleOwner, {data ->
            if (data.state == true){
                val ranges = 0..data.length-1
                for (i in ranges){
                    totalPenjualan += data.data[i].chargePenjualan
                    totalTaxService += data.data[i].servicePenjualan + data.data[i].taxPenjualan
                    totalDiscount += data.data[i].discountPenjualan
                    totalAll += data.data[i].totalPenjualan
                }
                binding.tvNominal.setText(utils.getCurrency(totalPenjualan))
                binding.tvTaxService.setText(utils.getCurrency(totalTaxService))
                binding.tvDiscount.setText(utils.getCurrency(totalDiscount))
                binding.tvFinalCharge.setText(utils.getCurrency(totalAll))
                setDataToLineChart(data.data)
                initLineChart()
            }
        })
    }

    private fun salesWeekly() {
        reportViewModel.salesWeekly.observe(viewLifecycleOwner, {data ->
            if (data.state == true){
                val ranges = 0..data.length-1
                for (i in ranges){
                    totalPenjualan += data.data[i].chargePenjualan
                    totalTaxService += data.data[i].servicePenjualan + data.data[i].taxPenjualan
                    totalDiscount += data.data[i].discountPenjualan
                    totalAll += data.data[i].totalPenjualan
                }
                binding.tvNominal.setText(utils.getCurrency(totalPenjualan))
                binding.tvTaxService.setText(utils.getCurrency(totalTaxService))
                binding.tvDiscount.setText(utils.getCurrency(totalDiscount))
                binding.tvFinalCharge.setText(utils.getCurrency(totalAll))
                setDataToLineChart(data.data)
                initLineChart()
            }
        })
    }

    private fun salesToday() {
        reportViewModel.salesToday.observe(viewLifecycleOwner, {data ->
            if (data.state == true){
                binding.tvNominal.setText(utils.getCurrency(data.data[0].chargePenjualan))
                binding.tvTaxService.setText(utils.getCurrency(data.data[0].taxPenjualan + data.data[0].servicePenjualan))
                binding.tvDiscount.setText(utils.getCurrency(data.data[0].discountPenjualan))
                binding.tvFinalCharge.setText(utils.getCurrency(data.data[0].totalPenjualan))
                setDataToLineChart(data.data)
                initLineChart()
            }
        })
    }

    private fun initLineChart(){
        lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)

        lineChart.axisRight.isEnabled = false

        lineChart.legend.isEnabled = true

        lineChart.description.isEnabled = false

        lineChart.animateX(1000, Easing.EaseInSine)

        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
    }

    private fun setDataToLineChart(dataItemSales: List<DataItemSales>){

        val entries: ArrayList<Entry> =  ArrayList()
        dataList = dataItemSales as ArrayList<DataItemSales>

        for (i in dataItemSales.indices){
            entries.add(Entry(i.toFloat(), dataItemSales[i].totalPenjualan.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "")

        val data = LineData(lineDataSet)
        lineChart.data = data

        lineChart.invalidate()
    }

    inner class MyAxisFormatter: IndexAxisValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            var index = value.toInt()
            if (index < 0 ){
                index = 0
            }
            Log.d("indexvalue", index.toString())
            return if (index<dataList.size) {
                dataList[index].displayWaktu
            } else{
                ""
            }
        }
    }


    companion object {
        const val KODE_PAGE = "kode"
    }
}