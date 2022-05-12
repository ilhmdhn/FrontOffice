package livs.code.frontoffice.view.fragment.reporting

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.dmoral.toasty.Toasty
import livs.code.frontoffice.data.remote.ApiRestService
import livs.code.frontoffice.data.remote.ReportClient
import livs.code.frontoffice.data.remote.respons.ListUser
import livs.code.frontoffice.data.remote.respons.StatusKasResponse
import livs.code.frontoffice.data.remote.respons.UserListResponse
import livs.code.frontoffice.data.repository.IhpRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportViewModel: ViewModel() {

    val ihpRepository =  IhpRepository()

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

    fun getStatusKas(baseUrl: String, tanggal: String, shift: String, username:  String): LiveData<StatusKasResponse>{
        return ihpRepository.getReportKas(baseUrl, tanggal, shift,  username)
    }
}