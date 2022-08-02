package livs.code.frontoffice;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import livs.code.frontoffice.data.entity.Config;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.local.FrontOfficeDatabase;
import livs.code.frontoffice.helper.NetworkUtil;

public class MyApp extends Application {

    //init all global data
    public static FrontOfficeDatabase frontOfficeDatabase;
    private  String baseUrl;
    private  User userFo;
    private String TAG = "MyApp";
    private static boolean wifiConnect;

    @Override
    public void onCreate() {
        super.onCreate();
        frontOfficeDatabase = FrontOfficeDatabase.getInstance(this.getApplicationContext());
        setBaseUrl();
        setUserLogin();

        if (!isWifiConnect(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi wifi ", Toast.LENGTH_LONG).show();
        }
    }

    private void setUserLogin() {
        try{
            this.userFo = frontOfficeDatabase.userDao().getUserLogin();
        }catch (Exception x){
            Log.e(TAG,x.getMessage());
        }
    }

    public void setBaseUrl(){
	    try{
            Config cfg = frontOfficeDatabase.configDao().getLastConfig();
            baseUrl = cfg.getBaseURL();
        }catch (Exception x){
	        Log.e(TAG,x.getMessage());
	        Toast
                    .makeText(this.getApplicationContext(),"Konfigurasi server belum ada",Toast.LENGTH_SHORT)
                    .show();
            baseUrl = "";
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public User getUserFo() {
        return userFo;
    }

    public void setUserFo(User userFo) {
        this.userFo = userFo;
    }

    @Override
    public String toString() {
        return "FrontOffice";
    }

    public static void setWifiConnect(boolean wifiConnect) {
        MyApp.wifiConnect = wifiConnect;
    }

    public static boolean isWifiConnect(Context ctx) {
        int wifiStatusNya = NetworkUtil.getConnectivityStatusString(ctx);
        if ((wifiStatusNya == 0) | (wifiStatusNya == 2)) {
            wifiConnect = false;
        } else {
            wifiConnect = true;
        }
        return wifiConnect;
    }
}