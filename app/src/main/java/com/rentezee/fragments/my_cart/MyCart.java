package com.rentezee.fragments.my_cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.adapters.ProductsAdapter;
import com.rentezee.fragments.payment.PaymentActivity;
import com.rentezee.fragments.payment.PaymentOptionActivity;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.main.Detail;
import com.rentezee.main.ProductListData;
import com.rentezee.main.R;

import java.util.ArrayList;

public class MyCart extends BaseActivity {

    public CoordinatorLayout coordinatorLayout;
    RecyclerView mycartProducts;
    Context context;
    LinearLayout layoutBottom;
    ArrayList<MyCartData> myCartDatas = new ArrayList<>();
    MyCartAdapter myCartAdapter;
    RelativeLayout relativeTotal;
    LinearLayout layoutContinue;
    String device_id;
    RelativeLayout relativeLayoutDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Cart");
        }

        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        layoutBottom = (LinearLayout) findViewById(R.id.layoutBottom);
        relativeTotal = (RelativeLayout) findViewById(R.id.relativeTotal);

        layoutContinue = (LinearLayout)findViewById(R.id.layoutContinue);

        relativeLayoutDetails = (RelativeLayout) findViewById(R.id.relativeLayoutDetails);

        mycartProducts=(RecyclerView)findViewById(R.id.lvMyCart);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myCartAdapter = new MyCartAdapter(context, myCartDatas,this);
        mycartProducts.setAdapter(myCartAdapter);
        mycartProducts.setLayoutManager(mLayoutManager);

        mycartProducts.setNestedScrollingEnabled(false);

       /*mycartProducts.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra(Constants.PRODUCT_ID, myCartDatas.get(position).product_id);
                gotoActivity(intent);
            }
        });*/

        layoutContinue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(context, PaymentActivity.class);
                gotoActivity(intent);
            }
        });



        fetchData(true);

    }

    public void fetchData(boolean reset)
    {

        if(reset)
        {
            reset();
        }
    }


    private void reset()
    {

        myCartDatas.clear();
        layoutBottom.setVisibility(View.GONE);
        relativeTotal.setVisibility(View.GONE);

        showProgressBar(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device_id", device_id);

        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/Products/listcart")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null)
                        {

                            System.out.println("data "+ result);
                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====" + productListArray.size());

                            if(productListArray.size()== 0){

                                relativeLayoutDetails.setVisibility(View.GONE);
                            }

                            for (int i = 0; i < productListArray.size(); i++)
                            {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                String cart_id = jsonObject.get("id").getAsString();
                                String product_id = jsonObject.get("product_id").getAsString();
                                String name = jsonObject.get("name").getAsString();
                                String price = jsonObject.get("price").getAsString();
                                String category_name = jsonObject.get("categories_name").getAsString();
                                String security_price = jsonObject.get("security_price").getAsString();
                                String image = jsonObject.get("image").getAsString();
                                myCartDatas.add(new MyCartData(cart_id,product_id, name, image, price, security_price,category_name));
                                relativeLayoutDetails.setVisibility(View.VISIBLE);

                            }
                            myCartAdapter.notifyDataSetChanged();

                            dismissProgressBar();
                            layoutBottom.setVisibility(View.VISIBLE);
                            relativeTotal.setVisibility(View.VISIBLE);

                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });

    }


}
