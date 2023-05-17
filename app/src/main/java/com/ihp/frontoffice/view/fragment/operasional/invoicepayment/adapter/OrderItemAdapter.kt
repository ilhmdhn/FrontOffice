package com.ihp.frontoffice.view.fragment.operasional.invoicepayment.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.*
import com.ihp.frontoffice.databinding.ListOrderBinding
import com.ihp.frontoffice.helper.utils

class OrderItemAdapter: RecyclerView.Adapter<OrderItemAdapter.ListViewHolder>() {

    var listOrder = ArrayList<xOrderDataItem>()
    var listCancelOrder = ArrayList<xCancelOrderDataItem>()
    var listCancelPromo = ArrayList<xPromoOrderCancelItem>()
    var listOrderPromo = ArrayList<xPromoOrderDataItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(xData: xData){
        listOrder.clear()
        listCancelOrder.clear()
        listOrderPromo.clear()
        listCancelPromo.clear()

        Log.d("liat item masuk adapter", xData.toString())

        val listOrderItem = xData.orderData as List<xOrderDataItem>
        val listOrderPromoItem = xData.promoOrderData as List<xPromoOrderDataItem>
        val listCancelOrderDataItem = xData.cancelOrderData as List<xCancelOrderDataItem>
        val listCancelPromoDataItem = xData.promoOrderCancel as List<xPromoOrderCancelItem>


        listOrder.addAll(listOrderItem)
        listOrderPromo.addAll(listOrderPromoItem)
        listCancelOrder.addAll(listCancelOrderDataItem)
        listCancelPromo.addAll(listCancelPromoDataItem)

        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = ListOrderBinding.bind(itemView)
        @SuppressLint("SetTextI18n")
        fun bind(data: xOrderDataItem, promo: ArrayList<xPromoOrderDataItem>, cancel: ArrayList<xCancelOrderDataItem>, cancelPromo: ArrayList<xPromoOrderCancelItem>){
            Log.d("liat promo atas ", promo.toString())
            val isCancel = cancel.filter { it.orderCode == data.orderCode && it.inventoryCode == data.inventoryCode}
            val isPromo = promo.filter { it.orderCode == data.orderCode && it.inventoryCode == data.inventoryCode}
            val isCancelPromo = cancelPromo.filter { it.orderCode == data.orderCode && it.inventoryCode == data.inventoryCode}
            var hiddenPromo = false
            with(binding){
                hiddenPromo = false
                binding.tvItemOrderName.text = data.namaItem
                binding.tvOrderCount.text = data.jumlah.toString()
                binding.tvOrderPrice.text = utils.getCurrency(data.harga?.toLong())
                binding.tvOrderTotalPriceItem.text = utils.getCurrency(data.total?.toLong())
                Log.d("liat promo", isPromo.toString())
                if(isPromo.isNotEmpty()){
                    binding.tvFnbDiscount.visibility = View.VISIBLE
                    binding.tvDiscountPrice.visibility = View.VISIBLE
                    if(isCancelPromo.isNotEmpty()){
                        if(isPromo[0].orderCode == isCancelPromo[0].orderCode && isPromo[0].inventoryCode == isCancelPromo[0].inventoryCode){
                            binding.tvFnbDiscount.text = isPromo[0].promoName
                            binding.tvDiscountPrice.text = "(${utils.getCurrency(isCancelPromo[0].promoPrice?.let { isPromo[0].promoPrice?.toLong()?.minus(it) })})"
                            hiddenPromo = true;
                        }else{
                            binding.tvFnbDiscount.text = isPromo[0].promoName
                            binding.tvDiscountPrice.text = "(${utils.getCurrency(isPromo[0].promoPrice?.toLong())})"
                        }
                    }else{
                            binding.tvFnbDiscount.text = isPromo[0].promoName
                            binding.tvDiscountPrice.text = "(${utils.getCurrency(isPromo[0].promoPrice?.toLong())})"
                    }
                }else{
                    binding.tvFnbDiscount.visibility = View.GONE
                    binding.tvDiscountPrice.visibility = View.GONE
                }

                if(isCancel.isNotEmpty()){
                    binding.tvReturItemName.visibility = View.VISIBLE
                    binding.tvReturItemCount.visibility = View.VISIBLE
                    binding.tvReturItemPrice.visibility = View.VISIBLE
                    binding.tvDummyKaliRetur.visibility = View.VISIBLE
                    binding.tvReturItemTotalPrice.visibility = View.VISIBLE


                    binding.tvReturItemName.text = "RETUR " +isCancel[0].namaItem
                    binding.tvReturItemCount.text = isCancel[0].jumlah.toString()
                    binding.tvReturItemPrice.text = utils.getCurrency(isCancel[0].harga?.toLong())
                    binding.tvReturItemTotalPrice.text = "(" + utils.getCurrency(isCancel[0].total?.toLong()) + ")"

                    if(data.orderCode == isCancel[0].orderCode && data.inventoryCode == isCancel[0].inventoryCode && data.jumlah == isCancel[0].jumlah){
                        binding.tvFnbDiscount.visibility = View.GONE
                        binding.tvDiscountPrice.visibility = View.GONE
                    }else if(isPromo.isNotEmpty()){
                        binding.tvFnbDiscount.visibility = View.VISIBLE
                        binding.tvDiscountPrice.visibility = View.VISIBLE
                    }

                }else{
                    binding.tvReturItemName.visibility = View.GONE
                    binding.tvDummyKaliRetur.visibility = View.GONE
                    binding.tvReturItemCount.visibility = View.GONE
                    binding.tvReturItemPrice.visibility = View.GONE
                    binding.tvReturItemTotalPrice.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_order, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listOrder[position]
        holder.bind(data, listOrderPromo, listCancelOrder, listCancelPromo)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }
}