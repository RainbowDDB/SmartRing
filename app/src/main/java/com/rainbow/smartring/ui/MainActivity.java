package com.rainbow.smartring.ui;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rainbow.smartring.core.Api;
import com.rainbow.smartring.R;
import com.rainbow.smartring.core.bluetooth.BLEService;
import com.rainbow.smartring.core.bluetooth.BluetoothStatusReceiver;
import com.rainbow.smartring.core.database.DatabaseManager;
import com.rainbow.smartring.entity.TData;
import com.rainbow.smartring.entity.UserProfile;
import com.rainbow.smartring.core.log.Logger;
import com.rainbow.smartring.entity.UserProfileDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends PermissionActivity implements View.OnClickListener {

    // 请求打开蓝牙requestCode
    private static final int REQUEST_ENABLE_BT = 0;

    private BLEService mService;
    private BLEService.BLEBinder mBinder;
    private BluetoothDataReceiver receiver;
    private boolean mBounded = false;

    private TextView status;

    private TextView tvHeartRate, tvBloodOxygen, tvTemperature;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (BLEService.BLEBinder) iBinder;
            mService = mBinder.getService();
            mBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBounded = false;
        }
    };

    private class BluetoothDataReceiver extends BroadcastReceiver {
        public static final String DATA_INTENT = "com.rainbow.dataintent";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DATA_INTENT.equals(intent.getAction())) {
                double temp = intent.getDoubleExtra("temperature", -1);
                int hr = intent.getIntExtra("hr", -1);
                int o2 = intent.getIntExtra("o2", -1);
                tvTemperature.setText(temp != -1 ?
                        String.format("%s℃", String.valueOf(temp)) : "--℃");
                tvHeartRate.setText(hr != -1 ?
                        String.format("%s次/分", String.valueOf(hr)) : "--次/分");
                tvBloodOxygen.setText(o2 != -1 ? o2 + "%" : "--%");
            } else if (BluetoothStatusReceiver.STATUS_INTENT.equals(intent.getAction())) {
                switch (intent.getStringExtra("status")) {
                    case BluetoothStatusReceiver.BIND_FAILED:
                        tvTemperature.setText("--℃");
                        tvHeartRate.setText("--次/分");
                        tvBloodOxygen.setText("--%");
                        break;
                    case BluetoothStatusReceiver.CONNECTED:
                        status.setText("连接成功");
                        break;
                    case BluetoothStatusReceiver.BIND_SUCCESS:
                        status.setText("绑定设备成功");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 绑定服务
        Intent service = new Intent(this, BLEService.class);
        bindService(service, mConnection, BIND_AUTO_CREATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkBluetooth();
        initView();
    }

    @Override
    protected Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new BluetoothDataReceiver();
        registerReceiver(receiver, new IntentFilter(BluetoothDataReceiver.DATA_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        receiver = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 解绑服务
        if (mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    private void checkBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // 设备不支持蓝牙
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            Logger.e("设备不支持蓝牙");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void initView() {
        Button connect = $(R.id.connect_btn);
        Button analyze = $(R.id.analyze);
        status = $(R.id.tv_device_status);

        tvTemperature = $(R.id.tv_temperature);
        tvBloodOxygen = $(R.id.tv_blood_oxygen);
        tvHeartRate = $(R.id.tv_heart_rate);

        LinearLayout temperatureLayout = $(R.id.layout_temperature);
        LinearLayout bloodOxygenLayout = $(R.id.layout_blood_oxygen);
        LinearLayout heartRateLayout = $(R.id.layout_heart_rate);

        connect.setOnClickListener(this);
        temperatureLayout.setOnClickListener(this);
        bloodOxygenLayout.setOnClickListener(this);
        heartRateLayout.setOnClickListener(this);

        status.setOnClickListener(this);
        analyze.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    // 蓝牙启用成功
                    Toast.makeText(this, "蓝牙已启用", Toast.LENGTH_SHORT).show();
                    Logger.i("用户已启用");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            // 未知错误 或 用户拒绝连接蓝牙
            Toast.makeText(this, "用户拒绝权限", Toast.LENGTH_SHORT).show();
            Logger.e("用户拒绝权限");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect_btn:
                status.setText("连接中");
                mBinder.connectDevice();
                break;
            case R.id.layout_temperature:
                start(TempActivity.class);
                break;
            case R.id.layout_heart_rate:
                start(HrActivity.class);
                break;
            case R.id.layout_blood_oxygen:
                start(OxygenActivity.class);
                break;
            case R.id.analyze:
                Api.analyze("50:51:A9:69:A6:7B", (response -> {
                    Logger.d("analyze mac:" + mBinder.getDeviceMac());
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    start(AnalysisActivity.class, bundle);
                }));
                break;
            case R.id.tv_device_status:
                insert();
                break;
            default:
                break;
        }
    }

    private void insert() {
        try {
            UserProfile profile = new UserProfile();
            profile.setSid("50:51:A9:69:A6:7B");
            DatabaseManager.getInstance().getUserProfileDao().insert(profile);
        } catch (Exception e) {
            Logger.e(e.toString());
        }

        for (int i = 0; i < 60; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.HOUR_OF_DAY, -1);
            c.add(Calendar.MINUTE, i);
            Date pre = c.getTime();
            Random random = new Random();
            int hr = random.nextInt(10) + 60;
            DatabaseManager.getInstance().insertHr(pre, "50:51:A9:69:A6:7B", hr);
        }
        for (int i = 0; i < 60; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.HOUR_OF_DAY, -1);
            c.add(Calendar.MINUTE, i);
            Date pre = c.getTime();
            Random random = new Random();
            int o2 = random.nextInt(5) + 95;
            DatabaseManager.getInstance().insertO2(pre, "50:51:A9:69:A6:7B", o2);
        }
        for (int i = 0; i < 24; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.HOUR_OF_DAY, i);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            Date pre = c.getTime();
            Random random = new Random();
            double t = random.nextDouble() * 5 + 35;
            DatabaseManager.getInstance().insertT(pre, "50:51:A9:69:A6:7B", t);
        }
    }

    private void query() {
        List<TData> da = DatabaseManager.getInstance().getUserProfileDao().queryBuilder()
                .where(UserProfileDao.Properties.Sid.eq("50:51:A9:69:A6:7B"))
                .unique()
                .getTDataList();
        for (TData td : da) {
            Logger.i("temperature", td.getDate().toString() + "--->" + td.getTemperature() + "℃");
        }
    }
}
