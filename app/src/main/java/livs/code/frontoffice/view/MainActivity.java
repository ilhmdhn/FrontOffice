package livs.code.frontoffice.view;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.RingtoneManager;
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
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skyfishjy.library.RippleBackground;

import org.greenrobot.eventbus.Subscribe;

import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Notification;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.repository.LocalRepository;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.helper.PreferenceUi;
import livs.code.frontoffice.helper.QRScanType;
import livs.code.frontoffice.sio.SioBackgroundReceiver;
import livs.code.frontoffice.sio.SioBackgroundService;
import livs.code.frontoffice.sio.SioEvent;
import livs.code.frontoffice.sio.SioEventImplement;
import livs.code.frontoffice.sio.SioEventListener;
import livs.code.frontoffice.view.component.PushNotify;
import livs.code.frontoffice.viewmodel.NotificationViewModel;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity
        implements SioEventListener {


    private static final String TAG = MainActivity.class.getSimpleName();

//    NavigationView mNavigationView;
    BottomNavigationView bottomNavigationView;


    private int navItemIndex = 0;
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";


    private boolean isPortraitsLayout;
    private boolean mUserLearnedDrawer;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar_title)
    TextView mTitle;


    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private TextView textNotifyCount;
    private int notifyCount = 0;
    private SioEvent sioEvent;
    private boolean currentStateInstance;
    private LocalRepository localRepository;

    //page

    private MaterialAlertDialogBuilder dialogBuilder;
    private LayoutInflater dialogInflater;
    private View dialogView;
    private AppCompatButton buttonConfirmCall, buttonRejectCall;
    private AlertDialog alertDialog;
    private RippleBackground rippleBackground;
    private TextView labelCallRom;
    protected PowerManager.WakeLock mWakeLock;
    private PushNotify pushNotify;
    private SioBackgroundService mYourService;
    private Intent mServiceIntent;
    private User USER_FO;
    private String BASE_URL;
    private Handler handler;
    private NotificationViewModel notificationViewModel;
    private static String CURRENT_PAGE = "";
    private final static String NOTIFICATION_PAGE = "NOTIFICATION_PAGE";
    private final static String OPERASIONAL_PAGE = "OPERASIONAL_PAGE";
    private boolean isLogout = false;
    private ZXingScannerView mScannerView;
    private AlertDialog scanQrDialog;

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

        handler = new Handler();
        if (toolbar != null) {
            setToolbarActivity();
        }


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
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            appBarLayout.setExpanded(true, true);
            CURRENT_PAGE = "";
            switch (destination.getId()) {
                case R.id.navOperasionalFragment:
                    showBottomNav();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    CURRENT_PAGE = OPERASIONAL_PAGE;
                    break;
                case R.id.navReportFragment:
                case R.id.navRoomStatusFragment:
                case R.id.navListRoomFragment:
                case R.id.navListInventoryFragment:
                    showBottomNav();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    break;
                case R.id.navNotificationFragment:
                    hideBottomNav();
                    CURRENT_PAGE = NOTIFICATION_PAGE;
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
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
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
                    break;
                case R.id.navCheckinRoomFragment:
                    hideBottomNav();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                case R.id.navExtendsRoomFragment:
                    hideBottomNav();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    break;
                case R.id.navTransferRoomFragment:
                    hideBottomNav();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    break;
                case R.id.navReportingFragment:
                    showBottomNav();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    break;
                case R.id.navStatusKasFragment:
                    hideBottomNav();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
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

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        if (isPortraitsLayout) {
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            /*NavigationUI.setupWithNavController(
                    toolbar, navController, mAppBarConfiguration);*/
        }
//        else {
//            NavigationUI.setupWithNavController(mNavigationView, navController);
//            /*NavigationUI.setupWithNavController(
//                    toolbar, navController, mAppBarConfiguration);*/
//        }
        localRepository = LocalRepository.getInstance(getApplicationContext());
        checkPermission();

        notificationViewModel = new ViewModelProvider(this)
                .get(NotificationViewModel.class);
        notificationViewModel.init(getApplicationContext());
        observableNotifyData();
        setupFoRct();
    }

    void observableNotifyData() {
        notificationViewModel
                .getUnreadNotificationLiveData()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        notifyCount = integer;
                        setupNotifyBadge();
                    }
                });

    }

    private void setToolbarActivity() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_home);
    }

    private void checkPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
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

    /*@Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/


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

       /* NavController navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);*/
        switch (item.getItemId()) {
            case R.id.refresh_menu:
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper.RefreshCurrentFragment(true));
                break;

            case R.id.notification_menu:
                navController.navigate(R.id.navNotificationFragment);
                break;

            case R.id.logout_menu:
                logOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Log Out")
                .setMessage("Anda ingin keluar aplikasi?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        handler.postDelayed(
                                new Runnable() {
                                    public void run() {
                                        isLogout = true;
                                        localRepository.setUserLogout();
                                        sioEvent.disconnect();
                                        if (isMyServiceRunning(mYourService.getClass())) {
                                            stopService(mServiceIntent);
                                        }
                                        //((MyApp) getApplicationContext()).setBaseUrl("");

                                        Intent moveToMain = new Intent(MainActivity.this, LoginActivity.class);
                                        moveToMain.putExtra("OUT", true);
                                        moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(moveToMain);
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
            //NavigationUI.setupWithNavController(bottomNavigationView, navController);
            try {
                bottomNavigationView.setVisibility(View.GONE);
            } catch (NullPointerException x) {
                Log.e("Error ", x.getMessage());
            }


        }
    }

    public void showBottomNav() {
        if (isPortraitsLayout) {
            //NavigationUI.setupWithNavController(bottomNavigationView, navController);

            try {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } catch (NullPointerException x) {
                Log.e("Error ", x.getMessage());
            }
        }
    }

    @Subscribe
    public void changeTitleFragment(EventsWrapper.TitleFragment titleFragment) {
        setToolbarActivity();
        mTitle.setText(titleFragment.getTitle());
    }

    @Subscribe
    public void resetNotify(EventsWrapper.ResetNotifikasi titleFragment) {
        //notifyCount = 0;
        setupNotifyBadge();
    }


    @Override
    public void onStop() {
        GlobalBus.getBus().unregister(this);
       /* Log.i(TAG, "onStop()");
		sioEvent.disconnect();
		if(!isLogout){
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, SioBackgroundReceiver.class);

            this.sendBroadcast(broadcastIntent);
        }
*/

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
        sioEvent.disconnect();
        Log.i(TAG, "onDestroy()");
        if (!isLogout) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, SioBackgroundReceiver.class);
            this.sendBroadcast(broadcastIntent);
        }

        super.onDestroy();
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
        // TODO :: init sio switch background and main

    }

    private void setupFoRct() {
        isLogout = false;
        sioEvent = SioEventImplement.getInstance(BASE_URL, USER_FO);
        sioEvent.setEventListener(this);
        mYourService = new SioBackgroundService();
        mServiceIntent = new Intent(getBaseContext(), mYourService.getClass());

        if (isMyServiceRunning(mYourService.getClass())) {
            stopService(mServiceIntent);
            handler.postDelayed(
                    new Runnable() {
                        public void run() {
                            try {
                                sioEvent.connect();
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 2000);

        } else {
            try {

                sioEvent.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Toasty.error(getApplicationContext(),
                        "SIO connect() Error : " + e.getMessage(),
                        Toast.LENGTH_SHORT,
                        true)
                        .show();
            }
        }
    }


    @Override
    public void onConnect(Object... args) {
        Log.i(TAG, "SIO onConnect");
    }

    @Override
    public void onDisconnect(Object... args) {
        Log.e(TAG, "SIO onDisconnect");
    }

    @Override
    public void onConnectError(Object... args) {
        Log.e(TAG, "SIO onConnectError");
    }

    @Override
    public void onConnectTimeout(Object... args) {
        Log.e(TAG, "SIO onConnectTimeout");
    }

    @Override
    public void onNewOrder(Notification notification) {
        notification.setRead(false);
        localRepository.insertNotification(notification);
        //notifyCount++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pushNotify = new PushNotify(getApplication(), notification, RingtoneManager.TYPE_NOTIFICATION);
            }
        });
        setupNotifyBadge();
    }

    @Override
    public void onRoomCall(Notification notification) {
        notification.setRead(false);
        localRepository.insertNotification(notification);
        //notifyCount++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //new PushNotify(getApplication(), notification,RingtoneManager.TYPE_ALARM);
                pushNotify = new PushNotify(getApplication(), notification, RingtoneManager.TYPE_NOTIFICATION);
            }
        });
        setupNotifyBadge();

        /*dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
        dialogInflater = this.getLayoutInflater();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dialogView = dialogInflater.inflate(R.layout.dialog_call_room, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                dialogBuilder.setTitle("");

                buttonConfirmCall = dialogView.findViewById(R.id.btn_confirm);
                buttonRejectCall = dialogView.findViewById(R.id.btn_reject);
                rippleBackground = dialogView.findViewById(R.id.ripple);

                labelCallRom = dialogView.findViewById(R.id.label_info_call_room);
                alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(dialogInterface -> {
                    labelCallRom.setText("Room " + notification.getRoomCode() + " Call");

                    rippleBackground.startRippleAnimation();

                    buttonConfirmCall.setOnClickListener(view -> {
                        notification.setAcceptedUser(USER_FO.getUserId());
                        sioEvent.acceptedRoomCall(notification);
                        pushNotify.stopPlayer();
                        rippleBackground.stopRippleAnimation();
                        alertDialog.dismiss();
                    });

                    buttonRejectCall.setOnClickListener(view -> {
                        notification.setAcceptedUser(USER_FO.getUserId());
                        sioEvent.rejectedRoomCall(notification);
                        pushNotify.stopPlayer();
                        rippleBackground.stopRippleAnimation();
                        alertDialog.dismiss();
                    });
                });

                alertDialog.show();
            }
        });*/


    }

    @Override
    public void onHideRoomCall(Notification notification) {
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    rippleBackground.stopRippleAnimation();
                    pushNotify.stopPlayer();
                    alertDialog.dismiss();
                }
            }
        });*/
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

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
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

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
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

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
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

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
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