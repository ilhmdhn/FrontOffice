package com.ihp.frontoffice.view.fragment.operasional.orderfnb.order

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ihp.frontoffice.data.remote.InventoryClient
import com.ihp.frontoffice.data.remote.respons.DataInventoryPaging
import com.ihp.frontoffice.data.remote.respons.InventoryPagingResponse
import java.io.IOException

class FnbPagingSource(private val inventoryClient: InventoryClient, private val category: String, private val search: String): PagingSource<Int, DataInventoryPaging>() {
    override fun getRefreshKey(state: PagingState<Int, DataInventoryPaging>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataInventoryPaging> {
        return try{
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = inventoryClient.getInventoryPaging(position, params.loadSize, category, search)
            if(responseData.state == false){
                throw IOException(responseData.message)
            }

            LoadResult.Page(
                    data = responseData.data,
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (responseData.data.isNullOrEmpty()) null else position + 1
            )
        }catch (e: Exception){
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}