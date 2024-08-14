package demo.nopointer.npLog;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.Locale;

import npLog.nopointer.core.NpLog;
import npLog.nopointer.mail.SendMailUtil;


public class MainActivity extends Activity {


    private static String HOST = "smtp.qq.com";
    private static String PORT = "587";
    private static String FROM_ADD = "3343249301@qq.com";
    //现在QQ邮箱不是密码 而是临时密令
    private static String FROM_PSW = "davpgtmyazmbciij";


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
                SendMailUtil.send(NpLog.getLogFileDir(), "635669470@qq.com", "npLog", "123", new SendMailUtil.SendMailCallback() {
                    @Override
                    public void onSend(boolean isSuccess) {

                    }
                });
            }
        });
        Locale locale = Locale.getDefault();
//
//        debug====locale==>zh_CN//中文
//        debug====locale==>zh_TW//台湾繁体
//        debug====locale==>zh_HK//香港繁体
        String language = locale.toString();
//        Toast.makeText(this, language, 0).show();
        textBtn.setText(language);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMailUtil.send(NpLog.getLogFileDir(), "857508412@qq.com", "NpLog", "", new SendMailUtil.SendMailCallback() {
                    @Override
                    public void onSend(boolean isSuccess) {

                    }
                });
            }
        });

//        NpLog.initLog(null, null, this);

        NpLog.logAndSave("哈哈");

//        NpLog.eAndSave("debug");


        SendMailUtil.setFromAdd(FROM_ADD);
        SendMailUtil.setFromPsw(FROM_PSW);
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


        NpLog.setLogLevel(NpLog.LEVEL_E);
        NpLog.allowShowCallPathAndLineNumber = true;
        NpLog.log("fuck！！！！");

//        File dir = NpLog.getLogFileDir();
//        NpLog.log("dir = " + dir.getPath());
//        File file = NpLog.getLogFile();
//        NpLog.log("file = " + file.getPath());
    }


    @Override
    protected void onDestroy() {
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
