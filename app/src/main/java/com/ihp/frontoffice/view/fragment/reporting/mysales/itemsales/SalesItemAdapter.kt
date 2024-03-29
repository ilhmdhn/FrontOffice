package com.ihp.frontoffice.view.fragment.reporting.mysales.itemsales

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.SaleItemList
import com.ihp.frontoffice.databinding.ListSalesItemsBinding
import com.ihp.frontoffice.helper.utils

class SalesItemAdapter: RecyclerView.Adapter<SalesItemAdapter.ListViewHolder>(){

    private var listData = ArrayList<SaleItemList>()
    var waktu = 0

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newListData: List<SaleItemList>, waktu: Int){
        listData.clear()
        listData.addAll(newListData.filter{it.jumlah != 0})
        this.waktu = waktu
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_sales_items, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        val backgroundColor: Int
        if (position % 2 == 0){
            backgroundColor = 1
        } else{
            backgroundColor = 2
        }
        holder.bind(data, waktu, backgroundColor)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListSalesItemsBinding.bind(itemView)
        @SuppressLint("ResourceAsColor")
        fun bind(data: SaleItemList, waktu: Int, color: Int){
            with(binding){
                if (color == 1){
                    itemView.setBackgroundResource(R.color.dark_primary_color)
                } else{
                    lyParentList.setBackgroundResource(R.color.dark_gray_primary)
                }
                tvValueItemName.text = data.namaItem
                tvValueQty.text = data.jumlah.toString()
                tvValuePrice.text = utils.getCurrency(data.total.toLong())
                itemView.setOnClickListener {
                    val toSaleperItemsNavigation = ItemSalesFragmentDirections.actionItemSalesFragmentToSaleperItemListFragment()
                    toSaleperItemsNavigation.itemName = data.namaItem
                    toSaleperItemsNavigation.waktu = waktu.toString()
                    Navigation.findNavController(it).navigate(toSaleperItemsNavigation)
                }
            }
        }
    }

    override fun getItemCount(): Int =
        listData.size
}