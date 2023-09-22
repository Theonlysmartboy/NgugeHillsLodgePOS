package com.otemainc.ngugehillslodgepos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.otemainc.ngugehillslodgepos.R;
import com.otemainc.ngugehillslodgepos.ui.MainActivity;
import com.otemainc.ngugehillslodgepos.ui.OnBoardingActivity;
import com.otemainc.ngugehillslodgepos.utils.NgugeHillsLodge;
import com.otemainc.ngugehillslodgepos.utils.UserSession;

public class OnboardingViewPagerAdapter extends PagerAdapter {
    Context ctx;

    public OnboardingViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater= (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.on_boarding_layout,container,false);

        ImageView logo=view.findViewById(R.id.logo);

        ImageView ind = view.findViewById(R.id.ind);
        ImageView ind1=view.findViewById(R.id.ind1);
        ImageView ind2=view.findViewById(R.id.ind2);
        ImageView ind3=view.findViewById(R.id.ind3);

        TextView title=view.findViewById(R.id.title);
        TextView desc=view.findViewById(R.id.desc);

        ImageView next=view.findViewById(R.id.next);
        ImageView back=view.findViewById(R.id.back);

        Button btnGetStarted=view.findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(v -> new UserSession(NgugeHillsLodge.getContext()).checkLogin(MainActivity.class));
        next.setOnClickListener(v -> OnBoardingActivity.viewPager.setCurrentItem(position+1));

        back.setOnClickListener(v -> OnBoardingActivity.viewPager.setCurrentItem(position-1));

        switch (position)
        {
            case 0:
                logo.setImageResource(R.drawable.logo);
                ind.setImageResource(R.drawable.selected);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.unselected);

                title.setText(R.string.welcome_text);
                desc.setText(R.string.welcome_decsr);
                back.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                break;
            case 1:
                logo.setImageResource(R.drawable.room);
                ind.setImageResource(R.drawable.unselected);
                ind1.setImageResource(R.drawable.selected);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.unselected);

                title.setText(R.string.room_booking_text);
                desc.setText(R.string.room_booking_descr);
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                break;
            case 2:
                logo.setImageResource(R.drawable.chicken_in_sauce);
                ind.setImageResource(R.drawable.unselected);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.selected);
                ind3.setImageResource(R.drawable.unselected);

                title.setText(R.string.food_title);
                desc.setText(R.string.food_text);
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                break;
            case 3:
                logo.setImageResource(R.drawable.logo3);
                ind.setImageResource(R.drawable.unselected);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.selected);

                title.setText(R.string.payment_title);
                desc.setText(R.string.payment_descr);
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
