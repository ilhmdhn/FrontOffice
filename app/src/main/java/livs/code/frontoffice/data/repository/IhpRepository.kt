package livs.code.frontoffice.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.dmoral.toasty.Toasty
import livs.code.frontoffice.data.remote.*
import livs.code.frontoffice.data.remote.respons.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.create

class IhpRepository {

    fun submitApproval(baseUrl: String, userId: String, levelUser: String, room: String, keterangan: String): Response {
        val responseData = Response()
        val namaDevice = "${android.os.Build.BRAND} ${android.os.Build.MODEL}"
        val client = ApiRestService.getClient(baseUrl).create(ApprovalClient::class.java)
        client.submitApproval(userId, levelUser, room, keterangan, namaDevice).enqueue(object :
            Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful){
                    responseData.message = response.message()
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
                    responseData.postValue(Response(false, t.message.toString()))
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
                    responseData.postValue(Response(false, t.message.toString()))
                }
            })
        return responseData
    }

    fun getJumlahApproval(baseUrl: String, username: String):LiveData<Response>{
        val responseData = MutableLiveData<Response>()
        val client = ApiRestService.getClient(baseUrl).create(ApprovalClient::class.java)
        client.jumlahApproval(username).enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful){
                    responseData.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<Response>, t: Throwable) {
                    responseData.postValue(Response(false, t.message.toString()))
            }
        })
        return responseData
    }

    fun getCancelItems(baseUrl: String): LiveData<CancelItemsResponse>{
        val responseData = MutableLiveData<CancelItemsResponse>()
        val client =  ApiRestService.getClient(baseUrl).create(ReportClient::class.java)
        client.getCancelItems().enqueue(object: Callback<CancelItemsResponse>{
            override fun onResponse(call: Call<CancelItemsResponse>, response: retrofit2.Response<CancelItemsResponse>
            ) {
                if (response.isSuccessful) {
                    responseData.postValue(response.body())
                } else{
                    responseData.postValue(CancelItemsResponse(false, mutableListOf(), "Gagal MengambilData"))
                }
            }
            override fun onFailure(call: Call<CancelItemsResponse>, t: Throwable) {
                responseData.postValue(CancelItemsResponse(false, mutableListOf(), t.message.toString()))
            }
        })
        return responseData
    }

    fun getSaleItemsByName(url: String, waktu: String, namaItem: String, user: String):LiveData<SaleItemsbyNameResponse>{
        val responseData = MutableLiveData<SaleItemsbyNameResponse>()
        val client = ApiRestService.getClient(url).create(ReportClient::class.java)
        client.getSalesItemByName(waktu, namaItem, user).enqueue(object: Callback<SaleItemsbyNameResponse>{
            override fun onResponse(
                call: Call<SaleItemsbyNameResponse>,
                response: retrofit2.Response<SaleItemsbyNameResponse>
            ) {
                if (response.isSuccessful){
                    responseData.postValue(response.body())
                } else{
                    responseData.postValue(SaleItemsbyNameResponse(mutableListOf(), 0, false, "Gagal Mengambil data"))
                }
            }

            override fun onFailure(call: Call<SaleItemsbyNameResponse>, t: Throwable) {
                responseData.postValue(SaleItemsbyNameResponse(mutableListOf(), 0, false, t.message))
            }
        })

        return responseData
    }

    fun printKas(baseUrl: String, tanggal: String, shift: String, username: String, context: Context){
        val client = ApiRestService.getClient(baseUrl).create(ReportClient::class.java)
        client.printKas(tanggal, shift, username).enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                    Toasty.info(context, response.message(), Toasty.LENGTH_SHORT, true).show()
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toasty.error(context, t.message.toString(), Toasty.LENGTH_SHORT, true).show()
            }

        })
    }
}