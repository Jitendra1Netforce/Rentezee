package com.rentezee.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by John on 11/4/2016.
 */
public class SearchAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<SearchData> list;
    private String rs;

    public SearchAdapter(Context context, ArrayList<SearchData> list) {
        this.context = context;
        this.list = list;
        rs = context.getString(R.string.rs);
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public SearchData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_search, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivProductImage = (ImageView) convertView.findViewById(R.id.ivProductImage);
            viewHolder.tvProductName = (TextView) convertView.findViewById(R.id.tvProductName);
            viewHolder.tvProductCategoryName = (TextView) convertView.findViewById(R.id.tvProductCategoryName);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Glide.with(context)
                .load(list.get(position).image_url)
                .centerCrop()
                        //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(viewHolder.ivProductImage);

        viewHolder.tvProductName.setText(list.get(position).product_name);
        viewHolder.tvProductCategoryName.setText(list.get(position).category_name);
        viewHolder.tvPrice.setText(rs + list.get(position).price + " per day");
        return convertView;
    }

    private static class ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductCategoryName, tvPrice;
    }
}
