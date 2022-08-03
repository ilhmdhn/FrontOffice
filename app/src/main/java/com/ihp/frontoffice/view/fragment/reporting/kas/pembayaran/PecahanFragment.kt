package com.ihp.frontoffice.view.fragment.reporting.kas.pembayaran

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
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.data.remote.respons.PecahanUang
import com.ihp.frontoffice.databinding.FragmentPecahanBinding
import com.ihp.frontoffice.helper.utils
import com.ihp.frontoffice.view.fragment.reporting.ReportViewModel

class PecahanFragment : Fragment() {

    private var _binding: FragmentPecahanBinding? = null
    private lateinit var reportViewModel: ReportViewModel
    private val binding get() = _binding!!
    private var tanggal = ""
    private var shift = ""
    private var baseUrl = ""
    var seratusRibu = 0L
    var limaPuluhRibu = 0L
    var duaPuluhRibu = 0L
    var sepuluhRibu = 0L
    var limaRibu = 0L
    var duaRibu = 0L
    var seribu = 0L
    var limaRatus = 0L
    var duaRatus = 0L
    var seratus = 0L
    var limaPuluh = 0L
    var duaLima = 0L
    var totalCash = 0L
    var jumlahPecahan = 0L
    var pecahanUang = PecahanUang()
    var selisih = 0L

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
        binding.tvTanggal.setText("Tanggal: " + tanggal)
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
        }

        binding.btnUpdate.setOnClickListener {
            updateCashDetail()
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
                binding.btnUpdate.visibility = View.GONE
                binding.btnSubmit.visibility = View.GONE
                Toasty.error(requireActivity(), data.message.toString(), Toast.LENGTH_SHORT).show()
            }
            hitungPecahan()
        })
    }

    private fun updateCashDetail() {
        hitungPecahan()
        if (selisih > 1000 || selisih < 0){
            Toasty.warning(requireActivity(), "Jumlah uang tidak boleh melebihi 1000 dan kurang dari 0", Toasty.LENGTH_SHORT, true).show()
            return
        }
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
        if (selisih > 1000 || selisih < 0){
            Toasty.warning(requireActivity(), "Jumlah uang tidak boleh melebihi 1000 dan kurang dari 0", Toasty.LENGTH_SHORT, true).show()
            return
        }
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
            seratusRibu = binding.etSeratusRibu.text.toString().toLong() * 100000
        }

        if (binding.etLimaPuluhRibu.text.isNullOrEmpty()){
            limaPuluhRibu = 0
            binding.etLimaPuluhRibu.setText("0")
        } else{
            limaPuluhRibu = binding.etLimaPuluhRibu.text.toString().toLong() * 50000
        }

        if (binding.etDuaPuluhRibu.text.isNullOrEmpty()){
            duaPuluhRibu = 0
            binding.etDuaPuluhRibu.setText("0")
        } else{
            duaPuluhRibu = binding.etDuaPuluhRibu.text.toString().toLong() * 20000
        }

        if (binding.etSepuluhRibu.text.isNullOrEmpty()){
            sepuluhRibu = 0
            binding.etSepuluhRibu.setText("0")
        } else{
            sepuluhRibu = binding.etSepuluhRibu.text.toString().toLong() * 10000
        }

        if (binding.etLimaRibu.text.isNullOrEmpty()){
            limaRibu = 0
            binding.etLimaRibu.setText("0")
        } else{
            limaRibu = binding.etLimaRibu.text.toString().toLong() * 5000
        }

        if (binding.etDuaRibu.text.isNullOrEmpty()){
            duaRibu = 0
            binding.etDuaRibu.setText("0")
        } else{
            duaRibu = binding.etDuaRibu.text.toString().toLong() * 2000
        }

        if (binding.etSeribu.text.isNullOrEmpty()){
            seribu = 0
            binding.etSeribu.setText("0")
        } else{
            seribu = binding.etSeribu.text.toString().toLong() * 1000
        }

        if (binding.etLimaRatus.text.isNullOrEmpty()){
            limaRatus = 0
            binding.etLimaRatus.setText("0")
        } else{
            limaRatus = binding.etLimaRatus.text.toString().toLong() * 500
        }

        if (binding.etDuaRatus.text.isNullOrEmpty()){
            duaRatus = 0
            binding.etDuaRatus.setText("0")
        } else{
            duaRatus = binding.etDuaRatus.text.toString().toLong() * 200
        }

        if (binding.etSeratus.text.isNullOrEmpty()){
            seratus = 0
            binding.etSeratus.setText("0")
        } else{
            seratus = binding.etSeratus.text.toString().toLong() * 100
        }

        if (binding.etLimaPuluh.text.isNullOrEmpty()){
            limaPuluh = 0
            binding.etLimaPuluh.setText("0")
        } else{
            limaPuluh = binding.etLimaPuluh.text.toString().toLong() * 50
        }

        if (binding.etDuaLima.text.isNullOrEmpty()){
            duaLima = 0
            binding.etDuaLima.setText("0")
        } else{
            duaLima = binding.etDuaLima.text.toString().toLong() * 25
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
        binding.tvSelisih.text = utils.getCurrency( jumlahPecahan - totalCash)
        selisih = jumlahPecahan - totalCash
    }


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
