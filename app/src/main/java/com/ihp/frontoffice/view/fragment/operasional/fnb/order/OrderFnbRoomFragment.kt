package com.ihp.frontoffice.view.fragment.operasional.fnb.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.entity.RoomOrder
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.databinding.FragmentOrderFnbRoomBinding
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.events.GlobalBus
import com.ihp.frontoffice.view.fragment.operasional.fnb.FnbConfirmFragment
import com.ihp.frontoffice.viewmodel.OtherViewModel
import es.dmoral.toasty.Toasty
import org.greenrobot.eventbus.Subscribe


class OrderFnbRoomFragment : Fragment() {

    private var _binding: FragmentOrderFnbRoomBinding? = null
    private val binding get() = _binding!!

    private lateinit var BASE_URL: String
    private lateinit var USER_FO: User

    private lateinit var otherViewModel: OtherViewModel
    val fnbPagingAdapter = FnbPagingAdapter()

    var category: String = ""
    var categoryName: String = ""
    var search: String = ""

    private lateinit var rvOrderDialog: RecyclerView
    private lateinit var btnSendOrder: AppCompatButton
    private var dialogAdapter = FnbInputOrderAdapter()

    var orderItem = ArrayList<DataBusEvent.OrderModel>()

    lateinit var roomOrder: RoomOrder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentOrderFnbRoomBinding.inflate(inflater, container, false)
        roomOrder = (arguments?.getSerializable(ARG_PARAM1) as RoomOrder)
        return binding.root
     }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BASE_URL = (requireActivity().applicationContext as MyApp).baseUrl
        USER_FO = (requireActivity().applicationContext as MyApp).userFo
        otherViewModel = ViewModelProvider(requireActivity()).get(OtherViewModel::class.java)
        binding.searchFnb.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    search = ""
                }else{
                    search = newText
                }
                getData()
                return true
            }
        })

        binding.btnCategoryFood.setOnClickListener {
            category = "1"
            categoryName = "FOOD"
            getData()
        }
        binding.btnCategoryDrink.setOnClickListener {
            category = "0"
            categoryName = "DRINK"
            getData()
        }
        binding.btnCategoryCigarette.setOnClickListener {
            category = "3"
            categoryName = "CIGARETTE"
            getData()
        }
        binding.btnCategorySnack.setOnClickListener {
            category = "16"
            categoryName = "SNACK"
            getData()
        }
        binding.btnCategoryPackage.setOnClickListener {
            category = "25"
            categoryName = "PAKET"
            getData()
        }

        binding.btnChooseCategory.setOnClickListener {
            category = ""
            categoryName = ""
            getData()
        }

        getData()

        with(binding.rvFnb){
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = fnbPagingAdapter
            setHasFixedSize(true)
        }

        binding.fabOrder.setOnClickListener {

            val builder: AlertDialog.Builder

            builder = AlertDialog.Builder(requireActivity())
            val inflater = requireActivity().layoutInflater

            val viewDialog = inflater.inflate(R.layout.dialog_order, null)
            builder.setView(viewDialog)
            builder.setCancelable(true)

            val alert = builder.create()

            val lp = WindowManager.LayoutParams()
            lp.copyFrom(alert.getWindow()?.getAttributes())
            lp.height = 400; // Set height here
            alert.getWindow()?.setAttributes(lp)

//            val dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_order, null)
            rvOrderDialog = viewDialog.findViewById(R.id.rv_item_order)
            rvOrderDialog.layoutManager = LinearLayoutManager(requireActivity())
            rvOrderDialog.setHasFixedSize(true)
            rvOrderDialog.adapter = dialogAdapter

            btnSendOrder = viewDialog.findViewById(R.id.btn_send_order)

            btnSendOrder.setOnClickListener {
                if(orderItem.isEmpty()){
                    Toasty.warning(requireActivity(), "Order Kosong", Toasty.LENGTH_SHORT, true).show()
                }else{
                    otherViewModel.sendOrder(BASE_URL, USER_FO.userId, roomOrder.checkinRoom.roomCode, roomOrder.checkinRoom.roomRcp, roomOrder.checkinRoom.roomType, roomOrder.checkinRoom.roomCheckinDuration.toString(), orderItem).observe(viewLifecycleOwner, {response ->
                        if(response.isLoading == true){
                            btnSendOrder.isEnabled = false
                        }else{
                            btnSendOrder.isEnabled = true
                            if(response.state == true){
                                orderItem.clear()
                                dialogAdapter.setData(orderItem)
                                fnbPagingAdapter.insertAddItem(orderItem)
                                alert.dismiss()
                            }
                            Toasty.info(requireActivity(), response.message, Toasty.LENGTH_LONG, true).show()
                        }
                    })
                }
            }
            alert.show()
        }
    }

    private fun getData(){
        if(category != ""){
            binding.btnChooseCategory.visibility = View.VISIBLE
            binding.btnChooseCategory.text = categoryName
        }else{
            binding.btnChooseCategory.visibility = View.GONE
        }
        otherViewModel.fnbPaging(BASE_URL, category, search).observe(requireActivity(), {data->
            fnbPagingAdapter.submitData( lifecycle,data)
        })
    }

    override fun onStart() {
        super.onStart()
        GlobalBus.getBus().register(this)
    }

    override fun onStop() {
        super.onStop()
        GlobalBus.getBus().unregister(this)
    }


    @Subscribe
    fun addOrder(addedItem: DataBusEvent.OrderModel){
        Log.d("DEBUG", "apakah sekali")
        val filter = orderItem.find { it.inventoryCode == addedItem.inventoryCode }
        if(filter == null){
            orderItem.add(addedItem)
        }

        dialogAdapter.setData(orderItem)
        if(orderItem.isEmpty()){
            binding.fabOrder.visibility = View.GONE
        }else{
            binding.fabOrder.visibility = View.VISIBLE
        }

        fnbPagingAdapter.insertAddItem(orderItem)
    }

    @Subscribe
    fun customOrder(newItem: ArrayList<DataBusEvent.OrderModel>){
        orderItem = newItem
        dialogAdapter.setData(orderItem)
        fnbPagingAdapter.insertAddItem(orderItem)
    }

    companion object{
        private val ARG_PARAM1 = "ROOM_ORDER"
    }
    fun newInstance(roomOrder: RoomOrder?): OrderFnbRoomFragment {
        val fragment = OrderFnbRoomFragment()
        val args = Bundle()
        args.putSerializable(ARG_PARAM1, roomOrder)
        fragment.arguments = args
        return fragment
    }
}