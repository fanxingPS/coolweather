package android.coolweather.com.coolweather.adapter;

import android.coolweather.com.coolweather.R;
import android.coolweather.com.coolweather.javabean.PlusCity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by Acer on 2017/3/2.
 */

public class MySwipeMenuAdapter extends SwipeMenuAdapter<MySwipeMenuAdapter.MyViewHolder> implements View.OnClickListener{
    private List<PlusCity> list;
    private OnRecyclerViewItemClickListener mOnItemClickListener =null;
    public MySwipeMenuAdapter(List<PlusCity> list) {
        this.list = list;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plus_city, parent, false);
        return view;
    }

    @Override
    public MyViewHolder onCompatCreateViewHolder(final View realContentView, int viewType) {

        final MyViewHolder myViewHolder = new MyViewHolder(realContentView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlusCity plusCity=list.get(position);
        holder.tvPlusCity.setText(plusCity.getCityName());
        holder.tvPlusCity.setTag(position);
        holder.tvPlusCity.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener !=null){
            mOnItemClickListener.onItemClick(v, Integer.parseInt(v.getTag().toString()));
        }
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvPlusCity;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvPlusCity = (TextView) itemView.findViewById(R.id.tv_plus_city);
        }
    }
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,int data);
    }
}
