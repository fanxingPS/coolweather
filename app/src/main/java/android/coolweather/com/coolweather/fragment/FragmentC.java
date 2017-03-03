package android.coolweather.com.coolweather.fragment;

import android.coolweather.com.coolweather.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Acer on 2017/2/24.
 */

public class FragmentC extends MyFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, null);
        pressedSkip(view,R.id.btn_guide_start);
        ImageView gifImageView = (ImageView) view.findViewById(R.id.iv_guide_gif);
        Glide.with(this).load(R.drawable.fragment3).into(gifImageView);
        return view;
    }
}
