package android.coolweather.com.coolweather.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.coolweather.com.coolweather.R;
import android.coolweather.com.coolweather.gsonbean.Forecast;
import android.coolweather.com.coolweather.gsonbean.Weather;
import android.coolweather.com.coolweather.javabean.PlusCity;
import android.coolweather.com.coolweather.service.AutoUpdateService;
import android.coolweather.com.coolweather.util.HttpUtil;
import android.coolweather.com.coolweather.util.Utility;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    //    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private ImageView ivPlusSign;
    private ImageView ivMore;
    private FrameLayout activityWeather;


    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private String weatherId;
    Weather weatherResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 21) {
            //获得标题栏
            View decorView = getWindow().getDecorView();
            //设置activity为全屏，状态栏沉浸
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        titleCity = (TextView) findViewById(R.id.title_city);
//        titleUpdateTime= (TextView) findViewById(R.id.title_updata_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivMore = (ImageView) findViewById(R.id.iv_title_more);
        ivPlusSign = (ImageView) findViewById(R.id.iv_title_plus_sign);
        activityWeather = (FrameLayout) findViewById(R.id.activity_weather);

        //切换城市按钮监听
        titleCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击时打开滑动菜单
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //从偏好设置中获取天气信息和图片
        final String weatherString = prefs.getString("weather", null);
        String bingPic = prefs.getString("bing_pic", null);

        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            weatherId = getIntent().getStringExtra("weather_id");
            //显示放有天气信息的容器
            weatherLayout.setVisibility(View.VISIBLE);
            requestWeather(weatherId);
        }
        //如果偏好设置中有图片，则直接用glide工具库解析图片
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        //下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        //点击加号时添加城市
        ivPlusSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlusCity plusCity = new PlusCity();
                String cityName = weatherResponse.getBasic().getCityName();
                String cityId=weatherResponse.getBasic().getWeatherId();
                List<PlusCity> plusCities = DataSupport.where("cityName=?", cityName).find(PlusCity.class);
                if (plusCities.size()>0) {
                    if (cityName.equals(plusCities.get(0).getCityName())) {
                        Toast.makeText(WeatherActivity.this, "该城市已添加", Toast.LENGTH_SHORT).show();
                    } else {
                        plusCity.setCityName(cityName);
                        plusCity.setCityId(cityId);
                        plusCity.save();
                        Toast.makeText(WeatherActivity.this, "添加城市成功", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    plusCity.setCityName(cityName);
                    plusCity.setCityId(cityId);
                    plusCity.save();
                    Toast.makeText(WeatherActivity.this, "添加城市成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //点击更多时
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, CityListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void requestWeather(final String id) {
        final String weatherUrl = "http://guolin.tech/api/weather?cityid=" + id + "&key=3566843d1b204570bd56a9267cf64fcc";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                weatherResponse = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherResponse != null && "ok".equals(weatherResponse.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            weatherId = weatherResponse.basic.weatherId;
                            editor.apply();
                            showWeatherInfo(weatherResponse);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        //请求完成后关闭刷新进度条
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
//        String updateTime=weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "°C";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
//        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动指数：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
        startService(intent);
    }

    //加载必应每日一图
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
