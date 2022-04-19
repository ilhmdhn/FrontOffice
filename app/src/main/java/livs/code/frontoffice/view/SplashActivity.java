package livs.code.frontoffice.view;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.sio.SioBackgroundService;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.init_splash)
    MKLoader _pgrrsLoad;
    User userlogin;

    private SioBackgroundService mYourService;
    private Intent mServiceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


        mYourService = new SioBackgroundService();
        mServiceIntent = new Intent(getBaseContext(), mYourService.getClass());

        if (isMyServiceRunning(mYourService.getClass())) {
            stopService(mServiceIntent);
        }

        _pgrrsLoad.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                _pgrrsLoad.setVisibility(View.GONE);

                if(null!=((MyApp) getApplicationContext()).getUserFo()){
                    userlogin = ((MyApp) getApplicationContext()).getUserFo();
                    if(userlogin.isLogin()){
                        toMainAct();
                    }else{
                        toLoginAct();
                    }
                }else {
                    toLoginAct();
                }
            }
        }, 3*1000);

    }

    private void toMainAct() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void toLoginAct() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG, "Background Service status Running");
                return true;
            }
        }
        Log.i(TAG, "Background Service status Not running");
        return false;
    }


}
