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
import com.rentezee.fragments.chooseaddress.ChooseAddressActivity;
import com.rentezee.fragments.payment.PaymentActivity;
import com.rentezee.fragments.payment.PaymentOptionActivity;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.Detail;
import com.rentezee.main.ProductListData;
import com.rentezee.main.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyCart extends BaseActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener
{

    public CoordinatorLayout coordinatorLayout;
    RecyclerView mycartProducts;
    Context context;
    LinearLayout layoutBottom;
    ArrayList<MyCartData> myCartDatas = new ArrayList<>();
    MyCartAdapter myCartAdapter;
    RelativeLayout relativeTotal;
    LinearLayout layoutContinue;
    public String device_id;
    RelativeLayout relativeLayoutDetails;
    DashboardContainer dashboardContainer;

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

                Intent intent = new Intent(context, ChooseAddressActivity.class);
                gotoActivity(intent);
            }
        });


        fetchData(true);

        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();



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



    @Override
    protected void onResume() {
        super.onResume();


        try {
            invalidateOptionsMenu();
        } catch (Exception e) {

        }

        dashboardContainer.count_cart();

    }



    public void pick_date()
    {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MyCart.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {

        if(MyCartAdapter.txt_from_date == true){
            MyCartHolder.textview_from_date.setText(dayOfMonth+"/"+String.valueOf(monthOfYear+1)+"/"+year);


        }
        else
        {

            MyCartHolder.textview_to_date.setText(dayOfMonth+"/"+String.valueOf(monthOfYear+1)+"/"+year);

        }

    }



    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        //month_txt.setText(time);
    }



}
