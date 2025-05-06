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

    private TextView btnMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startActivity(new Intent(this,DebugActiviyt.class));
//        finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        btnMail = findViewById(R.id.btnMail);
        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMailUtil.send(NpLog.getLogFile(), "635669470@qq.com", "npLog", "123", new SendMailUtil.SendMailCallback() {
                    @Override
                    public void onSend(boolean isSuccess) {

                    }
                });
            }
        });
//        Locale locale = Locale.getDefault();

//        debug====locale==>zh_CN//中文
//        debug====locale==>zh_TW//台湾繁体
//        debug====locale==>zh_HK//香港繁体
//        String language = locale.toString();
//        Toast.makeText(this, language, 0).show();
//        btnMail.setText(language);

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < 1000; i++) {
            stringBuffer.append("adfasdfasasdgagasgagagasgsg");
        }
        NpLog.logAndSave("哈哈 - >" + stringBuffer.toString());
        SendMailUtil.setFromAdd(FROM_ADD);
        SendMailUtil.setFromPsw(FROM_PSW);

        NpLog.setLogLevel(NpLog.LEVEL_E);
        NpLog.allowShowCallPathAndLineNumber = true;
        NpLog.log("fuck！！！！");


        findViewById(R.id.btnCode).setOnClickListener(v -> {
            startActivity(new Intent(this, CodeActivity.class));
        });


    }


}
