package com.rainbow.smartring.core.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Latte-Core
 * Created By Rainbow on 2019/4/30.
 */
@SuppressWarnings("unused")
public final class LattePreference {
    /**
     * 提示:
     * Activity.getPreferences(int mode)生成 Activity名.xml 用于Activity内部存储
     * PreferenceManager.getDefaultSharedPreferences(Context)生成 包名_preferences.xml
     * Context.getSharedPreferences(String name,int mode)生成name.xml
     */

    private static final String APP_PREFERENCES_KEY = "profile";

    private static SharedPreferences getAppPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static void setAppProfile(Context context, String val) {
        getAppPreference(context)
                .edit()
                .putString(APP_PREFERENCES_KEY, val)
                .apply();
    }

    private static String getAppProfile(Context context) {
        return getAppPreference(context).getString(APP_PREFERENCES_KEY, null);
    }

    public static void removeAppProfile(Context context) {
        getAppPreference(context)
                .edit()
                .remove(APP_PREFERENCES_KEY)
                .apply();
    }

    public static void clearAppPreferences(Context context) {
        getAppPreference(context)
                .edit()
                .clear()
                .apply();
    }

    public static void setAppFlag(Context context,String key, boolean flag) {
        getAppPreference(context)
                .edit()
                .putBoolean(key, flag)
                .apply();
    }

    public static boolean getAppFlag(Context context,String key) {
        return getAppPreference(context)
                .getBoolean(key, false);
    }

    public static void addCustomAppProfile(Context context,String key, String val) {
        getAppPreference(context)
                .edit()
                .putString(key, val)
                .apply();
    }

    public static String getCustomAppProfile(Context context,String key) {
        return getAppPreference(context).getString(key, "");
    }
}
