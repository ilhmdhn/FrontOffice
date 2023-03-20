package com.ihp.frontoffice.view.fragment.operasional.invoicepayment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.xTransferBillDataItem
import com.ihp.frontoffice.data.remote.respons.xTransferListDataItem
import com.ihp.frontoffice.databinding.ListTransferBinding
import com.ihp.frontoffice.helper.utils

class TransferItemAdapter: RecyclerView.Adapter<TransferItemAdapter.ListViewHolder>() {

    var listTransfer = ArrayList<xTransferListDataItem>()

    fun setData(data: List<xTransferListDataItem>){
        listTransfer.clear()
        listTransfer.addAll(data)
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = ListTransferBinding.bind(itemView)
        @SuppressLint("SetTextI18n")
        fun bind(data: xTransferListDataItem){
            with(binding){
                binding.tvTransferRoomName.text ="TRANSFER ROOM " + data.room
                binding.tvTransferPrice.text = utils.getCurrency(data.transferTotal?.toLong())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_transfer, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listTransfer[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return listTransfer.size
    }
}