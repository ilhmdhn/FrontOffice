package com.ihp.frontoffice.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.User;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.init_splash)
    MKLoader _pgrrsLoad;
    User userlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

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

}