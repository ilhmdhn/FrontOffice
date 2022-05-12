package livs.code.frontoffice.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import livs.code.frontoffice.data.remote.ApiRestService
import livs.code.frontoffice.data.remote.ApprovalClient
import livs.code.frontoffice.data.remote.StatusKasClient
import livs.code.frontoffice.data.remote.respons.Response
import livs.code.frontoffice.data.remote.respons.StatusKasResponse
import retrofit2.Call
import retrofit2.Callback

class IhpRepository {

    fun submitApproval(baseUrl: String, userId: String, levelUser: String, room: String, keterangan: String): Response {
        val responseData = Response()
        val client = ApiRestService.getClient(baseUrl).create(ApprovalClient::class.java)
        client.submitApproval(userId, levelUser, room, keterangan).enqueue(object :
            Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful){
                    responseData.message = "Success"
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                responseData.message = t.message.toString()
            }
        })
        return responseData
    }

    fun getReportKas(baseUrl: String, tanggal: String, shift: String, chusr:  String): LiveData<StatusKasResponse> {
        val responseData = MutableLiveData<StatusKasResponse>()
        val client = ApiRestService.getClient(baseUrl).create(StatusKasClient::class.java)
        client.getStatusKasReport(tanggal, shift, chusr).enqueue(object: Callback<StatusKasResponse>{
            override fun onResponse(call: Call<StatusKasResponse>, response: retrofit2.Response<StatusKasResponse>) {
                    responseData.postValue(response.body())
                }

            override fun onFailure(call: Call<StatusKasResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }
        })
        return responseData
    }
}