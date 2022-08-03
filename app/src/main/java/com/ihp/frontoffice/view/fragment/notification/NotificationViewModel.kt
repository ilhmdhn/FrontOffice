package com.ihp.frontoffice.view.fragment.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ihp.frontoffice.data.remote.respons.RoomCallResponse
import com.ihp.frontoffice.data.repository.IhpRepository

class NotificationViewModel: ViewModel() {
    private val ihpRepository = IhpRepository()

    fun getCallRoom(url: String): LiveData<RoomCallResponse>{
        return ihpRepository.getRoomCall(url)
    }
}