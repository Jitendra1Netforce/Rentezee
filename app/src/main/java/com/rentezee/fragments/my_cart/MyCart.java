package com.rentezee.fragments.my_cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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


        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        layoutBottom = (LinearLayout) findViewById(R.id.layoutBottom);
        relativeTotal = (RelativeLayout) findViewById(R.id.relativeTotal);

        mycartProducts=(RecyclerView)findViewById(R.id.lvMyCart);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
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

        fetchData(true);

    }

    private void fetchData(boolean reset)
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

        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/products/product_list?cat_id=1")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null) {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data====="+ result.toString());

                            for (int i = 0; i < productListArray.size(); i++)
                            {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);
                                JsonObject product = jsonObject.getAsJsonObject("Product");
                                String id = product.get("id").getAsString();
                                String name = product.get("name").getAsString();
                                String price = product.get("price").getAsString();
                                String special_price = product.get("special_price").getAsString();
                                String image = "http://netforce.biz/renteeze/webservice/files/products/" + product.get("images").getAsString();
                                myCartDatas.add(new MyCartData(id, name, image, price, special_price));

                            }
                            myCartAdapter = new MyCartAdapter(context, myCartDatas);
                            mycartProducts.setAdapter(myCartAdapter);
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
