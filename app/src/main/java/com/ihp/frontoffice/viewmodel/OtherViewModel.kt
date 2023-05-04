package com.ihp.frontoffice.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.data.remote.ApiConfig
import com.ihp.frontoffice.data.remote.InventoryClient
import com.ihp.frontoffice.data.remote.respons.*
import com.ihp.frontoffice.data.repository.IhpRepository
import com.ihp.frontoffice.data.repository.LocalRepository
import com.ihp.frontoffice.events.DataBusEvent

class OtherViewModel: ViewModel() {
    val ihpRepository = IhpRepository()

    fun getJumlahApproval(baseUrl: String, username: String): LiveData<Response>{
        return ihpRepository.getJumlahApproval(baseUrl, username)
    }

    fun printStatus(url: String, rcp: String): LiveData<PrintStatusResponse>{
        return ihpRepository.printStatus(url, rcp)
    }

    fun getLoginStatus(context:Context): LiveData<User>{
       val localDataSource = LocalRepository.getInstance(context);
        return localDataSource.user
    }

    fun getBillData(url: String, room: String): LiveData<xBillResponse>{
        return ihpRepository.printMobilePrintBill(url, room)
    }

    fun viewBill(url: String, room: String): LiveData<xBillResponse>{
        return ihpRepository.getViewBill(url, room)
    }



    fun getInvoiceData(url: String, rcp: String): LiveData<xInvoiceResponse>{
        return ihpRepository.printMobileInvoice(url, rcp)
    }

    fun checkUser(url: String, userId: String, password: String): LiveData<UserResponse>{
        return ihpRepository.checkUser(url, userId, password)
    }

    fun removePromoFnB(url: String, rcp: String):LiveData<Response>{
        return ihpRepository.removePromoFnB(url, rcp)
    }

    fun checkinSlip(url: String, rcp: String): LiveData<CheckinSlipResponse>{
        return ihpRepository.checkinSlip(url, rcp)
    }
    fun fnbPaging(url: String, category: String, search: String):LiveData<PagingData<DataInventoryPaging>>{
        val apiService = ApiConfig.getApiService(url)
        return ihpRepository.getFnbPaging(category, search, apiService).cachedIn(viewModelScope)
    }

    fun sendOrder(url: String, chusr: String, roomCode: String, rcp: String, roomType: String, checkinDuration: String,  order: ArrayList<DataBusEvent.OrderModel>): LiveData<Response>{
        return ihpRepository.sendOrder(url, chusr, roomCode, rcp, roomType, checkinDuration, order)
    }

    fun getOrder(url: String, roomCode: String): LiveData<OrderResponse>{
        return ihpRepository.getRoomOrder(url, roomCode)
    }
    fun cancelOrder(url: String, so: String, inventoryCode: String, qty: String, rcp: String, user: String, android: String): LiveData<Response>{
        return ihpRepository.cancelOrder(url, so, inventoryCode, qty, rcp, user, android)
    }
}