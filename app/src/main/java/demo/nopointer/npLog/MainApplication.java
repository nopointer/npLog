package demo.nopointer.npLog;

import android.app.Application;
import android.os.Handler;


public class MainApplication extends Application {


    public static MainApplication mainApplication = null;

    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
    }

    public static MainApplication getMainApplication() {
        return mainApplication;
    }





}
