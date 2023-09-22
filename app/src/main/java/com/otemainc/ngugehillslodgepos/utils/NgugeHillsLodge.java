package com.otemainc.ngugehillslodgepos.utils;

import android.app.Application;
import android.content.Context;

public class NgugeHillsLodge extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
    public static Context getContext(){
        return context;
    }
}
