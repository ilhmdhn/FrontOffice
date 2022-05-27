package livs.code.frontoffice.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import livs.code.frontoffice.data.remote.*
import livs.code.frontoffice.data.remote.respons.*
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
                responseData.postValue(StatusKasResponse(DataStatusKas(), false, t.message.toString()))
            }
        })
        return responseData
    }

    fun getCashDetail(baseUrl: String, tanggal: String, shift: String): LiveData<PecahanUangResponse>{
        val responseData = MutableLiveData<PecahanUangResponse>()
        val client = ApiRestService.getClient(baseUrl).create(PecahanUangClient::class.java)
        client.getCashDetail(tanggal, shift).enqueue(object: Callback<PecahanUangResponse>{
            override fun onResponse(call: Call<PecahanUangResponse>,response: retrofit2.Response<PecahanUangResponse>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<PecahanUangResponse>, t: Throwable) {
                responseData.postValue(PecahanUangResponse(PecahanUangData(), false, t.message))
            }
        })
        return responseData
    }

    fun postCashDetail(baseUrl: String, tanggal: String, shift: String, pecahanUang: PecahanUang): LiveData<Response>{
        val responseData = MutableLiveData<Response>()
        val  client = ApiRestService.getClient(baseUrl).create(ReportClient::class.java)
        client.postCashDetail(tanggal, shift, pecahanUang.seratus_ribu, pecahanUang.lima_puluh_ribu, pecahanUang.dua_puluh_ribu, pecahanUang.sepuluh_ribu, pecahanUang.lima_ribu, pecahanUang.dua_ribu, pecahanUang.seribu, pecahanUang.lima_ratus, pecahanUang.dua_ratus,  pecahanUang.seratus, pecahanUang.lima_puluh, pecahanUang.dua_lima)
            .enqueue(object: Callback<Response>{
                override fun onResponse(call: Call<Response>,response: retrofit2.Response<Response>) {
                    responseData.postValue(response.body())
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    responseData.postValue(Response(null, false, t.message.toString()))
                }
            })
        return responseData
    }

    fun updateCashDetail(baseUrl: String, tanggal: String, shift: String, pecahanUang: PecahanUang):  LiveData<Response>{
        val responseData = MutableLiveData<Response>()
        val client = ApiRestService.getClient(baseUrl).create(ReportClient::class.java)
        client.updateCashDetail(tanggal, shift, pecahanUang.seratus_ribu, pecahanUang.lima_puluh_ribu, pecahanUang.dua_puluh_ribu, pecahanUang.sepuluh_ribu, pecahanUang.lima_ribu, pecahanUang.dua_ribu, pecahanUang.seribu, pecahanUang.lima_ratus, pecahanUang.dua_ratus,  pecahanUang.seratus, pecahanUang.lima_puluh, pecahanUang.dua_lima)
            .enqueue(object: Callback<Response>{
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                    responseData.postValue(response.body())
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    responseData.postValue(Response(null, false, t.message.toString()))
                }
            })
        return responseData
    }
}