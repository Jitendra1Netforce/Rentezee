package com.rentezee.navigation.notification;

import android.content.Context;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.Login;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity
{

    RecyclerView recycler_notification;
    Context context;
    ArrayList<NotificationData> notificationDatas = new ArrayList<>();
    NotificationAdapter notificationAdapter;
    DashboardContainer dashboardContainer;
    public static String id, user_id;
    long userId;
    User user;
    String title,message,order_id,transaction_id,device_id;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Notification");
        }

        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);



        /*dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();
         */

        user = (User) new AppPreferenceManager(getApplicationContext()).getObject(PreferenceKeys.savedUser, User.class);

        if (user != null)
        {

            userId = user.getUserId();
            user_id = Long.toString(userId);

        }
        else
        {

            Intent i = new Intent(NotificationActivity.this, Login.class);
            startActivity(i);
            finish();

        }

        recycler_notification=(RecyclerView)findViewById(R.id.lvNotification);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_notification.setLayoutManager(mLayoutManager);
        notificationAdapter = new NotificationAdapter(context, notificationDatas);
        recycler_notification.setAdapter(notificationAdapter);

        recycler_notification.setNestedScrollingEnabled(false);

        reset(user_id);

    }


    private void reset(String user_id)
    {

        Log.d("USerIDDF",user_id);
        notificationDatas.clear();
        showProgressBar(context);
        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/users/offers/"+user_id)
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

                                String date = jsonObject.get("date").getAsString();

                                JsonObject product = jsonObject.getAsJsonObject("message");

                                String type = product.get("type").getAsString();

                                if(type.equalsIgnoreCase("transaction"))
                                {

                                 try{

                                     title = product.get("title").getAsString();
                                     message = product.get("message").getAsString();
                                     order_id = product.get("order_id").getAsString();
                                     transaction_id = product.get("transaction_id").getAsString();

                                 }catch (Exception ex){

                                     ex.fillInStackTrace();
                                 }



                                }
                                else{

                                     title = product.get("title").getAsString();
                                     message = product.get("message").getAsString();
                                     order_id = "";
                                     transaction_id= "";

                                }

                                notificationDatas.add(new NotificationData(type, title, message, order_id, transaction_id,date));

                            }

                            notificationAdapter.notifyDataSetChanged();

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

    @Override
    protected void onResume() {
        super.onResume();


        try {
            invalidateOptionsMenu();
        } catch (Exception e) {

        }

       // dashboardContainer.count_cart();
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




}
