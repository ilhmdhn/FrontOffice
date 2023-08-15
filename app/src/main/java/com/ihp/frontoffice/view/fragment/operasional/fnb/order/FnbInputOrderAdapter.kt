package com.ihp.frontoffice.view.fragment.operasional.fnb.order

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ihp.frontoffice.R
import com.ihp.frontoffice.databinding.DialogOrderBinding
import com.ihp.frontoffice.databinding.ListReviewOrderBinding
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.events.GlobalBus

class FnbInputOrderAdapter: RecyclerView.Adapter<FnbInputOrderAdapter.ListViewHolder>() {

    private var listData = ArrayList<DataBusEvent.OrderModel>()

    fun setData(newListData: List<DataBusEvent.OrderModel>){
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<DataBusEvent.OrderModel>{
        return listData
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListReviewOrderBinding.bind(itemView)
        @SuppressLint("SetTextI18n")
        fun bind(data: DataBusEvent.OrderModel, index: Int, listData : ArrayList<DataBusEvent.OrderModel>){
            with(binding){
                tvFoodName.text = data.itemName
                tvFnbJumlah.text = data.orderQty.toString()
                tvOrderNote.text = data.orderNotes
                btnPlus.setOnClickListener {
                        listData[index].orderQty++

                        GlobalBus.getBus().post(ArrayList<DataBusEvent.OrderModel>(
                                listData
                        ))
                }
                btnMinus.setOnClickListener {
                    if(listData[index].orderQty>1){
                        listData[index].orderQty--
                        GlobalBus.getBus().post(ArrayList<DataBusEvent.OrderModel>(
                                listData
                        ))
                    }else{
                        val builder = MaterialAlertDialogBuilder(itemView.context, R.style.AlertDialogTheme)
//                        val builder: AlertDialog.Builder
//                        builder = AlertDialog.Builder(itemView.context)
                        builder
                                .setMessage("Hapus ${data.itemName} ?")
                                .setPositiveButton("OK",
                                DialogInterface.OnClickListener{dialog, which ->
                                    listData.removeAt(index)
                                    GlobalBus.getBus().post(ArrayList<DataBusEvent.OrderModel>(
                                            listData
                                    ))
                                    dialog.dismiss()
                                })
                        builder.show()
                    }
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
                                            listData[index].orderNotes = etNoteOrder.text.toString()
                                            GlobalBus.getBus().post(ArrayList<DataBusEvent.OrderModel>(
                                                    listData
                                            ))
                                        }
                                        dialog.dismiss();
                                    })
                    builder.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_review_order, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data, position, listData)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}