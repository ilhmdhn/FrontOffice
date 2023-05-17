package com.ihp.frontoffice.view.fragment.operasional.fnb.orderroomtransfer

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.entity.RoomOrder
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.data.model.CancelOrderModel
import com.ihp.frontoffice.databinding.FragmentOrderRoomTransferBinding
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.events.GlobalBus
import com.ihp.frontoffice.helper.UserAuthRole
import com.ihp.frontoffice.viewmodel.OtherViewModel
import es.dmoral.toasty.Toasty
import org.greenrobot.eventbus.Subscribe;

class OrderRoomTransferFragment : Fragment() {

    private var _binding: FragmentOrderRoomTransferBinding? =null
    private val binding get() = _binding!!

    private lateinit var BASE_URL: String
    private lateinit var USER_FO: User

    private lateinit var otherViewModel: OtherViewModel
    lateinit var roomOrder: RoomOrder

    val orderOldRoomAdapter = OrderOldRoomAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderRoomTransferBinding.inflate(inflater, container, false)
        roomOrder = (arguments?.getSerializable(ARG_PARAM1)) as RoomOrder
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BASE_URL = (requireActivity().applicationContext as MyApp).baseUrl
        USER_FO = (requireActivity().applicationContext as MyApp).userFo

        otherViewModel = ViewModelProvider(requireActivity()).get(OtherViewModel::class.java)

        with(binding.rvOrderRoomOld){
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = orderOldRoomAdapter
            setHasFixedSize(true)
        }
        
        getData()
        
        binding.swipe.setOnRefreshListener { 
            getData()
            binding.swipe.isRefreshing = false
        }
    }

    private fun getData(){
        isLoading(true)
        otherViewModel.getOrderRoomOld(BASE_URL, roomOrder.checkinRoom.invoice).observe(requireActivity(), {response->
            if(response.state == true){
                orderOldRoomAdapter.setData(response.data)
            }else{
                Toasty.error(requireActivity(), response.message, Toasty.LENGTH_LONG).show()
            }
            isLoading(false)
        })
    }



//    @SuppressLint("SetTextI18n")
    @Subscribe
    fun cancelOrder(data: DataBusEvent.cancelOldOrder){
        val dialog = MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme)
        val dialogView = layoutInflater.inflate(R.layout.dialog_cancel_order_inventory, null)
        dialog.setView(dialogView)
        val etUser = dialogView.findViewById<EditText>(R.id.input_username_otorisasi)
        val etPassword = dialogView.findViewById<EditText>(R.id.input_password_otorisasi)
        val etInventoryName = dialogView.findViewById<TextInputLayout>(R.id.inventory_name_dialog)
        val etInventoryCode = dialogView.findViewById<TextInputLayout>(R.id.inventory_code_dialog)
        val etInventoryQty = dialogView.findViewById<TextInputLayout>(R.id.inventory_qty_dialog)

        etInventoryName.isEnabled = false
        etInventoryCode.isEnabled = false
        etInventoryQty.isEnabled = false

        etInventoryName.editText?.setText(data.itemName)
        etInventoryCode.editText?.setText("${data.inventoryCode} | ${data.soCode}")
        etInventoryQty.editText?.setText(data.orderQty.toString())

        dialog.setPositiveButton("Submit", DialogInterface.OnClickListener { dialog, which ->
            otherViewModel.userLogin(BASE_URL, etUser.text.toString(), etPassword.text.toString()).observe(viewLifecycleOwner, {response->
                isLoading(true)
                if (response.isOkay){
                    if(UserAuthRole.isAllowCancelOrder(response.user)){
                        val cancelListtemp = ArrayList<CancelOrderModel>()
                        cancelListtemp.add(CancelOrderModel(
                                data.inventoryCode,
                                data.itemName,
                                data.orderCode,
                                data.orderQty.toString(),
                                data.soCode
                        ))
                        val cancelList: List<CancelOrderModel>
                        cancelList = cancelListtemp
                        otherViewModel.cancelOld(BASE_URL, USER_FO.userId, roomOrder.checkinRoom.roomCode, cancelList)

                    }else{
                        Toasty.info(requireActivity(), "User tidak memiliki akses ini", Toasty.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }else{
                    Toasty.error(requireActivity(), response.message.toString(), Toasty.LENGTH_LONG).show()
                    dialog.dismiss()
                }
                getData()
            })
        })

        dialog.show()
    }
    private fun isLoading(state: Boolean){
        if(state){
            binding.pbLoading.visibility = View.VISIBLE
            binding.rvOrderRoomOld.visibility = View.GONE
        }else{
            binding.pbLoading.visibility = View.GONE
            binding.rvOrderRoomOld.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalBus.getBus().register(this)
    }

    override fun onPause() {
        super.onPause()
        GlobalBus.getBus().unregister(this)
    }
    fun newInstance(roomOrder: RoomOrder?): OrderRoomTransferFragment {
        val fragment = OrderRoomTransferFragment()
        val args = Bundle()
        args.putSerializable(ARG_PARAM1, roomOrder)
        fragment.arguments = args
        return fragment
    }
    companion object{
        private val ARG_PARAM1 = "ROOM_ORDER"
    }
}