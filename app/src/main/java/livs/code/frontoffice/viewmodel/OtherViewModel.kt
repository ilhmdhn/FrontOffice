package livs.code.frontoffice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import livs.code.frontoffice.data.remote.respons.Response
import livs.code.frontoffice.data.repository.IhpRepository

class OtherViewModel: ViewModel() {
    val ihpRepository = IhpRepository()

    fun getJumlahApproval(baseUrl: String, username: String): LiveData<Response>{
        return ihpRepository.getJumlahApproval(baseUrl, username)
    }
}