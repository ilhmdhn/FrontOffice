package livs.code.frontoffice.view.fragment.reporting.kas

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.dmoral.toasty.Toasty
import livs.code.frontoffice.MyApp
import livs.code.frontoffice.R
import livs.code.frontoffice.databinding.FragmentStatusKasBinding
import livs.code.frontoffice.events.EventsWrapper.TitleFragment
import livs.code.frontoffice.events.GlobalBus
import livs.code.frontoffice.view.fragment.reporting.ReportViewModel
import java.text.SimpleDateFormat
import java.util.*


class StatusKasFragment : Fragment() {

    private var _binding: FragmentStatusKasBinding? = null
    private val binding get() = _binding!!
    var calendar = Calendar.getInstance()
    var levelUser: String? = null
    var BASE_URL: String? = ""
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
        setMainTitle()
        BASE_URL = (requireActivity().applicationContext as MyApp).baseUrl
        val levelUserArray = resources.getStringArray(R.array.level_user)
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


        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
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
                            dateSetListener,
                            // set DatePickerDialog to point to today's date when it loads up
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }
            })

        ArrayAdapter.createFromResource(requireActivity(), R.array.level_user, android.R.layout.simple_spinner_item).also { adapter ->
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
                shift = "shift satu"
            } else if (binding.rbShiftDua.isChecked){
                shift = "shift dua"
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
                    Toast.makeText(requireActivity(), "Mencari tanpa user Tanggal $date \nshift $shift", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(requireActivity(), "Mencari dengan user Tanggal $date \nshift $shift \nUser $user ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setDate() {
        val dateFormat = SimpleDateFormat("dd-MM-yyy", Locale.getDefault())
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

    private fun setMainTitle() {
        GlobalBus
            .getBus()
            .post(TitleFragment("Laporan Kas"))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}