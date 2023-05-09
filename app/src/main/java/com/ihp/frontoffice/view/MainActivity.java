package com.ihp.frontoffice.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.Result;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.repository.IhpRepository;
import com.ihp.frontoffice.data.repository.LocalRepository;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.PreferenceUi;
import com.ihp.frontoffice.helper.QRScanType;
import com.ihp.frontoffice.viewmodel.OtherViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skyfishjy.library.RippleBackground;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity{


    private static final String TAG = MainActivity.class.getSimpleName();

    BottomNavigationView bottomNavigationView;


    private int navItemIndex = 0;
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";


    private boolean isPortraitsLayout;
    private boolean mUserLearnedDrawer;
    private IhpRepository ihpRepository;
    private String notifToken;

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private TextView textNotifyCount;
    private int notifyCount = 0;

    private boolean currentStateInstance;
    private LocalRepository localRepository;

    protected PowerManager.WakeLock mWakeLock;
    private User USER_FO;
    private String BASE_URL;
    private Handler handler;
    private static String CURRENT_PAGE = "";
    private final static String NOTIFICATION_PAGE = "NOTIFICATION_PAGE";
    private final static String OPERASIONAL_PAGE = "OPERASIONAL_PAGE";
    private boolean isLogout = false;
    private ZXingScannerView mScannerView;
    private AlertDialog scanQrDialog;

    private OtherViewModel otherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BASE_URL = ((MyApp) getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getApplicationContext()).getUserFo();
        final PowerManager pm = (PowerManager) getSystemService(getApplicationContext().POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
        this.mWakeLock.acquire();
        ihpRepository = new IhpRepository();
        otherViewModel = new ViewModelProvider(this).get(OtherViewModel.class);

        insertToken();

        otherViewModel.getLoginStatus(getApplicationContext()).observe(this, data->{
        if (data == null || !data.isLogin()){
            getLogout();
        }
        });

        handler = new Handler();
//        if (toolbar != null) {
//            setToolbarActivity();
//        }

        mUserLearnedDrawer = Boolean
                .parseBoolean(PreferenceUi
                        .readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));


        if (savedInstanceState != null) {
            navItemIndex = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            currentStateInstance = true;
        }


        if (findViewById(R.id.ll_potrait) != null) {
            //Phone layout

            isPortraitsLayout = true;
            bottomNavigationView = findViewById(R.id.nav_view_bottom);

            bottomNavigationView.setItemIconTintList(null);
            if (!mUserLearnedDrawer) {
                mUserLearnedDrawer = true;
                PreferenceUi.saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
            }

        } else {
            //Tablet layout
            isPortraitsLayout = false;

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getWindow()
//                        .setStatusBarColor(ContextCompat
//                                .getColor(this, R.color.colorPrimaryDark));
//            }

//            mNavigationView = findViewById(R.id.nav_view_side);
        }


        mAppBarConfiguration = new AppBarConfiguration
                .Builder(
                R.id.navOperasionalFragment,
                R.id.navRoomStatusFragment,
                R.id.navListHistoryRoomFragment,
                R.id.navListRoomFragment,
                R.id.navListInventoryFragment,
                R.id.navReportingFragment)
                .build();


        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            appBarLayout.setExpanded(true, true);
            CURRENT_PAGE = "";
            switch (destination.getId()) {
                case R.id.navOperasionalFragment:
                    showBottomNav();
//                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    CURRENT_PAGE = OPERASIONAL_PAGE;
                    break;
                case R.id.navReportFragment:
                case R.id.navRoomStatusFragment:
                case R.id.navListRoomFragment:
                case R.id.navListInventoryFragment:
                    showBottomNav();
//                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    break;
                case R.id.navNotificationFragment:
                    hideBottomNav();
                    CURRENT_PAGE = NOTIFICATION_PAGE;
//                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
                    break;
                case R.id.navOperasionalFnbFragment:
                case R.id.navDetailRoomFragment:
                case R.id.navPaymentFragment:
                case R.id.navOperasionalListRoomTypeToCheckinFragment:
                case R.id.navOperasionalCheckinAvailableRoomFragment:
                case R.id.navOperasionalReservasiFragment:
                case R.id.navOperasionalCheckinEditInfoFragment:
                case R.id.navOperasionalListRoomToEditInfoFragment:
                case R.id.navOperasionalListRoomToExtendFragment:
                case R.id.navOperasionalListRoomToPaymentFragment:
                case R.id.navOperasionalListRoomToFnbFragment:
                case R.id.navOperasionalListRoomToCheckoutFragment:
                case R.id.navOperasionalListRoomToTransferFragment:
                case R.id.navOperasionalRoomTypeToTransferFragment:
                case R.id.navOperasionalListRoomToCleanFragment:
                case R.id.navOperasionalInvoiceAndPaymentFragment:
                    hideBottomNav();
//                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
                    break;
                case R.id.navCheckinRoomFragment:
                    hideBottomNav();
                    break;
//                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                case R.id.navExtendsRoomFragment:
                    hideBottomNav();
//                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    break;
                case R.id.navTransferRoomFragment:
                    hideBottomNav();
//                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    break;
                case R.id.navReportingFragment:
                    showBottomNav();
//                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    break;
                case R.id.navStatusKasFragment:
                    hideBottomNav();
//                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    break;
                case R.id.mySalesReportParentFragment:
                    hideBottomNav();
                    break;
                case R.id.itemSalesFragment:
                    hideBottomNav();
                    break;
                case R.id.cancelReportFragment:
                    hideBottomNav();
                    break;
                default:
                    break;
            }
        });

//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        if (isPortraitsLayout) {
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            String fromNotif = intent.getStringExtra("FromPushNotify");
            if (fromNotif != null || fromNotif.equals("ROOM_CALL")){
                if (!CURRENT_PAGE.equals(NOTIFICATION_PAGE)) {
                    navController.navigate(R.id.navNotificationFragment);
                }
            }
        }

        localRepository = LocalRepository.getInstance(getApplicationContext());
        checkPermission();
    }

    private void insertToken(){
        //        token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Gagal mendapatkan token notifikasi", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String token = task.getResult();
                        Log.d("tokennya", token);
                        notifToken = token;
                        ihpRepository.insertToken(MainActivity.this,BASE_URL, token, USER_FO.getUserId(), USER_FO.getLevelUser());
                    }
                });
    }

    private void checkPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isPortraitsLayout = false;
        } else {
            isPortraitsLayout = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, navItemIndex);
        outState.putBoolean("TWO_PANE", isPortraitsLayout);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isPortraitsLayout = savedInstanceState.getBoolean("TWO_PANE");
        navItemIndex = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        final MenuItem menuItem = menu.getItem(0);
        menuItem.setActionView(R.layout.notify_badge_layout);
        View actionView = menuItem.getActionView();
        textNotifyCount = actionView.findViewById(R.id.notification_count);

        setupNotifyBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh_menu:
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper.RefreshCurrentFragment(true));
                break;

            case R.id.notification_menu:
                navController.navigate(R.id.navNotificationFragment);
                break;

            case R.id.setting_menu:
                navController.navigate(R.id.navSettingFragment);
                break;

            case R.id.logout_menu:
                logOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLogout(){
        Toast.makeText(this, "Silahkan Login Kembali", Toast.LENGTH_SHORT).show();
        handler.postDelayed(
                new Runnable() {
                    public void run() {
                        ihpRepository.turnOffTOken(BASE_URL, notifToken);
                        isLogout = true;
                        localRepository.setUserLogout();
                        Intent moveToMain = new Intent(MainActivity.this, LoginActivity.class);
                        moveToMain.putExtra("OUT", true);
                        moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToMain);
                        finish();
                    }
                }, 1000);
    }

    private void logOut() {
        new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogDarkTheme)
                .setTitle("Log Out")
                .setMessage("Anda ingin keluar aplikasi?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        handler.postDelayed(
                                new Runnable() {
                                    public void run() {
                                        ihpRepository.turnOffTOken(BASE_URL, notifToken);
                                        isLogout = true;
                                        localRepository.setUserLogout();
                                        Intent moveToMain = new Intent(MainActivity.this, LoginActivity.class);
                                        moveToMain.putExtra("OUT", true);
                                        moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(moveToMain);
                                        finish();
                                    }
                                }, 1000);


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

    }


    private void setupNotifyBadge() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textNotifyCount != null) {
                    if (notifyCount == 0) {
                        if (textNotifyCount.getVisibility() != View.GONE) {
                            textNotifyCount.setVisibility(View.GONE);
                        }
                    } else {
                        textNotifyCount.setText(String.valueOf(Math.min(notifyCount, 99)));
                        if (textNotifyCount.getVisibility() != View.VISIBLE) {
                            textNotifyCount.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

    }


    public void hideBottomNav() {
        if (isPortraitsLayout) {
            try {
                bottomNavigationView.setVisibility(View.GONE);
            } catch (NullPointerException x) {
                Log.e("Error ", x.getMessage());
            }


        }
    }

    public void showBottomNav() {
        if (isPortraitsLayout) {
            try {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } catch (NullPointerException x) {
                Log.e("Error ", x.getMessage());
            }
        }
    }


    @Subscribe
    public void changeTitleFragment(EventsWrapper.TitleFragment titleFragment) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(titleFragment.getTitle());
    }

    @Subscribe
    public void resetNotify(EventsWrapper.ResetNotifikasi titleFragment) {
        //notifyCount = 0;
        setupNotifyBadge();
    }

    @Override
    public void onStop() {
        GlobalBus.getBus().unregister(this);
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        if (CURRENT_PAGE == OPERASIONAL_PAGE) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
        Log.i(TAG, "onStart " + Build.VERSION.SDK_INT);

    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        if (!isLogout) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            this.sendBroadcast(broadcastIntent);
        }

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //setIntent(intent);
        String type = "";
        Log.i(TAG, "onNewIntent ");
        if (intent.getExtras() != null) {
            type = intent.getStringExtra("FromPushNotify");
            Log.i(TAG, type);
            if (type.equals("NEW_ORDER") | type.equals("ROOM_CALL")) {
                if (CURRENT_PAGE != NOTIFICATION_PAGE) {
                    navController.navigate(R.id.navNotificationFragment);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        insertToken();
    }

    @Subscribe
    public void scannerDialog(EventsWrapper.XZscan scanType) {

        if (scanType.getScanTypeAction().equals(QRScanType.CHECKIN.getType())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialogCheckinScanQR();
                }
            });
        } else if (scanType.getScanTypeAction().equals(QRScanType.RESERVASI.getType())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialogReservasiScanQR();
                }
            });
        }else if (scanType.getScanTypeAction().equals(QRScanType.VOUCHER.getType())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialogVoucherScanQR();
                }
            });
        }else if (scanType.getScanTypeAction().equals(QRScanType.MEMBER_INFO.getType())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialogMemberInfoScanQR();
                }
            });
        }

    }

    private void dialogCheckinScanQR() {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogDarkTheme);
        LayoutInflater dialogInflater = this.getLayoutInflater();

        View dialogView = dialogInflater.inflate(R.layout.dialog_qrscan_checkin, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("");

        FrameLayout frameLayout = dialogView.findViewById(R.id.frame_layout_camera);
        EditText qrCodeTxt = dialogView.findViewById(R.id.qr_code_txt);
        AppCompatButton buttonOk = dialogView.findViewById(R.id.btn_ok);
        AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);

        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                qrCodeTxt.setText(result.getText());
                mScannerView.stopCamera();
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper
                                .ScanResult(true,result.getText())
                        );
                scanQrDialog.dismiss();
            }
        });
        frameLayout.addView(mScannerView);

        scanQrDialog = dialogBuilder.create();
        scanQrDialog.setOnShowListener(dialogInterface -> {
            mScannerView.startCamera();

            buttonOk.setOnClickListener(view -> {
                mScannerView.stopCamera();
                String result = qrCodeTxt.getText().toString();
                if(result.length()>1){
                    GlobalBus
                            .getBus()
                            .post(new EventsWrapper
                                    .ScanResult(true,result)
                            );
                    scanQrDialog.dismiss();
                }else{
                    Toasty.warning(
                            getApplicationContext(),
                            "Anda belum input kode",
                            Toast.LENGTH_SHORT,
                            true).show();
                }

            });
            buttonCancel.setOnClickListener(view -> {
                mScannerView.stopCamera();
                scanQrDialog.dismiss();
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper
                                .ScanResult(false,null)
                        );
            });
        });

        scanQrDialog.show();

    }

    private void dialogReservasiScanQR() {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogDarkTheme);
        LayoutInflater dialogInflater = this.getLayoutInflater();

        View dialogView = dialogInflater.inflate(R.layout.dialog_qrscan_reservasi, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("");

        FrameLayout frameLayout = dialogView.findViewById(R.id.frame_layout_camera);
        EditText qrCodeTxt = dialogView.findViewById(R.id.qr_code_txt);
        AppCompatButton buttonOk = dialogView.findViewById(R.id.btn_ok);
        AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);

        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                qrCodeTxt.setText(result.getText());
                mScannerView.stopCamera();
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper
                                .ScanResult(true,result.getText())
                        );
                scanQrDialog.dismiss();
            }
        });
        frameLayout.addView(mScannerView);

        scanQrDialog = dialogBuilder.create();
        scanQrDialog.setOnShowListener(dialogInterface -> {
            mScannerView.startCamera();

            buttonOk.setOnClickListener(view -> {
                String result = qrCodeTxt.getText().toString();
                if(result.length()>1){
                    GlobalBus
                            .getBus()
                            .post(new EventsWrapper
                                    .ScanResult(true,result)
                            );
                    scanQrDialog.dismiss();
                }else{
                    Toasty.warning(
                            getApplicationContext(),
                            "Anda belum input kode",
                            Toast.LENGTH_SHORT,
                            true).show();
                }
            });
            buttonCancel.setOnClickListener(view -> {
                mScannerView.stopCamera();
                scanQrDialog.dismiss();
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper
                                .ScanResult(false,null)
                        );
            });
        });

        scanQrDialog.show();

    }

    private void dialogVoucherScanQR() {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogDarkTheme);
        LayoutInflater dialogInflater = this.getLayoutInflater();

        View dialogView = dialogInflater.inflate(R.layout.dialog_qrscan_voucher, null);
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
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper
                                .ScanResult(true,result.getText())
                        );
                scanQrDialog.dismiss();
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
                                .ScanResult(false,null)
                        );
            });
        });

        scanQrDialog.show();

    }

    private void dialogMemberInfoScanQR() {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogDarkTheme);
        LayoutInflater dialogInflater = this.getLayoutInflater();

        View dialogView = dialogInflater.inflate(R.layout.dialog_qrscan_memberlink, null);
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
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper
                                .ScanResult(true,result.getText())
                        );
                scanQrDialog.dismiss();
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
                                .ScanResult(false,null)
                        );
            });
        });
        scanQrDialog.show();
    }
}