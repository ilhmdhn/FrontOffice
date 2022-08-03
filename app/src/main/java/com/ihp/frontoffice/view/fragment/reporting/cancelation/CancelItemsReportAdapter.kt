package com.ihp.frontoffice.view.fragment.reporting.cancelation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.DataCancelItems
import com.ihp.frontoffice.databinding.ListCancelItemsBinding

class CancelItemsReportAdapter: RecyclerView.Adapter<CancelItemsReportAdapter.ListViewHolder>() {

    private var listData = ArrayList<DataCancelItems>()

    fun setData(newListData: List<DataCancelItems>){
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListCancelItemsBinding.bind(itemView)
        fun bind(data: DataCancelItems, kodeWarna: Int){
            with(binding){
                if (kodeWarna == 1){
                    itemView.setBackgroundResource(R.drawable.background_baby_blue)
                } else{
                    itemView.setBackgroundResource(R.drawable.background_white)
                }
                tvJam.text = data.jam
                tvItemName.text = data.namaItem
                tvKamar.text = data.kamar
                tvJumlah.text = data.jumlahItem.toString()
                tvChusr.text = data.chusr
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_cancel_items, parent, false)
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
        holder.bind(data, backgroundColor)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}