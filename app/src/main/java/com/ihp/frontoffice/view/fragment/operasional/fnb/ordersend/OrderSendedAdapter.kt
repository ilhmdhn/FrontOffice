package com.ihp.frontoffice.view.fragment.operasional.fnb.ordersend

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.DataOrderItem
import com.ihp.frontoffice.databinding.ListSendedOrderBinding
import com.ihp.frontoffice.events.DataBusEvent
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
            var orderNote: String = data.orderNotes ?: ""
            with(binding){
                if(data.orderState == "1"){
                    linearLayout12.visibility = View.VISIBLE
                    btnEditOrderSubmit.visibility = View.VISIBLE
                    tvFnbName.text = data.orderInventoryNama
                    tvFnbName.setTextColor(Color.WHITE)
                    tvOrderNote.text = orderNote

                    btnPlus.setOnClickListener {
                        qtyOrder++
                        tvFnbJumlah.text = qtyOrder.toString()
                    }

                    btnMinus.setOnClickListener {

                        if(qtyOrder>1){
                            qtyOrder--
                            tvFnbJumlah.text = qtyOrder.toString()
                        }else if(qtyOrder == 1){
                            val dialog = MaterialAlertDialogBuilder(itemView.context, R.style.MaterialAlertDialogDarkTheme)
                            dialog.setMessage("Hapus Order")
                                    .setPositiveButton("Yes", {dialog_, which ->
                                        dialog_.dismiss()
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

                    btnEditOrderSubmit.setOnClickListener {
                        val dialog = AlertDialog.Builder(itemView.context)
                        dialog.setMessage("Hapus Order")
                                .setPositiveButton("Yes", {dialog_, which ->
                                    dialog_.dismiss()
                                    GlobalBus.getBus().post(DataBusEvent.revisiOrder(
                                            so = data.orderSol!!,
                                            inventoryCode= data.orderInventory!!,
                                            note = orderNote,
                                            qty = data.orderQuantity.toString(),
                                            qtyTemp=qtyOrder.toString(),
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

                    tvNotes.setOnClickListener {
                        val builder: AlertDialog.Builder
                        builder = AlertDialog.Builder(itemView.context)
                        lateinit var etNoteOrder: EditText
                        val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.input_text, null)
                        etNoteOrder = dialogView.findViewById(R.id.ly_input_txt)
                        etNoteOrder.setText(data.orderNotes)
                        builder.setMessage("Tulis Catatan Order")
                                .setView(dialogView)
                                .setPositiveButton(R.string.ok,
                                        DialogInterface.OnClickListener { dialog, id ->
                                            if (!etNoteOrder.text.toString().isEmpty()){
                                                orderNote = etNoteOrder.text.toString()
                                            }
                                            tvOrderNote.text = orderNote
                                            dialog.dismiss();
                                        })
                        builder.show()
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