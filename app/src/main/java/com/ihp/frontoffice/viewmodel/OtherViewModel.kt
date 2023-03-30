package com.ihp.frontoffice.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.data.remote.respons.*
import com.ihp.frontoffice.data.repository.IhpRepository
import com.ihp.frontoffice.data.repository.LocalRepository

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
}