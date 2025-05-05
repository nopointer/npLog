package demo.nopointer.npLog;

import android.app.Application;
import android.os.Handler;

import com.tencent.mmkv.MMKV;


public class MainApplication extends Application {


    public static MainApplication mainApplication = null;

    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
        MMKV.initialize(this);
    }

    public static MainApplication getMainApplication() {
        return mainApplication;
    }





}
