package com.ihp.frontoffice.view.fragment.operasional.fnb.order

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.data.model.OrderModel
import com.ihp.frontoffice.data.remote.respons.DataInventoryPaging
import com.ihp.frontoffice.data.remote.respons.xOrderDataItem
import com.ihp.frontoffice.databinding.ListFnbBinding
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.events.GlobalBus
import com.ihp.frontoffice.helper.utils

class FnbPagingAdapter: PagingDataAdapter<DataInventoryPaging, FnbPagingAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
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
            binding.btnAddOrder.setOnClickListener {
                GlobalBus.getBus().post(DataBusEvent.OrderModel(
                        inventoryCode = data.inventoryCode,
                        orderQty = 1,
                        orderNotes = "",
                        itemName = data.name,
                        itemPrice = data.price.toLong(),
                        itemLocation = data.location.toString(),
                ))
            }
            binding.tvFnbName.text = data.name
            binding.tvFnbPrice.text = utils.getCurrency(data.price.toLong())
        }

        /*            var jumlahOrder = 0
            val order = ArrayList<OrderModel>()
            val orderItem = OrderModel(
                    inventoryCode = data.inventoryCode,
                    orderQty = 1,
                    orderNotes = "",
                    itemName = data.name
            )
            order.add(
                    orderItem
            )

            binding.btnPlus.setOnClickListener {
                jumlahOrder++
                binding.tvFnbJumlah.text = jumlahOrder.toString()
            }

            binding.btnMinus.setOnClickListener {
                if(jumlahOrder>0){
                    jumlahOrder--
                }
                binding.tvFnbJumlah.text = jumlahOrder.toString()
            }

            binding.tvFnbJumlah.text = jumlahOrder.toString()
 */
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
