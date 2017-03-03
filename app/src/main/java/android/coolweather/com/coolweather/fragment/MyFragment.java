package android.coolweather.com.coolweather.fragment;

import android.content.Intent;
import android.coolweather.com.coolweather.R;
import android.coolweather.com.coolweather.ui.MainActivity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Acer on 2017/2/24.
 */
public class MyFragment extends Fragment{
    public void pressedSkip(View view){
        View tvSkip = view.findViewById(R.id.tv_guide_skip);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    public void pressedSkip(View view,int id){
        View tvSkip = view.findViewById(id);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }


}
