package livs.code.frontoffice.view.fragment.reporting.mysales.itemsales.detailsales

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import livs.code.frontoffice.R
import livs.code.frontoffice.databinding.FragmentSaleperItemListBinding

class SaleperItemListFragment : Fragment() {

    private var _binding: FragmentSaleperItemListBinding?=null
    private val binding get() = _binding!!
    var namaItem = ""
    var kodeWaktu = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentSaleperItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        namaItem = arguments?.getString(item_name).toString()
        kodeWaktu = arguments?.getString(waktu).toString().toInt()

        binding.tvTest.text = namaItem + kodeWaktu.toString()

    }

    companion object{
        const val item_name = "item_name"
        const val waktu = "waktu"
    }
}