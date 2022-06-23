package livs.code.frontoffice.view.fragment.reporting

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.dmoral.toasty.Toasty
import livs.code.frontoffice.data.remote.ApiRestService
import livs.code.frontoffice.data.remote.ReportClient
import livs.code.frontoffice.data.remote.StatusKasClient
import livs.code.frontoffice.data.remote.respons.*
import livs.code.frontoffice.data.repository.IhpRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportViewModel: ViewModel() {

    val ihpRepository =  IhpRepository()
    private val _statusKas = MutableLiveData<StatusKasResponse>()
    val statusKas: LiveData<StatusKasResponse> = _statusKas;

    private val _salesToday = MutableLiveData<MySalesResponse>()
    val salesToday: LiveData<MySalesResponse> = _salesToday

    private val _salesWeekly = MutableLiveData<MySalesResponse>()
    val salesWeekly: LiveData<MySalesResponse> = _salesWeekly

    private val _salesMonthly = MutableLiveData<MySalesResponse>()
    val salesMonthly: LiveData<MySalesResponse> = _salesMonthly

    private val _saleItemsToday = MutableLiveData<SalesItemListResponse>()
    val saleItemToday: LiveData<SalesItemListResponse> = _saleItemsToday

    private val _saleItemWeekly = MutableLiveData<SalesItemListResponse>()
    val saleItemWeekly: LiveData<SalesItemListResponse> = _saleItemWeekly

    private val _saleItemMonthly = MutableLiveData<SalesItemListResponse>()
    val saleItemMonthly: LiveData<SalesItemListResponse> = _saleItemMonthly

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStatusKas(baseUrl: String, tanggal: String, shift: String, username: String){
        val client = ApiRestService.getClient(baseUrl).create(StatusKasClient::class.java)
        client.getStatusKasReport(tanggal, shift, username).enqueue(object: Callback<StatusKasResponse>{
            override fun onResponse(call: Call<StatusKasResponse>, response: Response<StatusKasResponse>) {
                _statusKas.postValue(response.body())
            }

            override fun onFailure(call: Call<StatusKasResponse>, t: Throwable) {
                _statusKas.postValue(StatusKasResponse(DataStatusKas(), false, t.message.toString()))
            }
        })
    }

    fun getUser(baseUrl: String, levelUser: String, context: Context): LiveData<List<ListUser>>{
        val resultUser = MutableLiveData<List<ListUser>>()
        val client = ApiRestService.getClient(baseUrl).create(ReportClient::class.java)
        client.getListUser(levelUser).enqueue(object : Callback<UserListResponse>{
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                if (response.isSuccessful && response.body()?.data != null){
                    resultUser.postValue(response.body()?.data as List<ListUser>?)
                }else{
                    Toasty.info(context, "User Kosong", Toast.LENGTH_SHORT, true).show()
                    resultUser.postValue(mutableListOf())
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Toasty.error(context,"On Failure : " + t.message,Toast.LENGTH_SHORT,true).show()
            }
        })
        return resultUser
    }

    fun getCashDetail(baseUrl: String, tanggal: String, shift: String):LiveData<PecahanUangResponse>{
        return ihpRepository.getCashDetail(baseUrl, tanggal, shift)
    }

    fun postCashDetail(baseUrl: String, tanggal: String, shift: String, pecahanUang: PecahanUang): LiveData<livs.code.frontoffice.data.remote.respons.Response>{
        return ihpRepository.postCashDetail(baseUrl, tanggal, shift, pecahanUang)
    }

    fun updateCashDetail(baseUrl: String, tanggal: String, shift: String, pecahanUang: PecahanUang): LiveData<livs.code.frontoffice.data.remote.respons.Response>{
        return ihpRepository.updateCashDetail(baseUrl, tanggal, shift, pecahanUang)
    }

    fun getSalesToday(url: String, username: String){
        _isLoading.value = true
        val client = ApiRestService.getClient(url).create(ReportClient::class.java)
        client.getSalesToday(username).enqueue(object: Callback<MySalesResponse>{
            override fun onResponse(call: Call<MySalesResponse>,response: Response<MySalesResponse>) {
                _isLoading.value = false
                _salesToday.postValue(response.body())
            }

            override fun onFailure(call: Call<MySalesResponse>, t: Throwable) {
                _isLoading.value = false
                _salesToday.postValue(MySalesResponse(mutableListOf(), 0, false, t.message.toString()))
            }
        })
    }

    fun getSalesWeekly(url: String, username: String){
        _isLoading.value = true
        val client = ApiRestService.getClient(url).create(ReportClient::class.java)
        client.getSalesWeekly(username).enqueue(object: Callback<MySalesResponse>{
            override fun onResponse(call: Call<MySalesResponse>,response: Response<MySalesResponse>) {
                _isLoading.value = false
                _salesWeekly.postValue(response.body())
            }

            override fun onFailure(call: Call<MySalesResponse>, t: Throwable) {
                _isLoading.value = false
                _salesWeekly.postValue(MySalesResponse(mutableListOf(), 0, false, t.message.toString()))
            }
        })
    }

    fun getSalesMonthly(url: String, username: String){
        _isLoading.value = true
        val client = ApiRestService.getClient(url).create(ReportClient::class.java)
        client.getSalesMonthly(username).enqueue(object: Callback<MySalesResponse>{
            override fun onResponse(call: Call<MySalesResponse>,response: Response<MySalesResponse>) {
                _isLoading.value = false
                _salesMonthly.postValue(response.body())
            }

            override fun onFailure(call: Call<MySalesResponse>, t: Throwable) {
                _isLoading.value = false
                _salesMonthly.postValue(MySalesResponse(mutableListOf(), 0, false, t.message.toString()))
            }
        })
    }

    fun getSaleItemToday(url: String, chusr: String){
        _isLoading.value = true
        val client = ApiRestService.getClient(url).create(ReportClient::class.java)
        client.getSaleItems("dialy", chusr).enqueue(object: Callback<SalesItemListResponse>{
            override fun onResponse(call: Call<SalesItemListResponse>,response: Response<SalesItemListResponse>) {
                _isLoading.value = false
                _saleItemsToday.postValue(response.body())
            }

            override fun onFailure(call: Call<SalesItemListResponse>, t: Throwable) {
                _isLoading.value = false
                _saleItemsToday.postValue(SalesItemListResponse(mutableListOf(), 0, false, t.message.toString()))
            }
        })
    }

    fun getSaleItemWeekly(url: String, chusr: String){
        _isLoading.value = true
        val client = ApiRestService.getClient(url).create(ReportClient::class.java)
        client.getSaleItems("weekly", chusr).enqueue(object: Callback<SalesItemListResponse>{
            override fun onResponse(call: Call<SalesItemListResponse>,response: Response<SalesItemListResponse>) {
                _isLoading.value = false
                _saleItemWeekly.postValue(response.body())
            }

            override fun onFailure(call: Call<SalesItemListResponse>, t: Throwable) {
                _isLoading.value = false
                _saleItemWeekly.postValue(SalesItemListResponse(mutableListOf(), 0, false, t.message.toString()))
            }
        })
    }

    fun getSaleItemMonthly(url: String, chusr: String){
        _isLoading.value = true
        val client = ApiRestService.getClient(url).create(ReportClient::class.java)
        client.getSaleItems("monthly", chusr).enqueue(object: Callback<SalesItemListResponse>{
            override fun onResponse(call: Call<SalesItemListResponse>,response: Response<SalesItemListResponse>) {
                _isLoading.value = false
                _saleItemMonthly.postValue(response.body())
            }

            override fun onFailure(call: Call<SalesItemListResponse>, t: Throwable) {
                _isLoading.value = false
                _saleItemMonthly.postValue(SalesItemListResponse(mutableListOf(), 0, false, t.message.toString()))
            }
        })
    }

    fun getCancelItems(url: String): LiveData<CancelItemsResponse>{
        return  ihpRepository.getCancelItems(url)
    }

    fun getSaleperItems(url: String, waktu: String, namaItem: String, user: String): LiveData<SaleItemsbyNameResponse>{
        return ihpRepository.getSaleItemsByName(url, waktu, namaItem, user)
    }
}