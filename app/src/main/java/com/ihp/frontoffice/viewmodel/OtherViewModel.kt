package com.ihp.frontoffice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ihp.frontoffice.data.remote.respons.PrintStatusResponse
import com.ihp.frontoffice.data.remote.respons.Response
import com.ihp.frontoffice.data.repository.IhpRepository

class OtherViewModel: ViewModel() {
    val ihpRepository = IhpRepository()

    fun getJumlahApproval(baseUrl: String, username: String): LiveData<Response>{
        return ihpRepository.getJumlahApproval(baseUrl, username)
    }

    fun printStatus(url: String, rcp: String): LiveData<PrintStatusResponse>{
        return ihpRepository.printStatus(url, rcp)
    }
}