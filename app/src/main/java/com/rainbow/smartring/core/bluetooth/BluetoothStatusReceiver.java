package com.rainbow.smartring.core.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/13.
 */
public class BluetoothStatusReceiver extends BroadcastReceiver {
    public static final String STATUS_INTENT = "com.rainbow.statusintent";
    public static final String CONNECTED = "connected";
    public static final String CONNECT_FAILED = "connect failed";
    public static final String DISCONNECT = "disconnected";
    public static final String BIND_FAILED = "bind failed";
    public static final String BIND_SUCCESS = "bind success";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (STATUS_INTENT.equals(intent.getAction())) {
            switch (intent.getStringExtra("status")) {
                case CONNECTED:
                    Toast.makeText(context, "蓝牙已连接", Toast.LENGTH_SHORT).show();
                    break;
                case CONNECT_FAILED:
                    break;
                case DISCONNECT:
                    Toast.makeText(context, "蓝牙已断开连接，请重新连接", Toast.LENGTH_SHORT).show();
                    break;
                case BIND_FAILED:
                    break;
                case BIND_SUCCESS:
                    Toast.makeText(context, "蓝牙已绑定连接", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    }
}
