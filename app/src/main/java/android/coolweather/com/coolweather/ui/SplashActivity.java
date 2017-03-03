package android.coolweather.com.coolweather.ui;

import android.content.Intent;
import android.coolweather.com.coolweather.R;
import android.coolweather.com.coolweather.util.SplashUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    private SplashUtil splashUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashUtil=new SplashUtil(SplashActivity.this);
        //判断是否是第一次执行
        if (splashUtil.isFirstLaunch()) {
            splashUtil.setFirstLaunch(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this,GuideActivity.class);
                    startActivity(intent);
                    finish();
                }
            },2000);
        }else{
            Intent intent=new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
