package com.rentezee.navigation.myorder.activeorder;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.R;
import java.util.ArrayList;


public class ActiveOrders extends BaseActivity {

    private static final String TAG = ActiveOrders.class.getSimpleName();
    RecyclerView recycler_notification;
    Context context;
    ArrayList<ActiveOrderdata> myOrderdatas = new ArrayList<>();
    ActiveOrderAdapter myOrderAdapter;



    protected  void  onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Order");
        }

        recycler_notification=(RecyclerView)findViewById(R.id.lvProducts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_notification.setLayoutManager(mLayoutManager);

        recycler_notification.setNestedScrollingEnabled(false);
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
        myOrderdatas.clear();
        showProgressBar(context);
        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/products/product_list?cat_id=1")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        if (result != null)
                        {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====" + result.toString());

                            for (int i = 0; i < productListArray.size(); i++)
                            {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);
                                JsonObject product = jsonObject.getAsJsonObject("Product");
                                String id = product.get("id").getAsString();
                                String name = product.get("name").getAsString();
                                String price = product.get("price").getAsString();
                                String special_price = product.get("special_price").getAsString();
                                String image = "https://netforcesales.com/renteeze/webservice/files/products/" + product.get("images").getAsString();
                                myOrderdatas.add(new ActiveOrderdata(id, name, image, price, special_price));

                            }
                            myOrderAdapter = new ActiveOrderAdapter(context, myOrderdatas);
                            recycler_notification.setAdapter(myOrderAdapter);
                            myOrderAdapter.notifyDataSetChanged();

                            dismissProgressBar();


                        }
                        else
                        {
                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });

    }
}