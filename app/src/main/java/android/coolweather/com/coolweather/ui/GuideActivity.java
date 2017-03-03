package android.coolweather.com.coolweather.ui;

import android.coolweather.com.coolweather.R;
import android.coolweather.com.coolweather.fragment.FragmentA;
import android.coolweather.com.coolweather.fragment.FragmentB;
import android.coolweather.com.coolweather.fragment.FragmentC;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private FragmentPagerAdapter pagerAdapter;
    private RadioGroup rgFlip;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        rgFlip = (RadioGroup) findViewById(R.id.rg_guide_flip);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        initView();
        setListeners();

    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.vp_guide_viewpager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentC());
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);

    }

    private void setListeners() {
        rgFlip.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        rgFlip.setVisibility(View.VISIBLE);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.radioButton2:
                        rgFlip.setVisibility(View.VISIBLE);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.radioButton3:
                        viewPager.setCurrentItem(2);
                        rgFlip.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rgFlip.setVisibility(View.VISIBLE);
                        radioButton1.setChecked(true);
                        break;
                    case 1:
                        radioButton2.setChecked(true);
                        rgFlip.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        rgFlip.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
