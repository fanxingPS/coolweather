package android.coolweather.com.coolweather.util;

import android.coolweather.com.coolweather.javabean.City;
import android.coolweather.com.coolweather.javabean.County;
import android.coolweather.com.coolweather.javabean.Province;
import android.coolweather.com.coolweather.gsonbean.Weather;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acer on 2017/2/11.
 */

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces=new JSONArray(response);
                for (int i=0;i<allProvinces.length();i++){
                    JSONObject object = allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(object.getString("name"));
                    province.setProvinceCode(object.getInt("id"));
                    province.save();
                }
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    return false;
    }
    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCity=new JSONArray(response);
                for (int i=0;i<allCity.length();i++){
                    JSONObject object = allCity.getJSONObject(i);
                    City city=new City();
                    city.setCityName(object.getString("name"));
                    city.setCityCode(object.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounty=new JSONArray(response);
                for (int i=0;i<allCounty.length();i++){
                    JSONObject object = allCounty.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(object.getString("name"));
                    county.setWeatherId(object.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析天气数据
     * @param response
     * @return
     */
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject object=new JSONObject(response);
            JSONArray array=object.getJSONArray("HeWeather");
            String weatherContent=array.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
