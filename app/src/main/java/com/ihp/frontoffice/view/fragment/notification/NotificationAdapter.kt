package com.ihp.frontoffice.view.fragment.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
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
                roomCode.text = data.room
                if (data.isNow == 0){
                    description.text = data.keterangan +" "+data.user
                    btnResponse.visibility = View.GONE
                } else{
                    description.text = data.keterangan
                    btnResponse.visibility = View.VISIBLE
                }
                val ihpRepository = IhpRepository()
                btnResponse.setOnClickListener {
                    ihpRepository.responseRoomCall(itemView.context, data.room, 0)
                    Navigation.findNavController(itemView).navigate(NotificationFragmentDirections.actionNavNotificationFragmentSelf())
//                    Navigation.findNavController(itemView).popBackStack()
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