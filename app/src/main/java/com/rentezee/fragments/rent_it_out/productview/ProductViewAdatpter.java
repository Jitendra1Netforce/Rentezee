package com.rentezee.fragments.rent_it_out.productview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rentezee.fragments.myorder.PastOrder.PastOrderData;
import com.rentezee.fragments.myorder.PastOrder.PastOrderHolder;
import com.rentezee.fragments.rent_it_out.productview_details.ProductViewDetailsActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.main.Detail;
import com.rentezee.main.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 11/21/2016.
 */
public class ProductViewAdatpter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values=new ArrayList<>();
    private List<ProductViewData> itemList;
    private Context context;
    ProductViewHolder pastOrderHolder;


    public ProductViewAdatpter(Context context, List<ProductViewData> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_productview, parent, false);
        pastOrderHolder = new ProductViewHolder(view);

        return pastOrderHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        ProductViewHolder homeHolder = (ProductViewHolder) holder;

        Glide.with(context)
                .load(itemList.get(position).image_url)
                .centerCrop()
                        //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(homeHolder.ivProductImage);

        if(itemList.get(position).security_price.toString().equals("0")){

            homeHolder.tvPerdayHeading.setVisibility(View.GONE);
            homeHolder.tvSecurityheading.setVisibility(View.GONE);
            homeHolder.tvSecurityMoney.setVisibility(View.GONE);
            homeHolder.tvPerdayRent.setVisibility(View.GONE);
            homeHolder.tvPriceData.setVisibility(View.VISIBLE);
        }
        else
        {
            homeHolder.tvPerdayHeading.setVisibility(View.VISIBLE);
            homeHolder.tvSecurityheading.setVisibility(View.VISIBLE);
            homeHolder.tvSecurityMoney.setVisibility(View.VISIBLE);
            homeHolder.tvPerdayRent.setVisibility(View.VISIBLE);
            homeHolder.tvPriceData.setVisibility(View.GONE);

        }

        homeHolder.tvProductName.setText(itemList.get(position).product_name);

        homeHolder.tvDescription.setText(itemList.get(position).description);

        homeHolder.tvProductCategoryName.setText(itemList.get(position).category_name);

        homeHolder.tvSecurityMoney.setText("\u20B9"+itemList.get(position).security_price);

        homeHolder.tvPerdayRent.setText("\u20B9" + itemList.get(position).price +" Per day");

        homeHolder.tvPriceData.setText("\u20B9" + itemList.get(position).price );

        homeHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProductViewDetailsActivity.class);
                intent.putExtra(Constants.PRODUCT_ID, itemList.get(position).product_id);
                context.startActivity(intent);

            }
        });

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

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }
}