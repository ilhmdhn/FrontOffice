package com.ihp.frontoffice.data.repository

import android.content.Context
import android.printservice.PrintDocument
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.data.model.CancelOrderModel
import com.ihp.frontoffice.data.model.OrderInventory
import com.ihp.frontoffice.data.model.RequestData
import es.dmoral.toasty.Toasty
import com.ihp.frontoffice.data.remote.*
import com.ihp.frontoffice.data.remote.respons.*
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.view.fragment.operasional.fnb.order.FnbPagingSource
import retrofit2.Call
import retrofit2.Callback

class IhpRepository {

    fun submitApproval(baseUrl: String, userId: String, levelUser: String, room: String, keterangan: String): Response {
        val responseData = Response()
        val namaDevice = "${android.os.Build.BRAND} ${android.os.Build.MODEL}"
        val client = ApiRestService.getClient(baseUrl).create(ApprovalClient::class.java)
        client.submitApproval(userId, levelUser, room, keterangan, namaDevice).enqueue(object :
            Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful){
                    responseData.message = response.body()!!.message
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
                val message: String
                if(response.body() == null){
                    message = "Server tidak merespon"
                }else {
                    message =  response.body()!!.message
                }
                Toasty.info(context, message, Toasty.LENGTH_SHORT, true).show()
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toasty.error(context, t.message.toString(), Toasty.LENGTH_SHORT, true).show()
            }

        })
    }

    fun printBill(url: String, rcp: String, user: String, context: Context):Boolean{
        var hasil = false
        val client = ApiRestService.getClient(url).create(PrintClient::class.java)
        client.printTagihan(rcp, user).enqueue(object:Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful && response.body() != null) {
                    Toasty.info(context, "Server Response "+ response.body()!!.message, Toasty.LENGTH_SHORT).show()
                    if (response.body()!!.state==true){
                        hasil = true
                    }else{
                        hasil = false
                    }
                } else{
                    Toasty.error(context,"Error " , Toasty.LENGTH_SHORT, true).show()
                    hasil = false;
                }
            }
            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toasty.error(context,"Error " + t.message.toString(), Toasty.LENGTH_SHORT, true).show()
                hasil = false
            }
        })
        return hasil
    }

    fun printInvoice(url: String, rcp: String, context: Context) {
        val client = ApiRestService.getClient(url).create(PrintClient::class.java)
        client.printInvoice(rcp).enqueue(object:Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful && response.body() != null) {
                    Toasty.info(context, "Server Response "+ response.body()!!.message, Toasty.LENGTH_SHORT).show()
                } else{
                    Toasty.error(context,"Error " , Toasty.LENGTH_SHORT, true).show()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toasty.error(context,"Gagal Print Invoice " + t.message.toString(), Toasty.LENGTH_SHORT, true).show()
            }
        })
    }

    fun printStatus(url: String, rcp: String):LiveData<PrintStatusResponse>{
        val dataResponse= MutableLiveData<PrintStatusResponse>()
        val client = ApiRestService.getClient(url).create(PrintClient::class.java)
        client.printStatus(rcp).enqueue(object: Callback<PrintStatusResponse>{
            override fun onResponse(call: Call<PrintStatusResponse>, response: retrofit2.Response<PrintStatusResponse>) {
                if(response.isSuccessful){
                    dataResponse.postValue(response.body())
                } else{
                    dataResponse.postValue(PrintStatusResponse(PrintStatusResult("5"),  false, "Gagal cek print status"))
                }
            }

            override fun onFailure(call: Call<PrintStatusResponse>, t: Throwable) {
                dataResponse.postValue(PrintStatusResponse(PrintStatusResult("5"),  false, t.message.toString()))
            }
        })
        return dataResponse
    }

    fun insertToken(context: Context,url: String, token: String, user: String?, levelUser: String?){
        val client = ApiRestService.getClient(url).create(FirebaseClient::class.java)
        client.insertToken(token, user, levelUser).enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful){
                    Log.d("Token", response.message())
                }else{
                    Log.e("Token", response.message())
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(context, "Gagal insert token " +t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getRoomCall(url: String): LiveData<RoomCallResponse>{
        val responseData = MutableLiveData<RoomCallResponse>()
        val client = ApiRestService.getClient(url).create(RoomCallClient::class.java)
        client.getCallRoom().enqueue(object: Callback<RoomCallResponse>{
            override fun onResponse(call: Call<RoomCallResponse>, response: retrofit2.Response<RoomCallResponse>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<RoomCallResponse>, t: Throwable) {
                responseData.postValue(RoomCallResponse(false, mutableListOf(), t.message.toString()))
            }
        })

        return responseData
    }

    fun turnOffTOken(url: String, token: String){
        val client = ApiRestService.getClient(url).create(FirebaseClient::class.java)
        client.turnOffToken(token).enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Log.d("token", response.message())
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.e("error remove token", t.message.toString())
            }
        })
    }

    fun responseRoomCall(context: Context, room: String, state: Byte){
        val user = (context.applicationContext as MyApp).userFo.userId
        val url = (context.applicationContext as MyApp).baseUrl
        val client = ApiRestService.getClient(url).create(RoomCallClient::class.java)
        client.responseCallRoom(room, state, user).enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun printMobilePrintBill(url: String, room: String):LiveData<xBillResponse>{
        val responseData = MutableLiveData<xBillResponse>()
        val client = ApiRestService.getClient(url).create(DataPrintClient::class.java)
        client.getPrintBill(room).enqueue(object: Callback<xBillResponse>{
            override fun onResponse(call: Call<xBillResponse>,response: retrofit2.Response<xBillResponse>) {
                if (response.isSuccessful){
                    if (response.body()?.state == true){
                        responseData.postValue(response.body())
                    }
                }
            }

            override fun onFailure(call: Call<xBillResponse>, t: Throwable) {
                responseData.postValue(xBillResponse( null, false, t.message))
            }
        })
        return responseData
    }

    fun getViewBill(url: String, room: String):LiveData<xBillResponse>{
        val responseData = MutableLiveData<xBillResponse>()
        val client = ApiRestService.getClient(url).create(DataPrintClient::class.java)
        client.getViewBill(room).enqueue(object: Callback<xBillResponse>{
            override fun onResponse(call: Call<xBillResponse>,response: retrofit2.Response<xBillResponse>) {
                if (response.isSuccessful){
                        responseData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<xBillResponse>, t: Throwable) {
                responseData.postValue(xBillResponse( null, false, t.message))
            }
        })
        return responseData
    }

    fun printMobileInvoice(url: String, rcp: String): LiveData<xInvoiceResponse>{
        val responseData = MutableLiveData<xInvoiceResponse>()
        val client = ApiRestService.getClient(url).create(DataPrintClient::class.java)
        client.getPrintInvoice(rcp).enqueue(object: Callback<xInvoiceResponse>{
            override fun onResponse(call: Call<xInvoiceResponse>, response: retrofit2.Response<xInvoiceResponse>) {
                if (response.isSuccessful){
                    responseData.postValue(response.body())
                }else{
                    responseData.postValue(xInvoiceResponse(state = false))
                }
            }

            override fun onFailure(call: Call<xInvoiceResponse>, t: Throwable) {
                    responseData.postValue(xInvoiceResponse(null, false, t.message))
            }
        })
        return responseData
    }

    fun updateStatusPrint(url: String, rcp: String, statusPrint: String, context: Context){
        val client = ApiRestService.getClient(url).create(DataPrintClient::class.java)
        client.updateStatusPrint(rcp, statusPrint).enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful){
                    if (response.body()?.state == true){
                        Log.d("print status update", "SUCCESS")
                    }
                }else{
                    response.body()?.message?.let { Toasty.warning(context, it, Toasty.LENGTH_SHORT,true).show() }
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                    Toasty.error(context, t.message.toString(), Toasty.LENGTH_SHORT, true).show()
            }
        })
    }

    fun removePromoFnB(url: String, rcp: String):LiveData<Response>{
        val client = ApiRestService.getClient(url).create(CheckinDirectClient::class.java)
        val responseData = MutableLiveData<Response>()
        client.removePromoFood(rcp).enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if(response.isSuccessful){
                        responseData.postValue(response.body())
                }else{
                    responseData.postValue(Response(false, "response failer"))
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                responseData.postValue(Response(false, t.message.toString()))
            }
        })
        return responseData
    }

    fun checkUser(url: String, email: String, password: String):LiveData<UserResponse>{
        val client = ApiRestService.getClient(url).create(UserClient::class.java)
        val responseData = MutableLiveData<UserResponse>()
        val responseFail = UserResponse()
        client.login(email,password).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, responses: retrofit2.Response<UserResponse>) {
                if(responses.isSuccessful){
                    responseData.postValue(responses.body())
                    Log.d("DEBUNGGING RESPONSE PURE", responses.body()?.user?.userId.toString())
                }else{
                    responseFail.isOkay = false
                    responseFail.message = "response failed"
                    responseData.postValue(responseFail)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                responseFail.isOkay = false
                responseFail.message = t.message.toString()
                responseData.postValue(responseFail)
            }
        });
        return responseData;
    }

    fun checkinSlip(url: String, rcp: String):LiveData<CheckinSlipResponse>{
        val client = ApiRestService.getClient(url).create(DataPrintClient::class.java)
        val responseData = MutableLiveData<CheckinSlipResponse>()
        client.getCheckinSlip(rcp).enqueue(object: Callback<CheckinSlipResponse>{
            override fun onResponse(call: Call<CheckinSlipResponse>, response: retrofit2.Response<CheckinSlipResponse>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<CheckinSlipResponse>, t: Throwable) {
                responseData.postValue(CheckinSlipResponse(state = false, message = t.message))
            }
        })
        return responseData
    }

    fun getFnbPaging(category: String, search: String, apiService: InventoryClient): LiveData<PagingData<DataInventoryPaging>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 10
                ),
                pagingSourceFactory = {
                    FnbPagingSource(apiService, category, search)
                }
        ).liveData
    }

    fun sendOrder(url: String,chusr: String, roomCode: String, rcp: String, roomType: String, checkinDuration: String,  order: ArrayList<DataBusEvent.OrderModel>): LiveData<Response>{
        val client = ApiRestService.getClient(url).create(InventoryClient::class.java)
        val responseData = MutableLiveData<Response>()
        val orderinv= ArrayList<String>()
        val orderQty = ArrayList<String>()
        val orderNotes = ArrayList<String>()
        val orderPrice = ArrayList<String>()
        val ordername = ArrayList<String>()
        val orderLocation = ArrayList<String>()
        for (i in order){
            orderinv.add(i.inventoryCode)
            orderQty.add(i.orderQty.toString())
            orderNotes.add(i.orderNotes)
            orderPrice.add(i.itemPrice.toString())
            ordername.add(i.itemName)
            orderLocation.add(i.itemLocation)
        }

        client.sendOrder(chusr, roomCode, rcp, roomType, checkinDuration, "", "", orderinv, orderQty, orderNotes, orderPrice, ordername, orderLocation).enqueue(object : Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                responseData.postValue(response.body()?.let { Response(state = it.state, message = it.message, isLoading = false) })
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                responseData.postValue(Response(state = false, message = "Cek order masuk " +t.message.toString(), isLoading = false, isBack = true))
            }
        })
        return responseData
    }

    fun getRoomOrder(url: String, roomCode: String): LiveData<OrderResponse>{
        val client = ApiRestService.getClient(url).create(OrderClient::class.java)
        val responseData = MutableLiveData<OrderResponse>()

        client.getOrderRoom(roomCode).enqueue(object : Callback<OrderResponse>{
            override fun onResponse(call: Call<OrderResponse>, response: retrofit2.Response<OrderResponse>) {
                responseData.postValue(OrderResponse(
                        isLoading = false,
                        state = response.body()?.state,
                        message = response.body()?.message,
                        data = response.body()?.data ?: mutableListOf()
                ))
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                responseData.postValue(OrderResponse(
                        isLoading = false,
                        state = false,
                        message = t.message
                ))
            }
        })
        return responseData
    }

    fun cancelOrder(url: String, so: String, inventoryCode: String, qty: String, rcp: String, user: String): LiveData<Response>{
        val client = ApiRestService.getClient(url).create(OrderClient::class.java)
        val responseData = MutableLiveData<Response>()
        val deviceName = MyApp().getDeviceName()
        client.cancelOrder(so, inventoryCode, qty, rcp, user, deviceName.toString()).enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                responseData.postValue(Response(state = false, message = t.message.toString()))
            }
        })
        return responseData
    }

    fun revisiOrder(url: String, so: String, inventoryCode: String, note: String, qty: String, qtyTemp: String, rcp: String, user: String):LiveData<Response>{
        val client = ApiRestService.getClient(url).create(OrderClient::class.java)
        val responseData = MutableLiveData<Response>()
        val deviceName = MyApp().getDeviceName()

        client.revisiOrder(so, inventoryCode, note, qty, qtyTemp, rcp, user, deviceName.toString()).enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                responseData.postValue(Response(state = false, message = t.toString()))
            }
        })
        return responseData
    }

    fun getOrderOldRoom(url: String, ivc: String): LiveData<OrderBeforeTransferResponse>{
        val client = ApiRestService.getClient(url).create(OrderClient::class.java)
        val responseData = MutableLiveData<OrderBeforeTransferResponse>()

        client.getOldOrder(ivc).enqueue(object : Callback<OrderBeforeTransferResponse>{
            override fun onResponse(call: Call<OrderBeforeTransferResponse>, response: retrofit2.Response<OrderBeforeTransferResponse>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<OrderBeforeTransferResponse>, t: Throwable) {
                responseData.postValue(OrderBeforeTransferResponse(state = false, message = t.message.toString(), data = mutableListOf(), length = 0))
            }

        })
        return responseData
    }

    fun userLogin(url: String, user: String, password: String):LiveData<UserResponse>{
        val client = ApiRestService.getClient(url).create(UserClient::class.java)
        val responseData = MutableLiveData<UserResponse>()

        client.login(user, password).enqueue(object: Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: retrofit2.Response<UserResponse>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                responseData.postValue(UserResponse())
            }
        })
        return responseData
    }

    fun cancelOld(url: String, user: String, room: String, rcpOld: String, cancelList: List<CancelOrderModel>): LiveData<Response>{
        val client = ApiRestService.getClient(url).create(OrderClient::class.java)
        val responseData = MutableLiveData<Response>()

        val orderInventory = OrderInventory(
                cancelList[0].inventory,
                cancelList[0].nama,
                cancelList[0].order_penjualan,
                cancelList[0].qty.toInt(),
                cancelList[0].slip_order,
        )

        val requestData = RequestData(
                user,
                room,
                rcpOld,
                listOf(orderInventory)
        )

        client.cancelOrder(requestData).enqueue(object : Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                responseData.postValue(Response(false, t.message.toString()))
            }
        })
        return responseData
    }

    fun latestSoCode(url: String, rcp: String): LiveData<StringResponse>{
        val client = ApiRestService.getClient(url).create(DataPrintClient::class.java)
        val responseData = MutableLiveData<StringResponse>()

        client.getLatestSoCode(rcp).enqueue(object: Callback<StringResponse>{
            override fun onResponse(call: Call<StringResponse>, response: retrofit2.Response<StringResponse>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                responseData.postValue(StringResponse(message = t.message.toString()))
            }
        })
        return responseData
    }

    fun listSol(url: String, sol: String): LiveData<ListSolResponse>{
        val client = ApiRestService.getClient(url).create(DataPrintClient::class.java)
        val responseData = MutableLiveData<ListSolResponse>()

        client.getListSol(sol).enqueue(object: Callback<ListSolResponse>{
            override fun onResponse(call: Call<ListSolResponse>, response: retrofit2.Response<ListSolResponse>) {
                responseData.postValue(response.body())
            }

            override fun onFailure(call: Call<ListSolResponse>, t: Throwable) {
                responseData.postValue(ListSolResponse(state = false, message = t.message.toString()))
            }

        })
        return responseData
    }
}