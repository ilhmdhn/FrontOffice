package com.ihp.frontoffice.view.fragment.operasional.fnb.order

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.DataInventoryPaging
import com.ihp.frontoffice.databinding.ListFnbBinding
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.events.GlobalBus
import com.ihp.frontoffice.helper.utils

class FnbPagingAdapter: PagingDataAdapter<DataInventoryPaging, FnbPagingAdapter.MyViewHolder>(DIFF_CALLBACK){

    val addedItem = ArrayList<DataBusEvent.OrderModel>()
    fun insertAddItem(data: List<DataBusEvent.OrderModel>){
        addedItem.clear()
        addedItem.addAll(data)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            holder.bind(data, addedItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListFnbBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(private val binding: ListFnbBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataInventoryPaging, added: ArrayList<DataBusEvent.OrderModel>){

            val addedItem = added.filter { it.inventoryCode == data.inventoryCode }
            if(addedItem.isEmpty()){
                binding.btnAddOrder.text = "tambahkan"
                binding.btnAddOrder.visibility = View.VISIBLE
                binding.llPlusMinus.visibility = View.GONE
                binding.btnAddOrder.isEnabled = true
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
            }else{
                var qtyOrder = addedItem[0].orderQty;
//                binding.btnAddOrder.text = "Ditambahkan"
                binding.tvFnbJumlah.text = addedItem[0].orderQty.toString()
                binding.btnAddOrder.visibility = View.GONE
                binding.llPlusMinus.visibility = View.VISIBLE
                binding.btnAddOrder.isEnabled = false

                binding.btnPlus.setOnClickListener {
                    qtyOrder++
                    addedItem[0].orderQty = qtyOrder
                    GlobalBus.getBus().post(DataBusEvent.OrderModel(
                        inventoryCode = data.inventoryCode,
                        orderQty = qtyOrder,
                        orderNotes = "",
                        itemName = data.name,
                        itemPrice = data.price.toLong(),
                        itemLocation = data.location.toString(),
                    ))
                }

                binding.btnMinus.setOnClickListener {
                    qtyOrder--
                    if(qtyOrder>0){
                        addedItem[0].orderQty = qtyOrder
                        GlobalBus.getBus().post(DataBusEvent.OrderModel(
                            inventoryCode = data.inventoryCode,
                            orderQty = qtyOrder,
                            orderNotes = "",
                            itemName = data.name,
                            itemPrice = data.price.toLong(),
                            itemLocation = data.location.toString(),
                        ))
                    }else{
                        val builder = MaterialAlertDialogBuilder(itemView.context, R.style.AlertDialogTheme)
//                        val builder: AlertDialog.Builder.
//                        builder = AlertDialog.Builder(itemView.context)
                        builder
                            .setMessage("Hapus ${data.name} ?")
                            .setPositiveButton("OK",
                                DialogInterface.OnClickListener{ dialog, which ->
                                    GlobalBus.getBus().post(DataBusEvent.DeleteOrder(
                                        inventoryCode = data.inventoryCode
                                    ))
                                    dialog.dismiss()
                                })
                        builder.show()
                    }
                }
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
