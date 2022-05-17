package livs.code.frontoffice.view.fragment.reporting.kas.pembayaran

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import livs.code.frontoffice.R
import livs.code.frontoffice.databinding.FragmentPecahanBinding

class PecahanFragment : Fragment() {

    private var _binding: FragmentPecahanBinding? = null
    private val binding get() = _binding!!
    private var tanggal = ""
    private var shift = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentPecahanBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tanggal = PecahanFragmentArgs.fromBundle(arguments as Bundle).tanggal
        shift = PecahanFragmentArgs.fromBundle(arguments as Bundle).shift

        binding.tvTanggal.text = tanggal
        binding.tvShift.text = shift
    }

}