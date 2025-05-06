package demo.nopointer.npLog;

import android.app.Application;
import android.os.Handler;

import com.tencent.mmkv.MMKV;

import npLog.nopointer.core.NpLog;


public class MainApplication extends Application {


    public static MainApplication mainApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
        NpLog.initLog(this);
        MMKV.initialize(this);
    }

    public static MainApplication getMainApplication() {
        return mainApplication;
    }





}
