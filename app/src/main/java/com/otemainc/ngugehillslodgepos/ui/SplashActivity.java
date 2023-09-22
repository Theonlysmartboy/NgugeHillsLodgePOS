package com.otemainc.ngugehillslodgepos.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.otemainc.ngugehillslodgepos.R;
import com.otemainc.ngugehillslodgepos.utils.NgugeHillsLodge;
import com.otemainc.ngugehillslodgepos.utils.UserSession;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        load(NgugeHillsLodge.getContext());
    }
    private void load(Context context) {
        SystemClock.sleep(5000);
        new UserSession(context).checkLogin(MainActivity.class);
    }
}