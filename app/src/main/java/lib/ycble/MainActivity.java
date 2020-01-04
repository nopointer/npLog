package lib.ycble;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import npLog.nopointer.core.npLog;
import npLog.nopointer.mail.SendMailUtil;


public class MainActivity extends Activity {


    //    public static String mac = "0C:B2:B7:53:39:D2";
//    public static String macForHTX = "D2:92:7C:6B:8B:7A";

    String file1Path = "/storage/emulated/0/Download/Bluetooth/OTA_TEST1.bin";
    String file2Path = "/storage/emulated/0/Download/Bluetooth/OTA_TEST2.bin";

    String file3Path = "/storage/emulated/0/Bluetooth/MagicSwitch__OAD.bin";
    String pathForHTX = "/sdcard/otaHelper/firmware/V5HD_H_20191024.bin";


    //    public static final String macForXinCore = "E3:06:05:E7:9C:52";
    public static final String macForXinCore = "CF:56:48:42:07:E2";

    public static final String pathForXinCore = "/storage/emulated/0/Bluetooth/xincore_band.bin";

    private TextView textBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }
        textBtn = findViewById(R.id.textBtn);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PushAiderHelper.getAiderHelper().goToSettingNotificationAccess(MainActivity.this);
//                startActivity(new Intent(MainActivity.this,BleActivity.class));
            }
        });

        npLog.initLog(null, null);

//        npLog.clearLogFile();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                for (int i = 0; i < 100*4; i++) {
//                    npLog.eAndSave("哈哈收发文奥无过和或或或或若或如隔热有何意义红外比的风格大哥哥哥哥各个而唯一我以为个人业余");
//                }
                SendMailUtil.send(npLog.getBleLogFileDir(), "635669470@qq.com", "npLog", "123", new SendMailUtil.SendMailCallback() {
                    @Override
                    public void onSend(boolean isSuccess) {

                    }
                });
            }
        }).start();


//        npLog.eAndSave("what？");

//        startActivity(new Intent(MainActivity.this,BleActivity.class));
//        BleScanner.getInstance().registerScanListener(this);
//        BleScanner.getInstance().startScan();

//        mac = "20:17:98:F7:7F:E5";
//        OTAHelper.getInstance().startOTA(this, file2Path, mac, null, FirmType.HTX, new OTACallback() {
//            @Override
//            public void onFailure(int code, String message) {
//                npLog.e("onFailure==>" + code + "///" + message);
//            }
//
//            @Override
//            public void onSuccess() {
//                npLog.e("onSuccess==>");
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                npLog.e("onProgress==>" + progress);
//            }
//        });
//        startService(new Intent(this, BgService.class));


//        BleDevice bleDevice = new BleDevice("W28", macForXinCore);
////
//        OTAHelper.getInstance().startOTA(this, pathForXinCore, bleDevice, FirmType.XC, new OTACallback() {
//
//
//            @Override
//            public void onFailure(int code, String message) {
//                npLog.e("onFailure===ota失败" + message);
//            }
//
//            @Override
//            public void onSuccess() {
//                npLog.e("onSuccess===ota成功");
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                npLog.e("progress===>ota进度" + progress);
//            }
//        });

//        BlePhoneSysUtil.releaseAllScanClient();
//        BlePhoneSysUtil.refreshBleAppFromSystem(this,"lib.ycble.demo");
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//        NpBleManager.getBleManager().connBleDevice(macForXinCore);

//        startService(new Intent(this, MainBackLiveService.class));
//        BleScanner.getBleScaner().startScan();

//        NpBleManager.getBleManager().connDevice(mac);


//        npLog.e("MTK mode==>" + WearableManager.getInstance().getWorkingMode());
//
//
//        Set<BluetoothDevice> tmpList = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
//
//
//        BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice("40:3D:A0:01:62:61");
//        npLog.e("name===>" + bluetoothDevice.getName());
//
//        WearableManager.getInstance().setRemoteDevice(bluetoothDevice);
//        npLog.e("[wearable][onCreate], BTNoticationApplication WearableManager connect!///" + WearableManager.getInstance().getWorkingMode());
//        WearableManager.getInstance().connect();
    }


    @Override
    protected void onDestroy() {
        npLog.e("清理掉了app");
        super.onDestroy();
//        BleScanner.getInstance().stopScan();
//        PushAiderHelper.getAiderHelper().stop(this);
    }


//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        PushAiderHelper.getAiderHelper().registerCallAndSmsReceiver(this);
////        PushAiderHelper.getAiderHelper().startListeningForNotifications(this);
//        if (PushAiderHelper.getAiderHelper().isNotifyEnable(this)) {
//            PushAiderHelper.getAiderHelper().startListeningForNotifications(this);
//            textBtn.setText("已开启");
//
//        } else {
//            textBtn.setText("未开启");
//        }
//    }

    //    adb shell dumpsys activity | grep -i run
//    plugin.voip.ui.VideoActivity
//    plugin.voip.ui.VideoActivity

}
