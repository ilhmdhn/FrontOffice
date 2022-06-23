package livs.code.frontoffice.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.irozon.library.HideKey;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.local.FrontOfficeDatabase;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.UserClient;
import livs.code.frontoffice.data.remote.respons.UserResponse;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.input_username)
    EditText _usernameTxt;
    @BindView(R.id.input_password)
    EditText _passwordTxt;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.btn_setting)
    Button _settingButton;
    @BindView(R.id.login_progress)
    MKLoader _loginProgress;
    @BindView(R.id.txt_version_app)
    TextView _tvVersion;

    int i = 0;


    FrontOfficeDatabase db = MyApp.frontOfficeDatabase;
    private boolean isOut = false;

    private static String BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        HideKey.initialize(this);
        BASE_URL = ((MyApp) getApplicationContext()).getBaseUrl();

       /* _usernameTxt.setText("AIN");
        _passwordTxt.setText("AI1");*/
        _loginButton.setOnClickListener(v -> {
            if (!isBaseURLset()) {
                return;
            }
            String email = _usernameTxt.getText().toString();
            String password = _passwordTxt.getText().toString();
            if (validate()) {
                _loginProgress.setVisibility(View.VISIBLE);
                BASE_URL = ((MyApp) getApplicationContext()).getBaseUrl();
                ApiRestService.initBaseUrl(BASE_URL);

                UserClient userClient = ApiRestService.getClient(BASE_URL).create(UserClient.class);
                Call<UserResponse> call = userClient.login(email, password);
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                        UserResponse res = response.body();
                        if (!res.isOkay()) {
                            res.displayMessage(getApplicationContext());
                            _loginProgress.setVisibility(View.GONE);
                            return;
                        }
                        saveToDb(res.getUser());
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT, true).show();
                        _loginProgress.setVisibility(View.GONE);
                    }
                });
            } else {
                Toast
                        .makeText(getApplicationContext(),
                                "Pastikan sudah input user dan password",
                                Toast.LENGTH_SHORT)
                        .show();
            }


        });

        _settingButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
            startActivity(intent);
            finish();
        });


        _tvVersion.setOnClickListener(view ->{
            i = i + 1;
            int berapa = 3-i;
            if (i < 3){
                Toast.makeText(this, "Tekan "+ berapa +"x lagi untuk menampilkan konfigurasi", Toast.LENGTH_SHORT).show();
            }else{
                _settingButton.setVisibility(View.VISIBLE);
            }
        });


        /*if (!isOut) {
            this.selfLogin();
        }*/
        if (isBaseURLset()) {
            Toast
                    .makeText(this,
                            "Silahkan Login",
                            Toast.LENGTH_SHORT)
                    .show();
        }
        try {
            isOut = (boolean) getIntent().getSerializableExtra("OUT");
        } catch (Exception e) {

        }

    }

    private void saveToDb(User user) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new InsertUser(user).execute();
                _loginProgress.setVisibility(View.GONE);
            }
        }, 1000);

    }

    private void navToMainActivity() {
        _loginProgress.setVisibility(View.GONE);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isBaseURLset()) {
            BASE_URL = ((MyApp) getApplicationContext()).getBaseUrl();
            Toast
                    .makeText(this,
                            BASE_URL,
                            Toast.LENGTH_LONG)
                    .show();
            _loginButton.setEnabled(true);
            Toast
                    .makeText(this,
                            "Silahkan Login",
                            Toast.LENGTH_SHORT)
                    .show();

        }
    }

    private boolean isBaseURLset() {
        if (((MyApp) getApplicationContext()).getBaseUrl() == "") {
            Toast
                    .makeText(this,
                            "Konfigurasi Server Belum Sesuai",
                            Toast.LENGTH_SHORT)
                    .show();
            //_loginButton.setEnabled(false);
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        try {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validate() {
        boolean valid = true;

        String email = _usernameTxt.getText().toString();
        String password = _passwordTxt.getText().toString();

        //if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        if (email.isEmpty()) {
            // _usernameTxt.setError("Enter a valid user");
            valid = false;
        } else {
            // _usernameTxt.setError(null);
        }

        //if (password.isEmpty() || password.length() < 4 || password.length() > 10)
        if (password.isEmpty()) {
            // _passwordTxt.setError("Enter a valid password");
            valid = false;
        } else {
            //_passwordTxt.setError(null);
        }

        return valid;
    }

    @Subscribe
    public void insertDbDone(EventsWrapper.DbProgressDone progressdb) {
        if (progressdb.isProgressDone()) {
            ((MyApp) getApplicationContext()).setUserFo(db.userDao().getUserLogin());
            Toast.makeText(getApplicationContext(), "User Save on DB", Toast.LENGTH_SHORT)
                    .show();
            navToMainActivity();
        } else {
            Toast.makeText(getApplicationContext(), "Insert DB Failure", Toast.LENGTH_SHORT)
                    .show();
            _loginProgress.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        GlobalBus.getBus().unregister(this);
        super.onStop();
    }

    private class InsertUser extends AsyncTask<User, Void, Boolean> {
        User user;

        InsertUser(User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(User... params) {
            db.userDao().setUserLogout();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            user.setLogin(true);
            db.userDao().insert(user);
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