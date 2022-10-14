package com.ihp.frontoffice.view.fragment.setting

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.ihp.frontoffice.R
import com.ihp.frontoffice.databinding.FragmentSettingBinding
import com.ihp.frontoffice.events.EventsWrapper.TitleFragment
import com.ihp.frontoffice.events.GlobalBus
import es.dmoral.toasty.Toasty

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMainTitle()

        val context = requireActivity()
        val sharedPref = context.getSharedPreferences(getString(R.string.preference_print), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        binding.button.setOnClickListener {
            testPrint()
        }

        val printSetting = sharedPref.getInt(getString(R.string.preference_print), 2)

        when(printSetting){
            1 -> binding.rbPrintMobile.isChecked = true
            2 -> binding.rbPrintDesktop.isChecked = true
            3 -> binding.rbPrintBoth.isChecked = true
        }

        binding.rbPrint.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb_print_mobile -> {
                    editor.putInt(getString(R.string.preference_print), 1)
                    editor.apply()
                    Toasty.info(requireActivity(), "Android Print", Toasty.LENGTH_SHORT, true).show()
                }
                R.id.rb_print_desktop -> {
                    editor.putInt(getString(R.string.preference_print), 2)
                    editor.apply()
                    Toasty.info(requireActivity(), "Pos Lorong Print", Toasty.LENGTH_SHORT, true).show()
                }
                R.id.rb_print_both -> {
                    editor.putInt(getString(R.string.preference_print), 3)
                    editor.apply()
                    Toasty.info(requireActivity(), "Kedua Printer Aktif", Toasty.LENGTH_SHORT, true).show()
                }
            }
        }

    }

    private fun testPrint(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT))
        }
        else{
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }

        try {
            val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
            printer.printFormattedText(
//                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.ic_baseline_fastfood_24, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
                "[L]\n" +
                        "[C]================================\n"+
                        "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
                        "[L]\n" +
                        "[L]\n" +
                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
                        "[L]  + Size : S\n" +
                        "[L]\n" +
                        "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
                        "[L]  + Size : 57/58\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[R]TOTAL PRICE :[R]34.98e\n" +
                        "[R]TAX :[R]4.23e\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<font size='tall'>Customer :</font>\n" +
                        "[L]Raymond DUPONT\n" +
                        "[L]5 rue des girafes\n" +
                        "[L]31547 PERPETES\n" +
                        "[L]Tel : +33801201456\n" +
                        "[L]\n" +
                        "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                        "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>"
            )
        }catch (e: java.lang.Exception){
            Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
            Log.e("error", e.toString())
        }
    }

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            //granted
        }else{
            //deny
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }

    private fun setMainTitle() {
        GlobalBus
            .getBus()
            .post(TitleFragment("Setting"))
    }
}