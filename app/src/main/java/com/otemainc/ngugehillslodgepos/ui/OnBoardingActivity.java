package com.otemainc.ngugehillslodgepos.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.otemainc.ngugehillslodgepos.R;
import com.otemainc.ngugehillslodgepos.adapter.OnboardingViewPagerAdapter;
import com.otemainc.ngugehillslodgepos.utils.UserSession;

public class OnBoardingActivity extends AppCompatActivity {
    public static ViewPager viewPager;
    OnboardingViewPagerAdapter adapter;
    private UserSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        viewPager=findViewById(R.id.viewpager);
        adapter=new OnboardingViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        session = new UserSession(this);
        if (session.isFirstTimeLaunch())
        {
            session.setFirstTimeLaunch(false);
        }
        else
        {
            Intent intent=new Intent(OnBoardingActivity.this,SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}