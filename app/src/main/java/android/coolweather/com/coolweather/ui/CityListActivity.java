package android.coolweather.com.coolweather.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.coolweather.com.coolweather.R;
import android.coolweather.com.coolweather.adapter.MySwipeMenuAdapter;
import android.coolweather.com.coolweather.javabean.PlusCity;
import android.coolweather.com.coolweather.util.MyDecoration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class CityListActivity extends AppCompatActivity {
    List<PlusCity> lists;
    MySwipeMenuAdapter mySwipeMenuAdapter;
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    TextView tvReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        initData();
        setAdapter();
        setListener();
    }

    private void initData() {
        lists=new ArrayList<PlusCity>();
        lists= DataSupport.findAll(PlusCity.class);
        swipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        tvReturn = (TextView) findViewById(R.id.tv_return);
    }

    private void setAdapter() {
        SwipeMenuCreator swipeMenuCreator=new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem topItem=new SwipeMenuItem(CityListActivity.this)
                        .setBackgroundDrawable(R.drawable.top_background)
                        .setText("置顶")
                        .setTextColor(Color.WHITE)
                        .setTextSize(20)
                        .setWidth(180)
                        .setHeight(140);
                swipeRightMenu.addMenuItem(topItem);
                SwipeMenuItem deleteItem=new SwipeMenuItem(CityListActivity.this)
                        .setBackgroundDrawable(R.drawable.delete_background)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setTextSize(20)
                        .setWidth(180)
                        .setHeight(140);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        };
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        swipeMenuRecyclerView.setLayoutManager(layoutManager);
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        swipeMenuRecyclerView.addItemDecoration(new MyDecoration(this,MyDecoration.VERTICAL_LIST));
        mySwipeMenuAdapter=new MySwipeMenuAdapter(lists);
        swipeMenuRecyclerView.setAdapter(mySwipeMenuAdapter);
    }

    private void setListener() {
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                switch (menuPosition){
                    case 0:
                        PlusCity plusCity=lists.get(adapterPosition);
                        lists.remove(adapterPosition);
                        lists.add(0,plusCity);
                        mySwipeMenuAdapter=new MySwipeMenuAdapter(lists);
                        swipeMenuRecyclerView.setAdapter(mySwipeMenuAdapter);
                        break;
                    case 1:
                        String cityName = lists.get(adapterPosition).getCityName();
                        DataSupport.deleteAll(PlusCity.class,"cityName=?",cityName);
                        lists.remove(adapterPosition);
                        mySwipeMenuAdapter.notifyDataSetChanged();break;
                }
            }
        });
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityListActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mySwipeMenuAdapter.setOnItemClickListener(new MySwipeMenuAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int data) {
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(CityListActivity.this).edit();
                String id=lists.get(data).getCityId();
                editor.putString("cityId",id);
                editor.commit();
                Intent intent=new Intent(CityListActivity.this,WeatherActivity.class);
                startActivity(intent);
                CityListActivity.this.finish();
            }
        });
    }
}
