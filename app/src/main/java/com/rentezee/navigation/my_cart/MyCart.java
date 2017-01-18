package com.rentezee.navigation.my_cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.navigation.chooseaddress.ChooseAddressActivity;
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
    public  static String device_id;
    RelativeLayout relativeLayoutDetails,relativelayoutDiscount,relativeEmplty;
    DashboardContainer dashboardContainer;
    TextView tvService_tax,tvOthertax,tvSerciceCharge,tv_payableAmount,tv_discount,tv_credit,tvsubTotalPrice;
    int total_per_item,all_text_charge,total_payable_amount;
    public static TextView tv_discount_heading;
    MaterialDialog dialog;
    Button buttonMyCart;


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

        relativelayoutDiscount = (RelativeLayout) findViewById(R.id.relativelayoutDiscount);

        relativeEmplty = (RelativeLayout) findViewById(R.id.relativeEmplty);

        buttonMyCart = (Button) findViewById(R.id.buttonMyCart);

        buttonMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DashboardContainer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        tv_discount = (TextView) findViewById(R.id.tv_discount);

        tv_credit = (TextView) findViewById(R.id.tv_credit);

        tvService_tax= (TextView) findViewById(R.id.tv_servicetax);

        tvSerciceCharge= (TextView) findViewById(R.id.tv_services_charges);

        tvOthertax= (TextView) findViewById(R.id.tv_othertax);

        tvsubTotalPrice = (TextView) findViewById(R.id.tv_SubPricedetail);

        tv_payableAmount = (TextView) findViewById(R.id.tv_total);


        tv_discount_heading = (TextView) findViewById(R.id.tv_discount_heading);

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
                //intent.putExtra("rentenzee_credit_amount", tv_credit.getText().toString());
                intent.putExtra("service_tax",tvService_tax.getText().toString());
                intent.putExtra("other_tax",tvOthertax.getText().toString());
                intent.putExtra("shipping_charge",tvSerciceCharge.getText().toString());
                intent.putExtra("subtotal",tvsubTotalPrice.getText().toString());
                intent.putExtra("total_amount",tv_payableAmount.getText().toString());


                gotoActivity(intent);


            }
        });



        fetchData(true);


        all_text_charge = Integer.valueOf(tv_payableAmount.getText().toString())/12+Integer.valueOf(tv_payableAmount.getText().toString())+ Integer.valueOf(tvSerciceCharge.getText().toString());

        System.out.println("all_txt_==============="+ all_text_charge);

        total_payable_amount = Integer.valueOf(tv_payableAmount.getText().toString())+all_text_charge;

        tv_payableAmount.setText(String.valueOf(total_payable_amount));


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
        relativeEmplty.setVisibility(View.GONE);
        relativelayoutDiscount.setVisibility(View.INVISIBLE);
        mycartProducts.setVisibility(View.VISIBLE);
        layoutContinue.setVisibility(View.VISIBLE);


        showProgressBar(context);

        System.out.println("device_id======"+device_id);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device_id", device_id);

        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/Products/listcart")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null)
                        {

                            try
                            {

                                System.out.println("data "+ result);

                                JsonObject details = result.getAsJsonObject("details");

                                String service_charge = details.get("service_charge").getAsString();

                                String other_tax = details.get("other_tax").getAsString();

                                String service_tax_percentage = details.get("service_tax_percentage").getAsString();

                                String rentenzee_credit = details.get("user_credit").getAsString();

                                tvService_tax.setText(service_tax_percentage);

                                tvSerciceCharge.setText(service_charge);

                                tvOthertax.setText(other_tax);

                                tv_credit.setText(rentenzee_credit);

                                JsonArray productListArray = result.getAsJsonArray("data");

                                System.out.println("data=====" + productListArray.size());

                                if(productListArray.size()== 0)
                                {


                                    relativeLayoutDetails.setVisibility(View.GONE);
                                    relativelayoutDiscount.setVisibility(View.GONE);
                                    layoutContinue.setVisibility(View.GONE);
                                    relativeEmplty.setVisibility(View.VISIBLE);
                                    mycartProducts.setVisibility(View.GONE);

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
                                    relativelayoutDiscount.setVisibility(View.VISIBLE);

                                }
                                myCartAdapter.notifyDataSetChanged();



                                dismissProgressBar();
                                layoutBottom.setVisibility(View.VISIBLE);
                                relativeTotal.setVisibility(View.VISIBLE);


                            }
                            catch (Exception e1)
                            {

                            }


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
            count_cart();
        } catch (Exception e) {

        }



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

                        if (result != null)
                        {
                            JsonObject v = result.getAsJsonObject("data");

                            try
                            {

                                String my_cart_c = v.get("my_cart").getAsString();

                                int new_my_cart = Integer.parseInt(my_cart_c);

                                tvCartCount.setText(String.valueOf(new_my_cart));


                            }
                            catch (Exception cart){}

                            //setMenuCounter(R.id.nav_cart, new_my_cart);
                        }
                        else
                        {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });


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



    public void delete_to_cart(final String cart_id) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", cart_id);

        Ion.with(context)
                .load("https://netforcesales.com/renteeze/webservice/Products/delete_cart")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null)
                        {

                              finish();

                            Intent i= new Intent(context,MyCart.class);
                            context.startActivity(i);

                            //   fetchData(true);
                          //  dashboardContainer.count_cart();
                          //  myCartAdapter.notifyDataSetChanged();


                        }

                    }

                });

    }

}
