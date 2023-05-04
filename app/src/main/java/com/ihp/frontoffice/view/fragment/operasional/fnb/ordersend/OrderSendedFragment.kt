package com.ihp.frontoffice.view.fragment.operasional.fnb.ordersend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.data.entity.RoomOrder
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.data.remote.respons.OrderResponse
import com.ihp.frontoffice.databinding.FragmentOrderSendedBinding
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.events.GlobalBus
import com.ihp.frontoffice.viewmodel.OtherViewModel
import es.dmoral.toasty.Toasty
import org.greenrobot.eventbus.Subscribe


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

    @Subscribe
    fun cancelOrder(data: DataBusEvent.cancelOrder){
        Log.d("DEBUG", "asdasdsa")
        isLoading(true)
        otherViewModel.cancelOrder(BASE_URL, data.so, data.inventoryCode, data.qty, data.rcp, data.user, data.android).observe(viewLifecycleOwner, {response ->
            if(response.state==true){
                getData()
            }else{
                Toasty.error(requireActivity(), response.message, Toasty.LENGTH_LONG, true).show()
                getData()
            }
        })
    }

    fun getData(){
        isLoading(true)
        otherViewModel.getOrder(BASE_URL, roomOrder.checkinRoom.roomCode).observe(viewLifecycleOwner, {response ->
            setData(response)
            isLoading(false)
        })
    }

    private fun setData(response: OrderResponse){
        if(response.state == true){
            val dataFiltered = response.data.filter { it.orderState == "1" || it.orderState == "2" || it.orderState == "3"}
            val sortedFilter = dataFiltered.sortedBy { it.orderState }
            orderSendAdapter.setData(sortedFilter)
        }else{
            Toasty.warning(requireActivity(), response.message.toString(), Toasty.LENGTH_LONG).show()
        }
    }

    fun isLoading(loading: Boolean){
        if(loading){
            binding.rvListOrderSended.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE
        }else{
            binding.rvListOrderSended.visibility = View.VISIBLE
            binding.loading.visibility = View.GONE
        }
    }

//    override fun onStart() {
//        super.onStart()
//        GlobalBus.getBus().register(this)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        GlobalBus.getBus().unregister(this)
//    }

    override fun onResume() {
        super.onResume()
        getData()
        GlobalBus.getBus().register(this)
    }

    override fun onPause() {
        super.onPause()
        GlobalBus.getBus().unregister(this)
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