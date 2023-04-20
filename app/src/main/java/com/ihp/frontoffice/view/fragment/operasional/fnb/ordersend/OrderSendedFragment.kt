package com.ihp.frontoffice.view.fragment.operasional.fnb.ordersend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.data.entity.RoomOrder
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.databinding.FragmentOrderSendedBinding
import com.ihp.frontoffice.viewmodel.OtherViewModel
import es.dmoral.toasty.Toasty


class OrderSendedFragment : Fragment() {

    private var _binding: FragmentOrderSendedBinding?= null
    private val binding get() = _binding!!

    private lateinit var otherViewModel: OtherViewModel

    private lateinit var BASE_URL: String
    private lateinit var USER_FO: User

    lateinit var roomOrder: RoomOrder

    private val orderSendAdapter = OrderSendedAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderSendedBinding.inflate(inflater, container, false)
        roomOrder = (arguments?.getSerializable(ARG_PARAM1) as RoomOrder)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BASE_URL = (requireActivity().applicationContext as MyApp).baseUrl
        USER_FO = (requireActivity().applicationContext as MyApp).userFo

        with(binding.rvListOrderSended){
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = orderSendAdapter
        }

        otherViewModel = ViewModelProvider(requireActivity()).get(OtherViewModel::class.java)
        getData()
    }

    fun getData(){
        otherViewModel.getOrder(BASE_URL, roomOrder.checkinRoom.roomCode).observe(viewLifecycleOwner, {response ->
            if(response.state == true){
                val dataFiltered = response.data.filter { it.orderState == "1" }
                orderSendAdapter.setData(response.data)
            }else{
                Toasty.warning(requireActivity(), response.message.toString(), Toasty.LENGTH_LONG).show()
            }
        })
    }

    companion object{
        private val ARG_PARAM1 = "ROOM_ORDER"
    }
    fun newInstance(roomOrder: RoomOrder?): OrderSendedFragment {
        val fragment = OrderSendedFragment()
        val args = Bundle()
        args.putSerializable(ARG_PARAM1, roomOrder)
        fragment.arguments = args
        return fragment
    }
}