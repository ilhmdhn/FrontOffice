package livs.code.frontoffice.view.fragment.reporting.kas.pembayaran

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.dmoral.toasty.Toasty
import livs.code.frontoffice.MyApp
import livs.code.frontoffice.data.remote.respons.PecahanUang
import livs.code.frontoffice.databinding.FragmentPecahanBinding
import livs.code.frontoffice.helper.utils
import livs.code.frontoffice.view.fragment.reporting.ReportViewModel

class PecahanFragment : Fragment() {

    private var _binding: FragmentPecahanBinding? = null
    private lateinit var reportViewModel: ReportViewModel
    private val binding get() = _binding!!
    private var tanggal = ""
    private var shift = ""
    private var baseUrl = ""
    var seratusRibu = 0
    var limaPuluhRibu = 0
    var duaPuluhRibu = 0
    var sepuluhRibu = 0
    var limaRibu = 0
    var duaRibu = 0
    var seribu = 0
    var limaRatus = 0
    var duaRatus = 0
    var seratus = 0
    var limaPuluh = 0
    var duaLima = 0
    var totalCash = 0
    var jumlahPecahan = 0
    var pecahanUang = PecahanUang()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentPecahanBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)

        tanggal = PecahanFragmentArgs.fromBundle(arguments as Bundle).tanggal
        shift = PecahanFragmentArgs.fromBundle(arguments as Bundle).shift
        totalCash = PecahanFragmentArgs.fromBundle(arguments as Bundle).totalTunai

        baseUrl =(requireActivity().applicationContext as MyApp).baseUrl

        binding.tvTotal.text = utils.getCurrency(totalCash)
        binding.tvTanggal.setText(tanggal)
        binding.tvShift.setText("Shift: $shift");

        getData()

        binding.etSeratusRibu.setOnFocusChangeListener { view, b ->
            hitungPecahan()
        }
        binding.etLimaPuluhRibu.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etDuaPuluhRibu.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etSepuluhRibu.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etLimaRibu.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etDuaRibu.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etSeribu.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etLimaRatus.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etDuaRatus.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etSeratus.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etLimaPuluh.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }
        binding.etDuaLima.setOnFocusChangeListener{view, b->
            hitungPecahan()
        }

        binding.btnSubmit.setOnClickListener {
            postCashDetail()
            getData()
        }

        binding.btnUpdate.setOnClickListener {
            updateCashDetail()
            getData()
        }
    }

    private fun getData(){
        reportViewModel.getCashDetail(baseUrl, tanggal, shift).observe(viewLifecycleOwner, {data ->
            if (data.state == true && data.pecahanUangData != null){
                binding.etSeratusRibu.setText(data.pecahanUangData.seratusRibu.toString())
                binding.etLimaPuluhRibu.setText(data.pecahanUangData.limaPuluhRibu.toString())
                binding.etDuaPuluhRibu.setText(data.pecahanUangData.duaPuluhRibu.toString())
                binding.etSepuluhRibu.setText(data.pecahanUangData.sepuluhRibu.toString())
                binding.etLimaRibu.setText(data.pecahanUangData.limaRibu.toString())
                binding.etDuaRibu.setText(data.pecahanUangData.duaRibu.toString())
                binding.etSeribu.setText(data.pecahanUangData.seribu.toString())
                binding.etLimaRatus.setText(data.pecahanUangData.limaRatus.toString())
                binding.etDuaRatus.setText(data.pecahanUangData.duaRatus.toString())
                binding.etSeratus.setText(data.pecahanUangData.seratus.toString())
                binding.etLimaPuluh.setText(data.pecahanUangData.limaPuluh.toString())
                binding.etDuaLima.setText(data.pecahanUangData.duaPuluhLima.toString())
                binding.btnSubmit.visibility = View.GONE
            } else if(data.state == true && data.pecahanUangData == null){
                binding.btnUpdate.visibility = View.GONE
            } else{
                Toasty.error(requireActivity(), data.message.toString(), Toast.LENGTH_SHORT).show()
            }
            hitungPecahan()
        })
    }

    private fun updateCashDetail() {
        hitungPecahan()
        reportViewModel.updateCashDetail(baseUrl, tanggal, shift, pecahanUang).observe(viewLifecycleOwner, {data ->
            if (data.state != true){
                Toasty.error(requireActivity(), data.message.toString(), Toast.LENGTH_SHORT).show()
            } else {
                Toasty.info(requireActivity(), data.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun postCashDetail() {
        hitungPecahan()
        reportViewModel.postCashDetail(baseUrl, tanggal, shift, pecahanUang).observe(viewLifecycleOwner, {data ->
            if (data.state != true){
                Toasty.error(requireActivity(), data.message.toString(), Toast.LENGTH_SHORT).show()
            } else {
                Toasty.info(requireActivity(), data.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun hitungPecahan(){

        if (binding.etSeratusRibu.text.isNullOrEmpty()){
            seratusRibu = 0
            binding.etSeratusRibu.setText("0")
        } else{
            seratusRibu = binding.etSeratusRibu.text.toString().toInt() * 100000
        }

        if (binding.etLimaPuluhRibu.text.isNullOrEmpty()){
            limaPuluhRibu = 0
            binding.etLimaPuluhRibu.setText("0")
        } else{
            limaPuluhRibu = binding.etLimaPuluhRibu.text.toString().toInt() * 50000
        }

        if (binding.etDuaPuluhRibu.text.isNullOrEmpty()){
            duaPuluhRibu = 0
            binding.etDuaPuluhRibu.setText("0")
        } else{
            duaPuluhRibu = binding.etDuaPuluhRibu.text.toString().toInt() * 20000
        }

        if (binding.etSepuluhRibu.text.isNullOrEmpty()){
            sepuluhRibu = 0
            binding.etSepuluhRibu.setText("0")
        } else{
            sepuluhRibu = binding.etSepuluhRibu.text.toString().toInt() * 10000
        }

        if (binding.etLimaRibu.text.isNullOrEmpty()){
            limaRibu = 0
            binding.etLimaRibu.setText("0")
        } else{
            limaRibu = binding.etLimaRibu.text.toString().toInt() * 5000
        }

        if (binding.etDuaRibu.text.isNullOrEmpty()){
            duaRibu = 0
            binding.etDuaRibu.setText("0")
        } else{
            duaRibu = binding.etDuaRibu.text.toString().toInt() * 2000
        }

        if (binding.etSeribu.text.isNullOrEmpty()){
            seribu = 0
            binding.etSeribu.setText("0")
        } else{
            seribu = binding.etSeribu.text.toString().toInt() * 1000
        }

        if (binding.etLimaRatus.text.isNullOrEmpty()){
            limaRatus = 0
            binding.etLimaRatus.setText("0")
        } else{
            limaRatus = binding.etLimaRatus.text.toString().toInt() * 500
        }

        if (binding.etDuaRatus.text.isNullOrEmpty()){
            duaRatus = 0
            binding.etDuaRatus.setText("0")
        } else{
            duaRatus = binding.etDuaRatus.text.toString().toInt() * 200
        }

        if (binding.etSeratus.text.isNullOrEmpty()){
            seratus = 0
            binding.etSeratus.setText("0")
        } else{
            seratus = binding.etSeratus.text.toString().toInt() * 100
        }

        if (binding.etLimaPuluh.text.isNullOrEmpty()){
            limaPuluh = 0
            binding.etLimaPuluh.setText("0")
        } else{
            limaPuluh = binding.etLimaPuluh.text.toString().toInt() * 50
        }

        if (binding.etDuaLima.text.isNullOrEmpty()){
            duaLima = 0
            binding.etDuaLima.setText("0")
        } else{
            duaLima = binding.etDuaLima.text.toString().toInt() * 25
        }

        jumlahPecahan =(seratusRibu +
                        limaPuluhRibu +
                        duaPuluhRibu +
                        sepuluhRibu +
                        limaRibu +
                        duaRibu +
                        seribu +
                        limaRatus +
                        duaRatus +
                        seratus +
                        limaPuluh +
                        duaLima)
        pecahanUang.seratus_ribu = binding.etSeratusRibu.text.toString().toInt()
        pecahanUang.lima_puluh_ribu = binding.etLimaPuluhRibu.text.toString().toInt()
        pecahanUang.dua_puluh_ribu = binding.etDuaPuluhRibu.text.toString().toInt()
        pecahanUang.sepuluh_ribu = binding.etSepuluhRibu.text.toString().toInt()
        pecahanUang.lima_ribu = binding.etLimaRibu.text.toString().toInt()
        pecahanUang.dua_ribu = binding.etDuaRibu.text.toString().toInt()
        pecahanUang.seribu  = binding.etSeribu.text.toString().toInt()
        pecahanUang.lima_ratus = binding.etLimaRatus.text.toString().toInt()
        pecahanUang.dua_ratus = binding.etDuaRatus.text.toString().toInt()
        pecahanUang.seratus = binding.etSeratus.text.toString().toInt()
        pecahanUang.lima_puluh = binding.etLimaPuluh.text.toString().toInt()
        pecahanUang.dua_lima = binding.etDuaLima.text.toString().toInt()

        binding.tvJumlahPecahan.text = utils.getCurrency(jumlahPecahan)
        binding.tvSelisih.text = utils.getCurrency(totalCash - jumlahPecahan)

    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}
