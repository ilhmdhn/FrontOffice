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

    fun getBillData(url: String, room: String): LiveData<PrintBillDataResponse>{
        return ihpRepository.printMobilePrintBill(url, room)
    }

    fun getInvoiceData(url: String, rcp: String): LiveData<PrintInvoiceDataResponse>{
        return ihpRepository.printMobileInvoice(url, rcp)
    }
}