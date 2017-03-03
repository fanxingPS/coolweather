package android.coolweather.com.coolweather.ui;


import android.content.Intent;
import android.coolweather.com.coolweather.R;
import android.coolweather.com.coolweather.adapter.MySwipeMenuAdapter;
import android.coolweather.com.coolweather.javabean.PlusCity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
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
        mySwipeMenuAdapter=new MySwipeMenuAdapter(lists);
    }

    private void setAdapter() {
        mySwipeMenuAdapter=new MySwipeMenuAdapter(lists);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        swipeMenuRecyclerView.setLayoutManager(layoutManager);
        SwipeMenuCreator swipeMenuCreator=new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem topItem=new SwipeMenuItem(CityListActivity.this)
                        .setBackgroundDrawable(R.drawable.top_background)
                        .setText("置顶")
                        .setTextColor(Color.WHITE)
                        .setTextSize(22)
                        .setWidth(180)
                        .setHeight(140);
                swipeRightMenu.addMenuItem(topItem);
                SwipeMenuItem deleteItem=new SwipeMenuItem(CityListActivity.this)
                        .setBackgroundDrawable(R.drawable.delete_background)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setTextSize(22)
                        .setWidth(180)
                        .setHeight(140);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        };
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        swipeMenuRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
            }
        });
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
                        mySwipeMenuAdapter.notifyDataSetChanged();break;
                    case 1:
                        String cityName = lists.get(adapterPosition).getCityName();
                        lists.remove(adapterPosition);
                        DataSupport.deleteAll(PlusCity.class,"cityName=?",cityName);
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
    }
}
