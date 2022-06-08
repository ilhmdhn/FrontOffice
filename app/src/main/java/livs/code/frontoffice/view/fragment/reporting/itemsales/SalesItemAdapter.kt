package livs.code.frontoffice.view.fragment.reporting.itemsales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import livs.code.frontoffice.R
import livs.code.frontoffice.data.remote.respons.SaleItemList
import livs.code.frontoffice.databinding.ListSalesItemsBinding
import livs.code.frontoffice.helper.utils

class SalesItemAdapter: RecyclerView.Adapter<SalesItemAdapter.ListViewHolder>(){

    private var listData = ArrayList<SaleItemList>()


    fun setData(newListData: List<SaleItemList>){
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_sales_items, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListSalesItemsBinding.bind(itemView)
        fun bind(data: SaleItemList){
            with(binding){
                if (data.jumlah != 0 || data.total != 0){
                    tvValueItemName.text = data.namaItem
                    tvValueQty.text = data.jumlah.toString()
                    tvValuePrice.text = utils.getCurrency(data.total.toLong())
                }
            }
        }
    }

    override fun getItemCount(): Int =
        listData.size
}