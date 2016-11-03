package com.rentezee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rentezee.main.R;
import com.rentezee.main.TrendingData;
import com.rentezee.pojos.mdashboard.Trending;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JitendraSingh on 10/17/2016.
 */

public class TrendingAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TrendingData> list;
    private String rs;
    private int imageWidth;

    public TrendingAdapter(Context context, ArrayList<TrendingData> list, int imageWidth) {
        this.context = context;
        this.list = list;
        rs = context.getString(R.string.rs);
        this.imageWidth=imageWidth;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public TrendingData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DashboardTrendingViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_dashboard_trending, parent, false);
            viewHolder = new DashboardTrendingViewHolder();
            viewHolder.ivTrendingImage = (ImageView) convertView.findViewById(R.id.ivTrendingImage);
            viewHolder.ivTrendingImage.getLayoutParams().height= (int) (imageWidth*0.5835);
            viewHolder.tvDashboardTrendingText = (TextView) convertView.findViewById(R.id.tvDashboardTrendingText);
            viewHolder.tvDashboardTrendingPrice = (TextView) convertView.findViewById(R.id.tvDashboardTrendingPrice);
            viewHolder.tvDashboardTrendingCategoryText = (TextView) convertView.findViewById(R.id.tvDashboardTrendingCategoryText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DashboardTrendingViewHolder) convertView.getTag();
        }


        Glide.with(context)
                .load(list.get(position).image_url)
                .centerCrop()
                //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(viewHolder.ivTrendingImage);

        viewHolder.tvDashboardTrendingText.setText(list.get(position).product_name);
        viewHolder.tvDashboardTrendingPrice.setText(rs + list.get(position).price + " per day");
        viewHolder.tvDashboardTrendingCategoryText.setText(list.get(position).category_name);

        return convertView;
    }

    private static class DashboardTrendingViewHolder {
        ImageView ivTrendingImage;
        TextView tvDashboardTrendingText, tvDashboardTrendingPrice, tvDashboardTrendingCategoryText;
    }
}
