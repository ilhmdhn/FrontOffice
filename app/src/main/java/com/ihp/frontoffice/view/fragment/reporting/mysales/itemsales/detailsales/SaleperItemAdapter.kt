package com.ihp.frontoffice.view.fragment.reporting.mysales.itemsales.detailsales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.SalesItembyName
import com.ihp.frontoffice.databinding.ListSalePerItemBinding

class SaleperItemAdapter: RecyclerView.Adapter<SaleperItemAdapter.ListViewHolder>() {

    private var listData = ArrayList<SalesItembyName>()

    fun setData(newListData: List<SalesItembyName>){
        listData.clear()
        listData.addAll(newListData.filter{it.jumlah != 0})
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListSalePerItemBinding.bind(itemView)
        fun bind(data: SalesItembyName, colorCode: Int){
            with(binding){
                if (colorCode == 1){
                    itemView.setBackgroundResource(R.color.dark_gray_primary)
                } else{
                    itemView.setBackgroundResource(R.color.dark_primary_color)
                }
                tvTanggal.text = data.tanggal
                tvNamaItem.text = data.namaItem
                tvRoom.text = data.room
                tvJumlah.text = data.jumlah.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): SaleperItemAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_sale_per_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SaleperItemAdapter.ListViewHolder, position: Int) {
        val data = listData[position]
        val backgroundColor:  Int
        if (position % 2==0){
            backgroundColor = 1
        }else{
            backgroundColor = 2
        }
        holder.bind(data,  backgroundColor)
    }

    override fun getItemCount(): Int = listData.size
}