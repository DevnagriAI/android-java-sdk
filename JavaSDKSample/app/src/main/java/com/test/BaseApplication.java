package com.test;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.devnagritranslationsdk.DevNagriTranslationSdk;
import com.devnagritranslationsdk.interfaces.ResponseListener;
import com.translationsdk.R;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

import androidx.annotation.RequiresApi;

public class BaseApplication extends Application {

    static String TAG = "baseApplicationTAG";
    static DevNagriTranslationSdk devNagriTranslationSdk;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();

        Field[] strings = R.string.class.getFields();
        Field[] arrays = R.array.class.getFields();
        //List<String> collect = Arrays.stream(R.plurals.class.getFields()).map(Field::getName).collect(toList());

        String API_KEY = "Enter Your API KEY Here";
        int sync_Time = 10;
        try {
            Log.d(TAG, "Start init process");
            devNagriTranslationSdk = new DevNagriTranslationSdk();
            devNagriTranslationSdk.init(getApplicationContext(), API_KEY, sync_Time, strings, arrays, null, new ResponseListener() {
                @Override
                public void responseCallback(Boolean isCompleted, String error) {
                    Log.d(TAG, "Init Completed");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
