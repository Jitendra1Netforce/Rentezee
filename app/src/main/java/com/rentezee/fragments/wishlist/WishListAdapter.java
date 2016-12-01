package com.rentezee.fragments.wishlist;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.fragments.my_cart.MyCart;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.VolleyErrorHandler;
import com.rentezee.helpers.VolleyGsonRequest;
import com.rentezee.main.AppController;
import com.rentezee.main.Detail;
import com.rentezee.main.R;
import com.rentezee.pojos.GenericResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 11/10/2016.
 */
public class WishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values=new ArrayList<>();
    private List<WishListData> itemList;
    private Context context;
    WishListHolder myCartHolder;
    WishList wishList;

    public WishListAdapter(Context context, List<WishListData> itemList, WishList wishList)
    {
        this.wishList = wishList;
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.row_wishlist, parent, false);
        myCartHolder = new WishListHolder(view);
        return myCartHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        WishListHolder homeHolder = (WishListHolder) holder;
        homeHolder.tvProductName.setText(itemList.get(position).product_name);

        if(itemList.get(position).security_price.toString().equals("0")){

            homeHolder.tvRentPrice.setText("\u20B9 " + itemList.get(position).price);
        }
        else {
            homeHolder.tvRentPrice.setText("\u20B9 " + itemList.get(position).price + " per day");
        }
        homeHolder.tvCategoriesName.setText(itemList.get(position).category_name);

        Glide.with(context)
                .load(itemList.get(position).image_url)
                .centerCrop()//.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(homeHolder.imProductImage);


        homeHolder.layoutRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_wihlist(itemList.get(position).wishlist_id.toString());
            }
        });

        homeHolder.layoutMoveToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, Detail.class);
                intent.putExtra(Constants.PRODUCT_ID, itemList.get(position).product_id);
                context.startActivity(intent);
            }
        });

        homeHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, Detail.class);
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
    public int getItemCount() {
        return itemList.size();
//        return itemList.size();
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }


    private void delete_wihlist(String wishlist_id)
    {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", wishlist_id);

        Ion.with(context)
                .load("http://netforce.biz/renteeze/webservice/Users/delete_wishlist")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        if (result != null)
                        {
                            wishList.load_refresh();
                        }

                    }
    });

    }






    }


