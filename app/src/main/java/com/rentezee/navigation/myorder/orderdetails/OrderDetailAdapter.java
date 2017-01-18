package com.rentezee.navigation.myorder.orderdetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rentezee.main.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 12/12/2016.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values=new ArrayList<>();
    private List<OrderDetailsDeta> itemList;
    private Context context;
    OrderDetailHolder orderDetailHolder;


    public OrderDetailAdapter(Context context, List<OrderDetailsDeta> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_order_details, parent, false);
        orderDetailHolder = new OrderDetailHolder(view);
        return orderDetailHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        OrderDetailHolder homeHolder = (OrderDetailHolder) holder;

        homeHolder.tvProductName.setText(itemList.get(position).product_name);

        homeHolder.tvProductType.setText(itemList.get(position).product_type);
        homeHolder.tvCategoriesName.setText(itemList.get(position).categories_name);


        if(itemList.get(position).security_price.toString().equals("0"))
        {

            homeHolder.relativeDescription.setVisibility(View.GONE);

            homeHolder.tvRentalHeading.setVisibility(View.INVISIBLE);
            homeHolder.tvSecurityfeeHeading.setVisibility(View.INVISIBLE);
            homeHolder.tvrentDurationHeading.setVisibility(View.INVISIBLE);

            homeHolder.tvItemTotalHeading.setText(itemList.get(position).categories_name);


            homeHolder.tv_to.setVisibility(View.INVISIBLE);

            homeHolder.tvRentPrice.setVisibility(View.INVISIBLE);
            homeHolder.tvSecurity.setVisibility(View.INVISIBLE);
            homeHolder.tv_rent_duration.setVisibility(View.INVISIBLE);
            homeHolder.textview_to_date.setVisibility(View.INVISIBLE);
            homeHolder.textview_from_date.setVisibility(View.INVISIBLE);

            homeHolder.tvRentalSecurityHeading.setText("ITEM TOTAL");

            homeHolder.tvItemTotal.setText(itemList.get(position).total_item_amount);

        }
        else
        {
            homeHolder.relativeDescription.setVisibility(View.VISIBLE);


            homeHolder.tvRentalHeading.setVisibility(View.VISIBLE);
            homeHolder.tvSecurityfeeHeading.setVisibility(View.VISIBLE);
            homeHolder.tvrentDurationHeading.setVisibility(View.VISIBLE);
            homeHolder.tvItemTotalHeading.setVisibility(View.VISIBLE);
            homeHolder.tv_to.setVisibility(View.VISIBLE);

            homeHolder.tvRentPrice.setVisibility(View.VISIBLE);
            homeHolder.tvSecurity.setVisibility(View.VISIBLE);
            homeHolder.tv_rent_duration.setVisibility(View.VISIBLE);
            homeHolder.textview_to_date.setVisibility(View.VISIBLE);
            homeHolder.textview_from_date.setVisibility(View.VISIBLE);

            homeHolder.tvRentPrice.setText(itemList.get(position).rent_price);
            homeHolder.tvSecurity.setText(itemList.get(position).security_price);
            homeHolder.tv_rent_duration.setText(itemList.get(position).rent_duration);
            homeHolder.tvItemTotal.setText(itemList.get(position).total_item_amount);

            homeHolder.tvItemTotalHeading.setText("ITEM TOTAL");

            homeHolder.tvRentalSecurityHeading.setText("(Rental + Security)");

            // 2016-12-25 to 2016-12-
            String from_date = itemList.get(position).duration_date.toString();

            homeHolder.textview_to_date.setText(from_date.substring(0, 10));

            homeHolder.textview_from_date.setText(from_date.substring(14, 24));

        }


        Glide.with(context)
                .load(itemList.get(position).image_url)
                .centerCrop()
                //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(homeHolder.imProductImage);

    }

    private void showMessage(String s)
    {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount()
    {
        return itemList.size();
//        return itemList.size();
    }

    public String getCurrentTimeStamp()
    {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }



}
