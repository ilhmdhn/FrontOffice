package com.ihp.frontoffice.view.fragment.reporting.kas

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.databinding.FragmentStatusKasBinding
import com.ihp.frontoffice.events.EventsWrapper.TitleFragment
import com.ihp.frontoffice.events.GlobalBus
import com.ihp.frontoffice.helper.UserAuthRole
import com.ihp.frontoffice.view.fragment.reporting.ReportViewModel
import es.dmoral.toasty.Toasty
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.*


class StatusKasFragment : Fragment() {

    private var _binding: FragmentStatusKasBinding? = null
    private val binding get() = _binding!!
    var calendar = Calendar.getInstance()
    var levelUser: String? = null
    var BASE_URL: String? = ""
    var USERFO: User? = null
    private  lateinit var reportViewModel: ReportViewModel
    var listUser: MutableList<String> = ArrayList()

    var date: String = ""
    var shift: String = ""
    var user: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStatusKasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BASE_URL = (requireActivity().applicationContext as MyApp).baseUrl
        USERFO = (requireActivity().applicationContext as MyApp).userFo
        val levelUserArray = resources.getStringArray(com.ihp.frontoffice.R.array.level_user)
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        binding.spinnerUser.isEnabled = false
        binding.spinnerLevelUser.isEnabled = false
        binding.parentSpinner.isEnabled = false

        binding.cbByUser.setOnClickListener {
            if (binding.cbByUser.isChecked){
                binding.spinnerUser.isEnabled = true
                binding.spinnerLevelUser.isEnabled = true
                binding.parentSpinner.isEnabled = true
            } else {
                binding.spinnerUser.isEnabled = false
                binding.spinnerLevelUser.isEnabled = false
                binding.parentSpinner.isEnabled = false
                binding.spinnerUser.setText("")
            }
        }



//        val datePickerDialog = DatePickerDialog(requireActivity(), com.ihp.frontoffice.R.style.MyDatePickerDialogTheme,
//                { view, year, month, dayOfMonth ->
//                    // Implementasi ketika tanggal dipilih
//                }, year, month, dayOfMonth
//        )
//
        val dateSetListener = object : OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,dayOfMonth: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setDate()
            }
        }

        binding.ivDatePicker.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    activity?.let {
                        DatePickerDialog(
                            it,
                            com.ihp.frontoffice.R.style.LightCalendar,
                            dateSetListener,
                            // set DatePickerDialog to point to today's date when it loads up
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }
            })

        ArrayAdapter.createFromResource(requireActivity(), com.ihp.frontoffice.R.array.level_user, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerLevelUser.adapter = adapter
        }

        binding.spinnerLevelUser.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                levelUser = levelUserArray[p2]
                getUserList(BASE_URL.toString(), levelUser.toString(), requireActivity())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                levelUser = null
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (binding.rbShiftSatu.isChecked){
                shift = "1"
            } else if (binding.rbShiftDua.isChecked){
                shift = "2"
            } else {
                shift = ""
            }
            date = binding.tvValueDate.text.toString()
            user = binding.spinnerUser.text.toString()

            if (date ==  "Pilih Tanggal"){
                Toasty.error(requireActivity(), "Tanggal belum dipilih", Toast.LENGTH_SHORT, true).show()
            } else if (shift.isEmpty()){
                Toasty.error(requireActivity(), "Shift belum dipilih", Toast.LENGTH_SHORT, true).show()
            } else{
                if (user.isEmpty()){
                    val toDetailStatusKasFragment = StatusKasFragmentDirections.actionNavStatusKasFragmentToDetailReportKasFragment()
                    toDetailStatusKasFragment.tanggal = date
                    toDetailStatusKasFragment.shift = shift
                    toDetailStatusKasFragment.username= "-"
                    Navigation.findNavController(it).navigate(toDetailStatusKasFragment)
                } else{
                    val toDetailStatusKasFragment = StatusKasFragmentDirections.actionNavStatusKasFragmentToDetailReportKasFragment()
                    toDetailStatusKasFragment.tanggal = date
                    toDetailStatusKasFragment.shift = shift
                    toDetailStatusKasFragment.username= user
                    Navigation.findNavController(it).navigate(toDetailStatusKasFragment)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setMainTitle()
    }

    private fun setDate() {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val pickDate = sdf.format(calendar.time)

        val date = Date()

        val currentDate = sdf.format(date)
        val fixPicedDate = sdf.parse(pickDate)
        val fixCurrentDate = sdf.parse(currentDate)

        val selisihMs = fixCurrentDate.time - fixPicedDate.time
        val selisihHari = selisihMs / (1000 * 60 * 60 * 24)

        if(selisihHari>1){
            if(UserAuthRole.isAllowViewAllKas(USERFO)){
                binding.tvValueDate.setText(dateFormat.format(calendar.time))
                return
            }else{
                val calendarz = Calendar.getInstance()
                calendarz.time = date
                calendarz.add(Calendar.DAY_OF_MONTH, -1)

                binding.tvValueDate.setText(dateFormat.format(calendarz.time))
                return
            }
        }
        binding.tvValueDate.setText(dateFormat.format(calendar.time))
    }

    private fun getUserList(baseUrl: String,  levelUser: String, context: Context){
        reportViewModel.getUser(baseUrl, levelUser, context).observe(viewLifecycleOwner, {user ->
            listUser.clear()
            binding.spinnerUser.setText("")
            for (i in 0..user.size-1){
                listUser = (listUser + user[i].username.toString()) as MutableList<String>
            }
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listUser)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinnerUser.setAdapter(spinnerAdapter)
        })
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    @Subscribe
    private fun setMainTitle() {
        GlobalBus
            .getBus()
            .post(TitleFragment("Laporan Kas"))
    }

    override fun onStop() {
        super.onStop()
        GlobalBus.getBus().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}