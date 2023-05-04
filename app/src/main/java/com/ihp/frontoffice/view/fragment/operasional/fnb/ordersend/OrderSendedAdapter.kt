package com.ihp.frontoffice.view.fragment.operasional.fnb.ordersend

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.DataOrderItem
import com.ihp.frontoffice.databinding.ListSendedOrderBinding
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.events.EventsWrapper
import com.ihp.frontoffice.events.GlobalBus

class OrderSendedAdapter: RecyclerView.Adapter<OrderSendedAdapter.ListViewHolder>() {

    private var listData = ArrayList<DataOrderItem>()

    fun setData(newListData: List<DataOrderItem>){
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = ListSendedOrderBinding.bind(itemView)

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(data: DataOrderItem){
            var qtyOrder = data.orderQuantity ?: 0
            var canceled = false
            with(binding){
                if(data.orderState == "1"){
                    linearLayout12.visibility = View.VISIBLE
                    btnEditOrderSubmit.visibility = View.VISIBLE
                    tvFnbName.text = data.orderInventoryNama
                    tvFnbName.setTextColor(Color.WHITE)
                    tvOrderNote.text = data.orderNotes

                    btnPlus.setOnClickListener {
                        qtyOrder++
                        tvFnbJumlah.text = qtyOrder.toString()
                    }

                    btnMinus.setOnClickListener {

                        if(qtyOrder>1){
                            qtyOrder--
                            tvFnbJumlah.text = qtyOrder.toString()
                        }else if(qtyOrder == 1){
                            val dialog = AlertDialog.Builder(itemView.context)
                            dialog.setMessage("Hapus Order")
                                    .setPositiveButton("Yes", {dialog_, which ->
                                        dialog_.dismiss()
                                        Log.d("debugging ", "is double?")
                                        GlobalBus.getBus().post(DataBusEvent.cancelOrder(
                                                so = data.orderSol!!,
                                                inventoryCode= data.orderInventory!!,
                                                qty = data.orderQuantity.toString(),
                                                rcp = data.orderRoomRcp!!,
                                                user= data.orderUser!!,
                                                android= data.orderDevice!!
                                        ))
                                    })

                                    .setNegativeButton("Batal", {dialog_, which ->
                                        dialog_.dismiss()
                                    })
                            dialog.show()
                        }
                    }

                    tvFnbJumlah.text = qtyOrder.toString()

                }else if(data.orderState=="2"){
                    linearLayout12.visibility = View.INVISIBLE
                    btnEditOrderSubmit.visibility = View.INVISIBLE
                    tvFnbName.setTextColor(Color.RED)
                    tvFnbName.text = "(CANCEL) ${data.orderInventoryNama}"
                }else if(data.orderState=="3"){
                    linearLayout12.visibility = View.INVISIBLE
                    btnEditOrderSubmit.visibility = View.INVISIBLE
                    tvFnbName.setTextColor(Color.YELLOW)
                    tvFnbName.text = "(PROCESS) ${data.orderInventoryNama}"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_sended_order, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}