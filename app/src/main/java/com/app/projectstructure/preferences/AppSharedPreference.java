package com.app.projectstructure.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.projectstructure.constant.AppConstant;

public class AppSharedPreference {

    //@formatter:off
    private final String TAG  = this.getClass().getSimpleName();
    private SharedPreferences        pref;
    private SharedPreferences.Editor editor;
    private Context                  mContext;
    //@formatter:on

    public AppSharedPreference(Context context) {
        this.mContext = context;
        int PRIVATE_MODE = 0;
        pref = mContext.getSharedPreferences(AppConstant.APP_PREF, PRIVATE_MODE);
    }

    public String getSharedData(String key) {
        return pref.getString(key, "");
    }

    public void setSharedData(String key, String value) {
        editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
