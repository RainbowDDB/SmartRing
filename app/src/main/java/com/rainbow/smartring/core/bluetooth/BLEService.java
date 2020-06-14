package com.rainbow.smartring.core.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.rainbow.smartring.core.Api;
import com.rainbow.smartring.core.database.DatabaseManager;
import com.rainbow.smartring.entity.UserProfile;
import com.rainbow.smartring.core.log.Logger;
import com.rainbow.smartring.utils.BluetoothUtils;
import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.IBleCallback;
import com.vise.baseble.callback.IConnectCallback;
import com.vise.baseble.common.PropertyType;
import com.vise.baseble.core.BluetoothGattChannel;
import com.vise.baseble.core.DeviceMirror;
import com.vise.baseble.exception.BleException;
import com.vise.baseble.model.BluetoothLeDevice;

import java.util.Date;
import java.util.UUID;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/1.
 */
public class BLEService extends Service {

    private static final String TAG = "BLEService";

    private static final String SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private static final String DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";

    // 通信桥梁binder
    private BLEBinder mBinder;
    // 此蓝牙设备的mac地址
    private String mMac; // 50:51:A9:69:A6:7B

    public BLEService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new BLEBinder();
        return mBinder;
    }

    public class BLEBinder extends Binder {
        public void connectDevice() {
            ViseBle.getInstance().connectByName("HC-08", new IConnectCallback() {
                @Override
                public void onConnectSuccess(DeviceMirror deviceMirror) {
                    Logger.i("连接成功");
                    mMac = deviceMirror.getBluetoothLeDevice().getAddress();
                    Logger.d("MAC:" + mMac);
                    try {
                        UserProfile profile = new UserProfile();
                        profile.setSid(mBinder.getDeviceMac());
                        DatabaseManager.getInstance().getUserProfileDao().insert(profile);
                    } catch (Exception e) {
                        Logger.d(TAG, e.toString());
                    }

                    // 服务启动，连接成功时，获取数据
                    Api.queryData(mBinder.getDeviceMac());

                    // 注册信道
                    registerNotify(deviceMirror);

                    Intent successIntent = new Intent(BluetoothStatusReceiver.STATUS_INTENT);
                    successIntent.putExtra("status", BluetoothStatusReceiver.CONNECTED);
                    sendBroadcast(successIntent);
                }

                @Override
                public void onConnectFailure(BleException exception) {
                    Logger.e("连接失败 exception msg:", exception.toString());

                    Intent failIntent = new Intent(BluetoothStatusReceiver.STATUS_INTENT);
                    failIntent.putExtra("status", BluetoothStatusReceiver.CONNECT_FAILED);
                    failIntent.putExtra("message", exception.toString());
                    sendBroadcast(failIntent);
                }

                @Override
                public void onDisconnect(boolean isActive) {
                    Logger.i("断开连接");

                    Intent disconnectIntent = new Intent(BluetoothStatusReceiver.STATUS_INTENT);
                    disconnectIntent.putExtra("status", BluetoothStatusReceiver.DISCONNECT);
                    disconnectIntent.putExtra("message", isActive);
                    sendBroadcast(disconnectIntent);
                }
            });
        }

        public void registerNotify(DeviceMirror deviceMirror) {
            BluetoothGattChannel bluetoothGattChannel = new BluetoothGattChannel.Builder()
                    .setBluetoothGatt(deviceMirror.getBluetoothGatt())
                    .setPropertyType(PropertyType.PROPERTY_NOTIFY) // （指示器方式有bug）使用可通知方式
                    .setServiceUUID(UUID.fromString(SERVICE_UUID))
                    .setCharacteristicUUID(UUID.fromString(CHARACTERISTIC_UUID))
                    .setDescriptorUUID(UUID.fromString(DESCRIPTOR_UUID))
                    .builder();

            deviceMirror.bindChannel(new IBleCallback() {
                @Override
                public void onSuccess(byte[] data, BluetoothGattChannel bluetoothGattChannel, BluetoothLeDevice bluetoothLeDevice) {
                    Logger.d("建立通信链路");
                    Intent successIntent = new Intent(BluetoothStatusReceiver.STATUS_INTENT);
                    successIntent.putExtra("status", BluetoothStatusReceiver.BIND_SUCCESS);
                    sendBroadcast(successIntent);
                }

                @Override
                public void onFailure(BleException exception) {
                    Logger.e("建立通信链路失败 exception：", exception.toString());
                    Intent failIntent = new Intent(BluetoothStatusReceiver.STATUS_INTENT);
                    failIntent.putExtra("status", BluetoothStatusReceiver.BIND_FAILED);
                    failIntent.putExtra("message", exception.toString());
                    sendBroadcast(failIntent);
                }
            }, bluetoothGattChannel);
//            Logger.d("call setNotifyListener method");
            deviceMirror.setNotifyListener(bluetoothGattChannel.getGattInfoKey(), new IBleCallback() {
                @Override
                public void onSuccess(byte[] data, BluetoothGattChannel bluetoothGattChannel, BluetoothLeDevice bluetoothLeDevice) {
                    String s = BluetoothUtils.bytesToString(data);
                    Logger.d("received data:" + s);
//                    Logger.d("generate user_profile" + getDeviceMac());
                    try {
//                        Logger.d("processing data");
                        String[] dataList = s.split(";");
                        Intent dataIntent = new Intent("com.rainbow.dataintent");
                        double t = -1;
                        int hr = -1;
                        int o2 = -1;
                        for (String d : dataList) {
                            if (d.contains("t")) {
                                t = Double.parseDouble(d.substring(d.indexOf(":") + 1)) / 10;
                                DatabaseManager.getInstance().insertT(new Date(), mMac, t);
                                dataIntent.putExtra("temperature", t);
                            } else if (d.contains("hr")) {
                                hr = Integer.parseInt(d.substring(d.indexOf(":") + 1));
                                DatabaseManager.getInstance().insertHr(new Date(), mMac, hr);
                                dataIntent.putExtra("hr", hr);
                            } else if (d.contains("o2")) {
                                o2 = Integer.parseInt(d.substring(d.indexOf(":") + 1));
                                DatabaseManager.getInstance().insertO2(new Date(), mMac, o2);
                                dataIntent.putExtra("o2", o2);
                            }
                        }
                        Logger.d("sendDataBroadcast");
                        sendBroadcast(dataIntent);
                        Api.sendData(mMac, t, hr, o2);
                    } catch (Exception e) {
                        Logger.e(e.toString());
                    }
                }

                @Override
                public void onFailure(BleException exception) {
                    Logger.e(exception.toString());
                }
            });
//            Logger.d("call registerNotify method finished");
            deviceMirror.registerNotify(false);
        }

        public String getDeviceMac() {
            return mMac;
        }

        public BLEService getService() {
            return BLEService.this;
        }
    }
}
