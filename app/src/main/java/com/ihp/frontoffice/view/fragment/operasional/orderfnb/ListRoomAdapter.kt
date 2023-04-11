package com.ihp.frontoffice.view.fragment.operasional.orderfnb

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.entity.Room
import com.ihp.frontoffice.databinding.LayoutListRoomCheckinBinding

class ListRoomAdapter: RecyclerView.Adapter<ListRoomAdapter.ListViewHolder>() {

    private var listData = ArrayList<Room>()

    fun setData(newListData: List<Room>){
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = LayoutListRoomCheckinBinding.bind(itemView)

        fun bind(data: Room){
            with(binding){
                tvRoomName.text = data.roomCode

                if (data.statusPrinter != "0"){
                    tvRoomState.text = "DITAGIHKAN"
                    tvRoomState.setTextColor(Color.RED)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_room_checkin, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size
}