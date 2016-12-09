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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.fragments.chooseaddress.ChooseAddressActivity;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.DashboardContainer;
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
    public static  ArrayList<MyCartData> myCartDatas = new ArrayList<>();
    MyCartAdapter myCartAdapter;
    RelativeLayout relativeTotal;
    RelativeLayout layoutContinue;
    public String device_id;
    RelativeLayout relativeLayoutDetails;
    DashboardContainer dashboardContainer;
    TextView tvService_tax,tvOthertax,tvSerciceCharge,tv_total,tv_discount,tv_credit;
    int total_per_item,all_text_charge,total_payable_amount;
    Button continue_button;



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

        layoutContinue = (RelativeLayout)findViewById(R.id.layoutContinue);

        relativeLayoutDetails = (RelativeLayout) findViewById(R.id.relativeLayoutDetails);

        tv_discount = (TextView) findViewById(R.id.tv_discount);

        tv_credit = (TextView) findViewById(R.id.tv_credit);

        tvService_tax= (TextView) findViewById(R.id.tv_servicetax);

        tvSerciceCharge= (TextView) findViewById(R.id.tv_services_charges);

        tvOthertax= (TextView) findViewById(R.id.tv_othertax);

        tv_total = (TextView) findViewById(R.id.tv_total);

        continue_button = (Button) findViewById(R.id.continue_button) ;

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


                System.out.println("hi ----------"+myCartDatas.get(0).to_date.toString());
                Intent intent = new Intent(context, ChooseAddressActivity.class);
                intent.putExtra("discount_amount",tv_discount.getText().toString());
                intent.putExtra("rentenzee_credit_amount",tv_credit.getText().toString());
                intent.putExtra("service_tax",tvService_tax.getText().toString());
                intent.putExtra("other_tax",tv_discount.getText().toString());
                intent.putExtra("service_charge",tvSerciceCharge.getText().toString());

                gotoActivity(intent);


            }
        });



        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                System.out.println("hi ----------"+myCartDatas.get(0).to_date.toString());

                Intent intent = new Intent(context, ChooseAddressActivity.class);

                intent.putExtra("discount_amount",tv_discount.getText().toString());
                intent.putExtra("rentenzee_credit_amount",tv_credit.getText().toString());
                intent.putExtra("service_tax",tvService_tax.getText().toString());
                intent.putExtra("other_tax",tvOthertax.getText().toString());
                intent.putExtra("shipping_charge",tvSerciceCharge.getText().toString());
                intent.putExtra("subtotal",tv_total.getText().toString());
                intent.putExtra("total_amount",tv_total.getText().toString());


                gotoActivity(intent);



            }
        });

        fetchData(true);

        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();


        all_text_charge = Integer.valueOf(tv_total.getText().toString())/12+Integer.valueOf(tv_total.getText().toString())+ Integer.valueOf(tvSerciceCharge.getText().toString());

        System.out.println("all_txt_==============="+ all_text_charge);

        total_payable_amount = Integer.valueOf(tv_total.getText().toString())+all_text_charge;

        tv_total.setText(String.valueOf(total_payable_amount));




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

                            JsonObject details = result.getAsJsonObject("details");

                            String service_charge = details.get("service_charge").getAsString();

                            String other_tax = details.get("other_tax").getAsString();

                            String service_tax_percentage = details.get("service_tax_percentage").getAsString();

                            tvService_tax.setText(service_tax_percentage);

                            tvSerciceCharge.setText(service_charge);

                            tvOthertax.setText(other_tax);

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====" + productListArray.size());

                            if(productListArray.size()== 0)
                            {

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


                                if(security_price.equals("0"))
                                {
                                    total_per_item = Integer.valueOf(price);
                                }
                                else
                                {

                                    total_per_item = Integer.valueOf(price)*1+Integer.valueOf(security_price);
                                }

                                String current_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                                String nextday = "";
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar c = Calendar.getInstance();
                                try {
                                    c.setTime(sdf.parse(current_date));
                                } catch (ParseException p) {
                                    p.printStackTrace();
                                }
                                c.add(Calendar.DATE, 1);  // number of days to add
                                nextday = sdf.format(c.getTime());  // dt is now the new date


                                myCartDatas.add(new MyCartData(cart_id,product_id, name, image, price, security_price,category_name,"1",current_date,nextday,String.valueOf(total_per_item)));
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

        dashboardContainer = new DashboardContainer();

        try {
            invalidateOptionsMenu();
            dashboardContainer.count_cart();
        } catch (Exception e) {

        }



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
/*
        if(MyCartAdapter.txt_from_date == true){
            MyCartHolder.textview_from_date.setText(dayOfMonth+"/"+String.valueOf(monthOfYear+1)+"/"+year);


        }
        else
        {

            MyCartHolder.textview_to_date.setText(dayOfMonth+"/"+String.valueOf(monthOfYear+1)+"/"+year);

        }*/

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
