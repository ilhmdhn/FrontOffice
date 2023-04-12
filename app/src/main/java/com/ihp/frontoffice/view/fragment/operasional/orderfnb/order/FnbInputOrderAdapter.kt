package com.ihp.frontoffice.view.fragment.operasional.orderfnb.order

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.databinding.DialogOrderBinding
import com.ihp.frontoffice.databinding.ListReviewOrderBinding
import com.ihp.frontoffice.events.DataBusEvent

class FnbInputOrderAdapter: RecyclerView.Adapter<FnbInputOrderAdapter.ListViewHolder>() {

    private var listData = ArrayList<DataBusEvent.OrderModel>()

    fun setData(newListData: List<DataBusEvent.OrderModel>){
        listData.clear()
        listData.addAll(newListData)
        Log.d("datanya", listData.toString())
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<DataBusEvent.OrderModel>{
        return listData
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListReviewOrderBinding.bind(itemView)

        fun bind(data: DataBusEvent.OrderModel, index: Int){
            with(binding){
                tvFoodName.text = data.itemName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FnbInputOrderAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.dialog_order, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FnbInputOrderAdapter.ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}