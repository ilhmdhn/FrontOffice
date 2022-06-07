package livs.code.frontoffice.view.fragment.reporting.mysales

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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
    private var totalTax = 0L
    private var totalService = 0L
    private var totalDiscount = 0L
    private var totalAll = 0L
    private lateinit var barChart: BarChart
    lateinit var dataList: ArrayList<DataItemSales>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMySalesReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        barChart = binding.barChart
    }

    override fun onResume() {
        super.onResume()
        kodePage = arguments?.getString(KODE_PAGE).toString().toInt()

        totalPenjualan = 0L
        totalTax = 0L
        totalService = 0L
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
                    totalTax += data.data[i].taxPenjualan
                    totalService += data.data[i].servicePenjualan
                    totalDiscount += data.data[i].discountPenjualan
                    totalAll += data.data[i].totalPenjualan
                }
                binding.tvNominal.setText(utils.getCurrency(totalPenjualan))
                binding.tvTax.setText(utils.getCurrency(totalTax))
                binding.tvService.setText(utils.getCurrency(totalService))
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
                    totalTax += data.data[i].taxPenjualan
                    totalService += data.data[i].servicePenjualan
                    totalDiscount += data.data[i].discountPenjualan
                    totalAll += data.data[i].totalPenjualan
                }
                binding.tvNominal.setText(utils.getCurrency(totalPenjualan))
                binding.tvTax.setText(utils.getCurrency(totalTax))
                binding.tvService.setText(utils.getCurrency(totalService))
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
                binding.tvTax.setText(utils.getCurrency(data.data[0].taxPenjualan))
                binding.tvService.setText(utils.getCurrency(data.data[0].servicePenjualan))
                binding.tvDiscount.setText(utils.getCurrency(data.data[0].discountPenjualan))
                binding.tvFinalCharge.setText(utils.getCurrency(data.data[0].totalPenjualan))
                setDataToLineChart(data.data)
                initLineChart()
            }
        })
    }

    private fun initLineChart(){
        //hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false


        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(3000)

        // to draw label on xAxis
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        val leftAxis: YAxis = barChart.getAxisLeft()
        leftAxis.setLabelCount(8, false)
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.maxWidth
        leftAxis.axisMinimum = 0f

        barChart.setDrawValueAboveBar(false)
    }

    private fun setDataToLineChart(dataItemSales: List<DataItemSales>){

        val entries: ArrayList<BarEntry> =  ArrayList()
        dataList = dataItemSales as ArrayList<DataItemSales>

        for (i in dataItemSales.indices){
            entries.add(BarEntry(i.toFloat(), dataItemSales[i].totalPenjualan.toFloat()))
        }

        val startColor2 = ContextCompat.getColor(requireActivity(), R.color.holo_blue_light)
        val startColor3 = ContextCompat.getColor(requireActivity(), R.color.holo_orange_light)
        val startColor4 = ContextCompat.getColor(requireActivity(), R.color.holo_green_light)
        val startColor5 = ContextCompat.getColor(requireActivity(), R.color.holo_red_light)

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setDrawValues(true)
        Log.d("panjang bar", barDataSet.entryCount.toString())
        barDataSet.setColors(startColor2,
                            startColor3,
                            startColor4,
                            startColor5)


        val data = BarData(barDataSet)

        barChart.data = data

        barChart.invalidate()

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