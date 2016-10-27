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
import com.rentezee.pojos.mdashboard.CategoryData;

import java.util.List;

/**
 * Created by JitendraSingh on 10/15/2016.
 */

public class DashboardCategoriesAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryData> list;
    public DashboardCategoriesAdapter(Context context, List<CategoryData> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public CategoryData getItem(int position) {
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

        CategoryData categoryData =getItem(position);
        Glide.with(context)
                .load(categoryData.getImageUrl())
                .centerCrop()
                //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(viewHolder.ivDashboardCategory);
        viewHolder.tvDashboardCategoryText.setText(categoryData.getCategoryName());

        return convertView;
    }

    static class DashboardCategoryViewHolder{
        ImageView ivDashboardCategory;
        TextView tvDashboardCategoryText;
    }
}
