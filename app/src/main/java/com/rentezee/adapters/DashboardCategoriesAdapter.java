package com.rentezee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rentezee.main.CategoriesData;
import com.rentezee.main.R;
import com.rentezee.pojos.mdashboard.CategoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JitendraSingh on 10/15/2016.
 */

public class DashboardCategoriesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CategoriesData> list;
    public DashboardCategoriesAdapter(Context context, ArrayList<CategoriesData> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public CategoriesData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DashboardCategoryViewHolder viewHolder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.row_dashboard_category, parent, false);
            viewHolder=new DashboardCategoryViewHolder();
            viewHolder.ivDashboardCategory=(ImageView)convertView.findViewById(R.id.ivDashboardCategory);
            viewHolder.tvDashboardCategoryText=(TextView) convertView.findViewById(R.id.tvDashboardCategoryText);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (DashboardCategoryViewHolder) convertView.getTag();
        }


        Glide.with(context)
                .load(list.get(position).image_url)
                .centerCrop()
                //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(viewHolder.ivDashboardCategory);
        viewHolder.tvDashboardCategoryText.setText(list.get(position).category_name);

        return convertView;
    }

    static class DashboardCategoryViewHolder{
        ImageView ivDashboardCategory;
        TextView tvDashboardCategoryText;
    }
}
