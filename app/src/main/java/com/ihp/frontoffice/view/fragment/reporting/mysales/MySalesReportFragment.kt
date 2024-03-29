package com.ihp.frontoffice.view.fragment.reporting.mysales

import android.R
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
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
import com.ihp.frontoffice.data.remote.respons.DataItemSales
import com.ihp.frontoffice.data.remote.respons.MySalesResponse
import com.ihp.frontoffice.databinding.FragmentMySalesReportBinding
import com.ihp.frontoffice.helper.utils
import com.ihp.frontoffice.view.fragment.reporting.ReportViewModel
import es.dmoral.toasty.Toasty


class MySalesReportFragment : Fragment() {

    private var _binding: FragmentMySalesReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var reportViewModel: ReportViewModel
    var kodePage = 0
    private var total = 0L
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

        binding.btnDetail.setOnClickListener {
            val toSalesItemsNavigation = MySalesReportParentFragmentDirections.actionMySalesReportParentFragmentToItemSalesFragment()
            Navigation.findNavController(it).navigate(toSalesItemsNavigation)
        }

        /*
        LinearLayout chartHolder = (LinearLayout) getActivity().findViewById(R.id.short_performance_chart);
        BarChart mChart = (BarChart) initializeBarChart(); //just populating data, etc
        chartHolder.addView(mChart);
        // Due to the fact that we are in a fragment we have to reset the height
        mChart.getLayoutParams().height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mChart.invalidate();
        */

        binding.barChart.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.barChart.invalidate();
    }

    override fun onResume() {
        super.onResume()
        kodePage = arguments?.getString(KODE_PAGE).toString().toInt()

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
            insertData(data)
        })

        reportViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    private fun salesWeekly() {
        reportViewModel.salesWeekly.observe(viewLifecycleOwner, {data ->
            insertData(data)
        })

        reportViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    private fun salesToday() {
        reportViewModel.salesToday.observe(viewLifecycleOwner, {data ->
            insertData(data)
        })

        reportViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
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
        xAxis.textSize = 12f
        xAxis.mLabelRotatedHeight = 35
        xAxis.textColor=Color.WHITE

        val leftAxis: YAxis = barChart.getAxisLeft()
        leftAxis.setLabelCount(8, false)
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.maxWidth
        leftAxis.spaceBottom = 12f
        leftAxis.axisMinimum = 0f
        leftAxis.textColor = Color.WHITE

        barChart.setDrawValueAboveBar(false)
    }

    private fun setDataToLineChart(dataItemSales: List<DataItemSales>){

        val entries: ArrayList<BarEntry> =  ArrayList()
        dataList = dataItemSales as ArrayList<DataItemSales>

        for (i in dataItemSales.indices){
            entries.add(BarEntry(i.toFloat(), dataItemSales[i].total.toFloat()))
        }

        val startColor2 = ContextCompat.getColor(requireActivity(), R.color.holo_blue_light)
        val startColor3 = ContextCompat.getColor(requireActivity(), R.color.holo_orange_light)
        val startColor4 = ContextCompat.getColor(requireActivity(), R.color.holo_green_light)
        val startColor5 = ContextCompat.getColor(requireActivity(), R.color.holo_red_light)

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setDrawValues(true)
        barDataSet.valueTextColor=Color.WHITE
        barDataSet.valueTextSize= 14f
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

    private fun insertData(data: MySalesResponse){
        total = 0L
        if (data.state == true){
            if (data.data != null){
                val ranges = 0..data.length-1
                for (i in ranges){
                    total += data.data[i].total
                }
                binding.tvTotal.setText(utils.getCurrency(total))
                setDataToLineChart(data.data)
                initLineChart()
                binding.llNominal.visibility = View.VISIBLE
                binding.frameLayout2.visibility = View.VISIBLE
                binding.ltEmpty.visibility = View.GONE
            } else{
                binding.frameLayout2.visibility = View.GONE
                binding.llNominal.visibility = View.GONE
                binding.ltEmpty.visibility = View.VISIBLE
                binding.ltEmpty.setAnimation("emptybox.json")
                Toasty.warning(requireActivity(),  data.message, Toast.LENGTH_SHORT).show()
                binding.ltEmpty.playAnimation()
            }
        } else{
            binding.frameLayout2.visibility = View.GONE
            binding.llNominal.visibility =  View.GONE
            binding.ltEmpty.visibility = View.VISIBLE
            binding.ltEmpty.setAnimation("erroranimation.json")
            Toasty.error(requireActivity(),  data.message, Toast.LENGTH_SHORT).show()
            binding.ltEmpty.playAnimation()
        }
     }

    private fun showLoading(isLoading:  Boolean){
        if (isLoading == true){
            binding.pbLoading.visibility = View.VISIBLE
        } else{
            binding.pbLoading.visibility = View.GONE
        }
    }

    companion object {
        const val KODE_PAGE = "kode"
    }
}