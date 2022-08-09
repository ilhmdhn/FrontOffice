package com.ihp.frontoffice.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.Result;
import com.irozon.library.HideKey;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Config;
import com.ihp.frontoffice.data.local.FrontOfficeDatabase;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ConfigActivity extends AppCompatActivity {
    @BindView(R.id.button_scan_qrconfig)
    Button bttnScanQr;
    @BindView(R.id.button_save_config)
    Button bttnSaveConfig;
    @BindView(R.id.btn_config_to_login)
    Button bttnToLogin;
    @BindView(R.id.txt_server_ip)
    EditText serverIp;
    @BindView(R.id.txt_server_port)
    EditText serverPort;
    @BindView(R.id.txt_server_base_url)
    EditText serverBaseURL;
    @BindView(R.id.config_progress)
    MKLoader progressBar;
    FrontOfficeDatabase db = MyApp.frontOfficeDatabase;
    private ZXingScannerView mScannerView;
    private AlertDialog scanQrDialog;
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ButterKnife.bind(this);
        HideKey.initialize(this);

        if (((MyApp) getApplicationContext()).getBaseUrl().equals("")) {
            config = null;
        } else {
            config = db.configDao().getLastConfig();
            setValue(config.getServerIp(), config.getServerPort(), config.getBaseURL());
        }


        bttnScanQr.setOnClickListener(view -> {
            dialogConfigScanQR();
        });

        serverPort.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                setBaseUrl();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        serverIp.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                setBaseUrl();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        bttnSaveConfig.setOnClickListener(view -> {
            boolean statusHurufIp = false;
            int jumlahAngkaIpTitik = 0;
            boolean statusJumlahAngkaIpTitik = false;
            int jumlahTitikIp = 0;
            for (int a = 0; a < serverIp.length(); a++) {
                if (!String.valueOf(serverIp.getText().charAt(a)).equals(".")) {
                    jumlahAngkaIpTitik = jumlahAngkaIpTitik + 1;
                }
                if (String.valueOf(serverIp.getText().charAt(a)).equals(".")) {
                    jumlahTitikIp = jumlahTitikIp + 1;
                    if (jumlahAngkaIpTitik > 3) {
                        statusJumlahAngkaIpTitik = true;
                    }
                    jumlahAngkaIpTitik = 0;
                }
                if (String.valueOf(serverIp.getText().charAt(a)).matches(".*[a-z].*")) {
                    statusHurufIp = true;
                }
            }

            String ip = serverIp.getText().toString();
            String port = serverPort.getText().toString();
            setBaseUrl();
            String baseUrl = serverBaseURL.getText().toString();
            config = new Config(1, ip, port, baseUrl);
            if (ip.isEmpty() || port.isEmpty() || baseUrl.isEmpty() || null == config) {
                Toast.makeText(this, "Konfigurasi belum sesuai", Toast.LENGTH_LONG).show();
                return;
            } else if (statusHurufIp == true) {
                Toast.makeText(getApplicationContext(), " Ip Adress Server POS tidak boleh huruf hanya diperbolehkan angka dan titik", Toast.LENGTH_LONG).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new InsertConfig(config).execute();
                }
            }, 3 * 1000);


        });

        bttnToLogin.setOnClickListener(view -> {
            if (isBaseURLset()) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //serverBaseURL.setText("http://192.168.1.11:3000");

    }

    private void setValue(String ip, String port, String baseUrl) {
        serverIp.setText(ip);
        serverPort.setText(port);
        serverBaseURL.setText(baseUrl);
    }

    private boolean isBaseURLset() {
        if (((MyApp) getApplicationContext()).getBaseUrl() == "") {
            Toast
                    .makeText(this,
                            "Konfigurasi Server Belum Tersimpan",
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Subscribe
    public void insertDbDone(EventsWrapper.DbProgressDone progressdb) {
        if (progressdb.isProgressDone()) {
            config = db.configDao().getLastConfig();
            setValue(config.getServerIp(), config.getServerPort(), config.getBaseURL());
            ((MyApp) getApplicationContext()).setBaseUrl();
            Toast.makeText(getApplicationContext(), "Config Save on DB", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getApplicationContext(), "Insert DB Failure", Toast.LENGTH_SHORT)
                    .show();
        }
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        GlobalBus.getBus().unregister(this);
        super.onStop();
    }


    private void dialogConfigScanQR() {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
        LayoutInflater dialogInflater = this.getLayoutInflater();

        View dialogView = dialogInflater.inflate(R.layout.dialog_qrscan_config, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("");

        FrameLayout frameLayout = dialogView.findViewById(R.id.frame_layout_camera);

        AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);

        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                mScannerView.stopCamera();
                scanQrDialog.dismiss();
                try {
                    JSONObject objekSubject = new JSONObject(result.getText());
                    if (objekSubject.length() == 3) {
                        String ip = objekSubject.get("server").toString();
                        String port = objekSubject.get("port").toString();
                        String baseUrl = "http://" + ip + ":" + port;

                        setValue(ip, port, baseUrl);


                        //backActivity();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
        frameLayout.addView(mScannerView);

        scanQrDialog = dialogBuilder.create();
        scanQrDialog.setOnShowListener(dialogInterface -> {
            mScannerView.startCamera();


            buttonCancel.setOnClickListener(view -> {
                mScannerView.stopCamera();
                scanQrDialog.dismiss();
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper
                                .ScanResult(false, null)
                        );
            });
        });

        scanQrDialog.show();

    }

    public void setBaseUrl() {
        if (serverIp.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Data Server IP Kosong", Toast.LENGTH_SHORT).show();
        } else if (serverPort.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Data Server Port Kosong", Toast.LENGTH_SHORT).show();
        } else if ((serverIp.getText().toString().equals("")) && (serverPort.getText().toString().equals(""))) {
            Toast.makeText(getApplicationContext(), "Data Server IP dan Server Port Kosong", Toast.LENGTH_SHORT).show();
        } else if ((!serverIp.getText().toString().equals("")) && (!serverPort.getText().toString().equals(""))) {
            serverBaseURL.setText(
                    "http://" + serverIp.getText() + ":" + serverPort.getText()
            );
        }
    }

    private class InsertConfig extends AsyncTask<Config, Void, Boolean> {
        Config config;

        InsertConfig(Config config) {
            this.config = config;
        }

        @Override
        protected Boolean doInBackground(Config... params) {
            try {
                //db.configDao().clearConfig();
                db.configDao().insert(config);
            } catch (Exception e) {
                Log.e("Error Insert DB", e.getMessage());
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            GlobalBus
                    .getBus()
                    .post(new EventsWrapper.DbProgressDone(success));
        }


    }
}