package android.coolweather.com.coolweather.fragment;

import android.coolweather.com.coolweather.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Acer on 2017/2/24.
 */

public class FragmentB extends MyFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, null);
        pressedSkip(view);
        return view;
    }
}
