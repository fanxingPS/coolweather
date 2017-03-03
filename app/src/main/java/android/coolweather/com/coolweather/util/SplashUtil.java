package android.coolweather.com.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Acer on 2017/2/20.
 */

public class SplashUtil {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public SplashUtil(Context context){
        sp= PreferenceManager.getDefaultSharedPreferences(context);
        editor=sp.edit();
//        Log.i("aaa", "sp=" + sp.hashCode() + "editor=" + editor);
    }
    public static boolean isFirstLaunch(){
        return sp.getBoolean("FIRST_LAUNCH",true);
    }
    public static void setFirstLaunch(Boolean f){
        editor.putBoolean("FIRST_LAUNCH", f);
        editor.commit();
    }
}
