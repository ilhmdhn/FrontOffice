package com.ihp.frontoffice.view.fragment.operasional.fnb.ordersend

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.DataOrderItem
import com.ihp.frontoffice.databinding.ListSendedOrderBinding

class OrderSendedAdapter: RecyclerView.Adapter<OrderSendedAdapter.ListViewHolder>() {

    private var listData = ArrayList<DataOrderItem>()

    fun setData(newListData: List<DataOrderItem>){
        listData.clear()
        listData.addAll(newListData)
        Log.d("DEBUGGING data order sended ", listData.toString())
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = ListSendedOrderBinding.bind(itemView)

        fun bind(data: DataOrderItem){
            with(binding){
                tvFnbName.text = data.orderInventoryNama
                tvOrderNote.text = data.orderNotes
                tvFnbJumlah.text = data.orderQuantity.toString()
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