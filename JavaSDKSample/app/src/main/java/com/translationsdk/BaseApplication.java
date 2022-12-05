package com.translationsdk;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.devnagritranslationsdk.DevNagriTranslationSdk;
import com.devnagritranslationsdk.interfaces.ResponseListener;
import java.lang.reflect.Field;

public class BaseApplication extends Application {

    static String TAG = "baseApplicationTAG";
    public static DevNagriTranslationSdk devNagriTranslationSdk;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();

        Field[] strings = R.string.class.getFields();
        Field[] arrays = R.array.class.getFields();
        /*Field[] plurals = R.plurals.class.getFields();*/

        int sync_Time = 50;
        try {
            devNagriTranslationSdk = new DevNagriTranslationSdk();
            Log.d("BaseApplication_TAG", "DevNagri Sdk initializing");
            devNagriTranslationSdk.init(getApplicationContext(), "devnagri_14f278ae6bc711eda08202bf838402f8", sync_Time, strings, arrays, null, new ResponseListener() {
                @Override
                public void responseCallback(Boolean isCompleted, String error) {
                    Log.d("BaseApplication_TAG", "DevNagri Sdk initialized");
                }
            });
         } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
