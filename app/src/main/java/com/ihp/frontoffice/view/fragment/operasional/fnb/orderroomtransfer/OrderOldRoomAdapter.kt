package com.ihp.frontoffice.view.fragment.operasional.fnb.orderroomtransfer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.OkdBeforeTransferItem
import com.ihp.frontoffice.data.remote.respons.OklBeforeTransfer
import com.ihp.frontoffice.databinding.ListOrderOldRoomBinding
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.events.EventsWrapper.CheckoutRoom
import com.ihp.frontoffice.events.GlobalBus
import com.ihp.frontoffice.helper.utils

class OrderOldRoomAdapter: RecyclerView.Adapter<OrderOldRoomAdapter.ListViewHolder>() {

    private var listData = ArrayList<OkdBeforeTransferItem>()
    private var listOkl = ArrayList<OklBeforeTransfer>()

    fun setData(newListData: List<OklBeforeTransfer>){
        val tempList = ArrayList<OkdBeforeTransferItem>()
        for(item in newListData){
            for(items in item.okd){
                tempList.add(items)
            }
        }
        listData.clear()
        listOkl.clear()
        listOkl.addAll(newListData)
        listData.addAll(tempList)
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val binding = ListOrderOldRoomBinding.bind(itemView)
        @SuppressLint("SetTextI18n")
        fun bind(data: OkdBeforeTransferItem, okl: List<OklBeforeTransfer>){
            val roomCode = okl.filter { item ->
                item.orderCode == data.orderCode
            }
            with(binding){
                tvRoomCode.text = "(${roomCode[0].room})"
                tvItemName.text = data.name
                tvItemQty.text = "qty: ${data.qty}"
                tvTotalPrice.text = utils.getCurrency(data.price.toLong())
                btnCancel.setOnClickListener {
                    GlobalBus
                            .getBus()
                            .post(DataBusEvent.cancelOldOrder(data.inventoryCode, data.orderCode, data.soCode, data.name, data.qty))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_order_old_room, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data, listOkl)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}