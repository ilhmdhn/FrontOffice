package com.ihp.frontoffice.view.fragment.operasional.orderfnb.order

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.data.remote.respons.DataInventoryPaging
import com.ihp.frontoffice.databinding.ListFnbBinding
import com.ihp.frontoffice.helper.utils

class FnbPagingAdapter: PagingDataAdapter<DataInventoryPaging, FnbPagingAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        Log.d("datanya di adapter",data.toString())
        if(data != null){
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListFnbBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(private val binding: ListFnbBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataInventoryPaging){
            binding.tvFnbName.text = data.name
            binding.tvFnbPrice.text = utils.getCurrency(data.price.toLong())
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataInventoryPaging>() {
            override fun areItemsTheSame(oldItem: DataInventoryPaging, newItem: DataInventoryPaging): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataInventoryPaging, newItem: DataInventoryPaging): Boolean {
                return oldItem.inventoryCode == newItem.inventoryCode
            }
        }
    }
}
