package com.example.android.coinz;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class WalletActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_wallet);

            ViewPager viewPager = findViewById(R.id.viewPager);
            setupViewPager(viewPager);

            Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
            TextView yourCoins = findViewById(R.id.your_coins_text);
            yourCoins.setTypeface(typefaceBold);

            Typeface typefaceLight = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Light.ttf");
            TextView coinsLeftText = findViewById(R.id.number_coins_left);
            coinsLeftText.setTypeface(typefaceLight);

        }

        //Setting View Pager
        private void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(new GridViewFragment());
            viewPager.setAdapter(adapter);
        }
    }

