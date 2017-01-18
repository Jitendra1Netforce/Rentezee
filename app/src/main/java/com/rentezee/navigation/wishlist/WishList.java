package com.rentezee.navigation.wishlist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

import java.util.ArrayList;

public class WishList extends BaseActivity {

    private final static String TAG = WishList.class.getSimpleName();
    private Context context;
    RecyclerView lvProducts;
    ArrayList<WishListData> wishListDatas = new ArrayList<>();
    WishListAdapter wishListAdapter;
    String user_id,device_id;
    long  userId;
    User user;
    RelativeLayout relativeEmpty;
    Button buttonMyCart;

    protected void onCreate(Bundle savedInstanceState)
    {

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

        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

       /* dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();
       */

        user = (User) new AppPreferenceManager(context).getObject(PreferenceKeys.savedUser, User.class);

        if(user != null){

            userId = user.getUserId();
            user_id = Long.toString(userId);
        }

        relativeEmpty = (RelativeLayout) findViewById(R.id.relativeEmpty) ;

        lvProducts=(RecyclerView)findViewById(R.id.lvProducts);

        buttonMyCart = (Button) findViewById(R.id.buttonMyCart);


        buttonMyCart.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DashboardContainer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lvProducts.setLayoutManager(mLayoutManager);
        wishListAdapter = new WishListAdapter(context, wishListDatas, this);
        lvProducts.setAdapter(wishListAdapter);


        if (user !=null)
        {
            load_refresh();

        }

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



    @Override
    protected void onResume() {
        super.onResume();


        try {
            invalidateOptionsMenu();
        } catch (Exception e) {

        }

        count_cart();


    }


    public  void count_cart()
    {
        // recyclerView.setVisibility(View.GONE);
        // homeDatas.clear();

        System.out.println("device_id-------------" + device_id);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device_id", device_id);


        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/Pages/dashboard.json")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        //                        System.out.println("data================" + result.toString());

                        if (result != null) {
                            JsonObject v = result.getAsJsonObject("data");

                            try {

                                String my_cart_c = v.get("my_cart").getAsString();

                                int new_my_cart = Integer.parseInt(my_cart_c);

                                tvCartCount.setText(String.valueOf(new_my_cart));


                            } catch (Exception cart) {
                            }

                            //setMenuCounter(R.id.nav_cart, new_my_cart);
                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });


    }



    public void load_refresh()
    {
        relativeEmpty.setVisibility(View.GONE);
        wishListDatas.clear();

        showProgressBar(context);
        JsonObject json = new JsonObject();
        json.addProperty("user_id", user_id);

        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/Users/wishlists")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====" + result);

                            if (productListArray.size() == 0)
                            {
                                relativeEmpty.setVisibility(View.VISIBLE);

                            }
                            else
                            {

                                relativeEmpty.setVisibility(View.GONE);

                            }


                            for (int i = 0; i < productListArray.size(); i++)
                            {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                String wishlistid = jsonObject.get("id").getAsString();
                                String product_id = jsonObject.get("product_id").getAsString();
                                String name = jsonObject.get("name").getAsString();
                                String price = jsonObject.get("price").getAsString();
                                String security_price = jsonObject.get("security_price").getAsString();
                                String category_name = jsonObject.get("categories_name").getAsString();
                                String image = "https://netforcesales.com/renteeze/webservice/files/products/" + jsonObject.get("image").getAsString();
                                wishListDatas.add(new WishListData(wishlistid,product_id, name, image, price, category_name,security_price));


                            }

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