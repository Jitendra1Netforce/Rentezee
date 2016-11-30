package com.rentezee.fragments.rentenzee_credit;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.fragments.my_cart.MyCartAdapter;
import com.rentezee.fragments.my_cart.MyCartData;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;

import java.util.ArrayList;

public class CreditActivity extends BaseActivity
{


    public CoordinatorLayout coordinatorLayout;
    RecyclerView creditProducts;
    Context context;
    LinearLayout layoutBottom;
    ArrayList<CreditData> creditDatas = new ArrayList<>();
    CreditAdapter creditAdapter;
    RelativeLayout relativeTotal;
    LinearLayout layoutContinue;
    String device_id;
    RelativeLayout relativeLayoutDetails;
    DashboardContainer dashboardContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Rentenzee Credits");
        }
        creditProducts=(RecyclerView)findViewById(R.id.lvMyCart);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        creditAdapter = new CreditAdapter(context, creditDatas,this);
        creditProducts.setAdapter(creditAdapter);
        creditProducts.setLayoutManager(mLayoutManager);

        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();

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
        creditDatas.clear();

        showProgressBar(context);

        System.out.println("device_id======"+device_id);

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

                                //relativeLayoutDetails.setVisibility(View.GONE);
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
                                creditDatas.add(new CreditData(cart_id,product_id, name, image, price, security_price,category_name));
                                relativeLayoutDetails.setVisibility(View.VISIBLE);

                            }
                            creditAdapter.notifyDataSetChanged();

                            dismissProgressBar();


                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });

    }


    @Override
    protected void onResume() {
        super.onResume();


        try {
            invalidateOptionsMenu();
        } catch (Exception e) {

        }

        dashboardContainer.count_cart();

    }





}
