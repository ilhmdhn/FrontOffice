package com.ihp.frontoffice.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ihp.frontoffice.data.remote.InventoryClient
import com.ihp.frontoffice.data.remote.respons.DataInventoryPaging
import com.ihp.frontoffice.view.fragment.operasional.orderfnb.order.FnbPagingSource

class PagingRepository(private val apiService: InventoryClient) {
    fun getFnbPaging(category: String, search: String): LiveData<PagingData<DataInventoryPaging>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 5
                ),
                pagingSourceFactory = {
                    FnbPagingSource(apiService)
                }
        ).liveData
    }
}