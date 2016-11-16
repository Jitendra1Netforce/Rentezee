package com.rentezee.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.adapters.WishListAdapter;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.R;

import java.util.ArrayList;

public class WishList extends BaseActivity {

    private final static String TAG = WishList.class.getSimpleName();
    private Context context;
    ListView lvProducts;
    ArrayList<WishListData> wishListDatas = new ArrayList<>();
    WishListAdapter wishListAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Wishlist");
        }



        lvProducts=(ListView)findViewById(R.id.lvProducts);

        load_refresh();
       /* lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra(Constants.PRODUCT_ID, productListDatas.get(position).product_id);
                gotoActivity(intent);
            }
        });
*/
    }


    private void load_refresh()
    {
        // recyclerView.setVisibility(View.GONE);

        wishListDatas.clear();

        showProgressBar(context);
        JsonObject json = new JsonObject();
        json.addProperty("user_id", "41");

        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/Users/wishlists")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data====="+result);

                            for (int i = 0; i < productListArray.size(); i++)
                            {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                String id = jsonObject.get("product_id").getAsString();
                                String name = jsonObject.get("name").getAsString();
                                String price = jsonObject.get("price").getAsString();
                                String category_name = jsonObject.get("categories_name").getAsString();
                                String image = "http://netforce.biz/renteeze/webservice/files/products/" + jsonObject.get("image").getAsString();
                                wishListDatas.add(new WishListData(id, name, image, price, category_name));

                            }
                            wishListAdapter = new WishListAdapter(context, wishListDatas);
                            lvProducts.setAdapter(wishListAdapter);
                            wishListAdapter.notifyDataSetChanged();


                            dismissProgressBar();

                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });

    }

}