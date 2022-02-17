package com.example.tablayout;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    private static final String APP_SETTINGS = "APP_SETTINGS";

    public static final String TOKEN = "TOKEN";


    private SharedPreferenceManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getStringValue(Context context, String key) {
        return getSharedPreferences(context).getString(key , null);
    }

    public static void setStringValue(Context context, String newValue, String key) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key , newValue);
        editor.apply();
    }
}
