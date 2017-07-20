package com.jcdelacueva.yootoov;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import com.jcdelacueva.yootoov.models.Convert;
import com.jcdelacueva.yootoov.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import static com.jcdelacueva.yootoov.Constants.APP_ID;
import static com.jcdelacueva.yootoov.Constants.SERVER_URL;

public class YootoovApp extends Application {
    public final static String LOG_TAG = "Yootoov";

    private static Context mContext;
    public static boolean isDebug;

    @Override public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        YootoovApp.isDebug = BuildConfig.DEBUG;

        initParse();
    }

    private void initParse() {
        ParseObject.registerSubclass(Convert.class);
        ParseObject.registerSubclass(User.class);

        if (isDebug) {
            Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        }

        Parse.initialize(new Parse.Configuration.Builder(mContext).applicationId(APP_ID).server(SERVER_URL).clientKey("").build());
    }

    public static String getDownloadPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.MUSIC_PATH;
    }
}
