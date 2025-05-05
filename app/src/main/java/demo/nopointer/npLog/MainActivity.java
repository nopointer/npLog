package demo.nopointer.npLog;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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

//        startActivity(new Intent(this,DebugActiviyt.class));
//        finish();
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

        NpLog.setLogLevel(NpLog.LEVEL_E);
        NpLog.allowShowCallPathAndLineNumber = true;
        NpLog.log("fuck！！！！");

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        MMKV kv = MMKV.defaultMMKV();
        {
            Button btnCodeDouble = findViewById(R.id.btnCodeDouble);
            String key = date + "_Double";
            String codeDouble = kv.decodeString(key, null);
            if (!TextUtils.isEmpty(codeDouble)) {
                btnCodeDouble.setText(formatCodeDouble(codeDouble));
                btnCodeDouble.setEnabled(false);
            } else {
                btnCodeDouble.setOnClickListener(v -> {
                    String getCode = CodeUtils.startDouble();
                    btnCodeDouble.setText(formatCodeDouble(getCode));
                    kv.encode(key, getCode);
                    btnCodeDouble.setEnabled(false);
                });
            }
        }

        {
            Button btnCodeBig = findViewById(R.id.btnCodeBig);
            String key = date + "_Big";
            String codeBig = kv.decodeString(key, null);
            if (!TextUtils.isEmpty(codeBig)) {

                NpLog.log("codeBig = " + codeBig);

                btnCodeBig.setText(formatCodeBig(codeBig));
                btnCodeBig.setEnabled(false);
            } else {
                btnCodeBig.setOnClickListener(v -> {
                    String getCode = CodeUtils.startBig();
                    btnCodeBig.setText(formatCodeBig(getCode));
                    kv.encode(key, getCode);
                    btnCodeBig.setEnabled(false);
                });
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BleScanner.getInstance().stopScan();
//        PushAiderHelper.getAiderHelper().stop(this);
    }

    static String formatCodeDouble(String code) {
        String[] array = code.split(" , ");
        List<String> tmpList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (!TextUtils.isEmpty(array[i])) {
                tmpList.add(array[i]);
            }
        }

        List<Integer> oneList = new ArrayList<>();
        for (int i = 0; i < tmpList.size() - 1; i++) {
            oneList.add(Integer.valueOf(tmpList.get(i)));
        }
        Collections.sort(oneList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        StringBuffer stringBuffer = new StringBuffer();
        for (int i : oneList) {
            stringBuffer.append(i).append(" , ");
        }
        stringBuffer.append(tmpList.get(tmpList.size() - 1));

        return stringBuffer.toString();
    }


    static String formatCodeBig(String code) {
        String[] array = code.split(" , ");
        NpLog.log("array = " + new Gson().toJson(array));

        List<String> tmpList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (!TextUtils.isEmpty(array[i])) {
                tmpList.add(array[i]);
            }
        }

        NpLog.log("tmpList = " + new Gson().toJson(tmpList));

        List<Integer> oneList = new ArrayList<>();
        for (int i = 0; i < tmpList.size() - 2; i++) {
            oneList.add(Integer.valueOf(tmpList.get(i)));
        }
        Collections.sort(oneList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        List<Integer> twoList = new ArrayList<>();
        for (int i = tmpList.size() - 2; i < tmpList.size(); i++) {
            twoList.add(Integer.valueOf(tmpList.get(i)));
        }


        Collections.sort(twoList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        NpLog.log("onwList = " + new Gson().toJson(oneList));
        NpLog.log("twoList = " + new Gson().toJson(twoList));

        StringBuffer stringBuffer = new StringBuffer();
        for (int i : oneList) {
            stringBuffer.append(i).append(" , ");
        }
        stringBuffer.append(twoList.get(0)).append(" , ");
        stringBuffer.append(twoList.get(1));

        return stringBuffer.toString();
    }


}
