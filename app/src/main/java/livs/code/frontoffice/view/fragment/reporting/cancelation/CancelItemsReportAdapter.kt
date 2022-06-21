package livs.code.frontoffice.view.fragment.reporting.cancelation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import livs.code.frontoffice.R
import livs.code.frontoffice.data.remote.respons.DataCancelItems
import livs.code.frontoffice.databinding.ListCancelItemsBinding

class CancelItemsReportAdapter: RecyclerView.Adapter<CancelItemsReportAdapter.ListViewHolder>() {

    private var listData = ArrayList<DataCancelItems>()

    fun setData(newListData: List<DataCancelItems>){
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListCancelItemsBinding.bind(itemView)
        fun bind(data: DataCancelItems){
            with(binding){
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
    ): CancelItemsReportAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_cancel_items, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CancelItemsReportAdapter.ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}