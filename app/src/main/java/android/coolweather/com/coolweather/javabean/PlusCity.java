package android.coolweather.com.coolweather.javabean;

import org.litepal.crud.DataSupport;

/**
 * Created by Acer on 2017/2/26.
 */

public class PlusCity extends DataSupport {
    private String cityName;
    private String cityId;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
