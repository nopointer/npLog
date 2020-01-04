package lib.ycble;

import android.app.Application;
import android.os.Handler;


public class MainApplication extends Application {


    private static final int NOTIFICATION_TITLE_TYPE = 9;
    private static final int NOTIFICATION_CONTENT_TYPE = 10;


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
