package com.ihp.frontoffice.view.fragment.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.DataRoomCall
import com.ihp.frontoffice.data.repository.IhpRepository
import com.ihp.frontoffice.databinding.HolderViewNotifyBinding

class NotificationAdapter: RecyclerView.Adapter<NotificationAdapter.ListViewHolder>() {

    private var listData = ArrayList<DataRoomCall>()

    fun setData(newListData: List<DataRoomCall>){
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = HolderViewNotifyBinding.bind(itemView)
        @SuppressLint("SetTextI18n")
        fun bind(data: DataRoomCall){
            with(binding){
                roomCode.text = data.kamar
                tvRoomAlias.text = "(${data.kamarAlias})"
                keterangan.text = "Tamu ${data.namaTamu} Memanggil"
                val ihpRepository = IhpRepository()
                btnResponse.setOnClickListener {
                    ihpRepository.responseRoomCall(itemView.context, data.kamar, 0)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.holder_view_notify, parent, false)
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