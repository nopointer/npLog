package demo.nopointer.npLog;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.util.Scanner;
import java.util.UUID;

import npLog.nopointer.core.NpLog;

import static android.bluetooth.BluetoothDevice.PHY_LE_1M;
import static android.bluetooth.BluetoothDevice.PHY_LE_1M_MASK;
import static android.bluetooth.BluetoothDevice.PHY_LE_2M;
import static android.bluetooth.BluetoothDevice.PHY_LE_2M_MASK;
import static android.bluetooth.BluetoothDevice.PHY_LE_CODED;
import static android.bluetooth.BluetoothDevice.PHY_LE_CODED_MASK;
import static android.bluetooth.BluetoothDevice.PHY_OPTION_NO_PREFERRED;
import static android.bluetooth.BluetoothDevice.PHY_OPTION_S2;
import static android.bluetooth.BluetoothDevice.PHY_OPTION_S8;

public class DebugActiviyt extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, 100);
        }

        findViewById(R.id.btnConn).setOnClickListener(v -> conn());
        findViewById(R.id.btnNotify).setOnClickListener(v -> notifyData());
        findViewById(R.id.btnSend).setOnClickListener(v -> send());

    }

    BluetoothGatt gatt = null;

    private BluetoothGattCharacteristic writeCha;
    private BluetoothGattCharacteristic notifyCha;

    void conn() {
        String mac = "AD:91:7A:4B:03:E7";
//        String mac = "50:C0:F0:33:6C:EB";
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gatt = device.connectGatt(this, false, gattCallback, BluetoothDevice.TRANSPORT_LE);
        }

    }

    void notifyData() {
        BluetoothGattService service = gatt.getService(UUID.fromString("06068d0c-6b97-11ef-b864-0240ac120002"));
        writeCha = service.getCharacteristic(UUID.fromString("06068d1c-6b97-11ef-b864-0241ac120002"));
        notifyCha = service.getCharacteristic(UUID.fromString("06068d2c-6b97-11ef-b864-0242ac120002"));

        gatt.setCharacteristicNotification(notifyCha, true);
//        gatt.setCharacteristicNotification(
//                gatt.getService(UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb"))
//                        .getCharacteristic(UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")), true);
//
//        gatt.setCharacteristicNotification(
//                gatt.getService(UUID.fromString("00239a6f-c616-89bb-3374-f05af588a7b3"))
//                        .getCharacteristic(UUID.fromString("00239a8f-c616-89bb-3374-f25af588a7b3")), true);

        BluetoothGattDescriptor descriptor = notifyCha.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        NpLog.log("descriptor = " + descriptor.getUuid());
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        boolean ret = gatt.writeDescriptor(descriptor);

        NpLog.log("写描述 ret = " + ret);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        send();
    }

    void send() {
//        writeCha.setValue("*APP#version#");
        writeCha.setValue("*APP#record#1#1#0#");
        writeCha.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        boolean res = gatt.writeCharacteristic(writeCha);
        NpLog.log("写数据 res = " + res);
    }

    BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }


        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);

        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            NpLog.log("status = " + status + " , newState = " + newState);

            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_CONNECTED) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            NpLog.log("onServicesDiscovered -> status = " + status);

            for (BluetoothGattService service : gatt.getServices()) {
                NpLog.log("service -> status = " + service.getUuid());
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    NpLog.log("characteristic -> status = " + characteristic.getUuid());
                }
            }
//            watch();
            headset();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            NpLog.log("onCharacteristicChanged -> status = ");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            NpLog.log("onCharacteristicWrite -> status = " + status + " , " + new String(characteristic.getValue()));
        }
    };


    void watch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gatt.requestMtu(100);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            gatt.setPreferredPhy(2, 2, 0);
        }

        BluetoothGattService service = gatt.getService(UUID.fromString("6E40FFF0-B5A3-F393-E0A9-E50E24DCCA9E"));
        writeCha = service.getCharacteristic(UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E"));
        notifyCha = service.getCharacteristic(UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E"));

        gatt.setCharacteristicNotification(notifyCha, true);
//        gatt.setCharacteristicNotification(
//                gatt.getService(UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb"))
//                        .getCharacteristic(UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")), true);
//
//        gatt.setCharacteristicNotification(
//                gatt.getService(UUID.fromString("00239a6f-c616-89bb-3374-f05af588a7b3"))
//                        .getCharacteristic(UUID.fromString("00239a8f-c616-89bb-3374-f25af588a7b3")), true);

        BluetoothGattDescriptor descriptor = notifyCha.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        boolean ret = gatt.writeDescriptor(descriptor);

        NpLog.log("写描述 ret = " + ret);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        writeCha.setValue("*App#version#");
        writeCha.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_SIGNED);
        boolean res = gatt.writeCharacteristic(writeCha);
        NpLog.log("写数据 res = " + res);


    }

    void headset() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gatt.requestMtu(300);
        }
//        Looper.prepare();
//        new Handler().postDelayed(() -> {
//            notifyData();
//        }, 300);
//        Looper.loop();
//
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Looper.prepare();
//        new Handler().postDelayed(() -> {
//            send();
//        }, 600);
//        Looper.loop();
//        notifyData();
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        send();
    }


}
