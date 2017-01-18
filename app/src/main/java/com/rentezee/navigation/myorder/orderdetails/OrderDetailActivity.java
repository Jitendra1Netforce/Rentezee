package com.rentezee.navigation.myorder.orderdetails;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.SimpleActivity;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.Login;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderDetailActivity extends SimpleActivity
{


    RecyclerView recycler_notification;
    Context context;
    ArrayList<OrderDetailsDeta> notificationDatas = new ArrayList<>();
    OrderDetailAdapter orderDetailAdapter;
    DashboardContainer dashboardContainer;
    public static String id, user_id;
    long userId;
    User user;
    public  static  String order_number;
    String title,message,order_id,transaction_id;
    TextView txtpayable_amount,txtAddress,txtAddressDetails,txtAddressCity;
    String payable_amouunt,address_label,address,pincode;
    RelativeLayout relativelayout;
    String service_tax,otherTax,serviceCharge,subtotal,order_date,order_Id;
    TextView tvservice_tax, tvother_tax,tvservice_charge,tvsub_total,tvOrderDate,tvOrderId;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Order Detail");
        }

        Intent intent = getIntent();

        order_number = intent.getStringExtra("order_number");

        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();

        user = (User) new AppPreferenceManager(getApplicationContext()).getObject(PreferenceKeys.savedUser, User.class);

        if (user != null)
        {
            userId = user.getUserId();
            user_id = Long.toString(userId);

        }
        else
        {
            Intent i = new Intent(OrderDetailActivity.this, Login.class);
            startActivity(i);
            finish();

        }

        relativelayout =  (RelativeLayout) findViewById(R.id.relativelayout);

        tvOrderId = (TextView) findViewById(R.id.tvOderId);

        tvOrderDate = (TextView) findViewById(R.id.tvOrderDate);

        tvservice_tax = (TextView) findViewById(R.id.tv_servicetax);

        tvother_tax = (TextView) findViewById(R.id.tv_othertax);

        tvservice_charge = (TextView) findViewById(R.id.tv_services_charges);

        tvsub_total = (TextView) findViewById(R.id.tv_SubPricedetail);

        txtpayable_amount = (TextView) findViewById(R.id.txtpayable_amount) ;

        txtAddress = (TextView) findViewById(R.id.txtAddress);

        txtAddressDetails = (TextView) findViewById(R.id.txtAddressDetails);

        txtAddressCity = (TextView) findViewById(R.id.txtAddressCity);

        recycler_notification = (RecyclerView) findViewById(R.id.lvMyCart);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_notification.setLayoutManager(mLayoutManager);
        orderDetailAdapter = new OrderDetailAdapter(context, notificationDatas);
        recycler_notification.setAdapter(orderDetailAdapter);

        recycler_notification.setNestedScrollingEnabled(false);

        reset(order_number);

    }




    public String parseDateToddMMyyyy(String time)
    {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }



    private void reset(String order_number)
    {
        System.out.println("order_id==========" + order_number);

        relativelayout.setVisibility(View.INVISIBLE);
        notificationDatas.clear();
        showProgressBar(context);
        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/orders/order_detail/" + order_number)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {

                            try {

                                System.out.println("data=====" + result.toString());

                                JsonObject jsonObject_data = result.getAsJsonObject("data");

                                order_date = jsonObject_data.get("order_date").getAsString();

                                payable_amouunt = jsonObject_data.get("payable_amount").getAsString();

                                 service_tax = jsonObject_data.get("serviceTax").getAsString();

                                otherTax = jsonObject_data.get("otherTax").getAsString();

                                serviceCharge = jsonObject_data.get("serviceCharge").getAsString();

                                subtotal = jsonObject_data.get("subtotal").getAsString();

                                JsonObject json_address = jsonObject_data.getAsJsonObject("all_address");

                                order_Id = json_address.get("order_id").getAsString();

                                address_label = json_address.get("address_label").getAsString();

                                address = json_address.get("address").getAsString();

                                pincode = json_address.get("pincode").getAsString();

                                JsonArray productListArray = jsonObject_data.getAsJsonArray("order_detail");

                                for (int i = 0; i < productListArray.size(); i++)
                                {
                                    JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                    String product_name = jsonObject.get("product_name").getAsString();

                                    String product_type = jsonObject.get("product_type").getAsString();

                                    String category_name = jsonObject.get("categories_name").getAsString();

                                    String rent_price = jsonObject.get("rent_price").getAsString();

                                    String security_price = jsonObject.get("security_price").getAsString();

                                    String item_total = jsonObject.get("item_total").getAsString();

                                    String rent_duration = jsonObject.get("rent_duration").getAsString();

                                    String rent_duration_date = jsonObject.get("rent_duration_date").getAsString();

                                    String image_url = jsonObject.get("image_url").getAsString();

                                    notificationDatas.add(new OrderDetailsDeta(product_name, product_type, category_name, rent_price, security_price, item_total, rent_duration, rent_duration_date, image_url));

                                }

                                orderDetailAdapter.notifyDataSetChanged();

                                String date_value = parseDateToddMMyyyy(order_date.toString());

                                tvOrderId.setText(order_Id);

                                tvOrderDate.setText(date_value);

                                tvservice_tax.setText(service_tax.toString());

                                tvother_tax.setText(otherTax.toString());

                                tvservice_charge.setText(serviceCharge.toString());

                                tvsub_total.setText(subtotal.toString());

                                txtpayable_amount.setText("\u20B9 "+payable_amouunt.toString());

                                txtAddress.setText(address_label.toString());

                                txtAddressDetails.setText(address.toString() + " " + pincode.toString());

                                relativelayout.setVisibility(View.VISIBLE);
                                dismissProgressBar();




                            }
                            catch (NullPointerException nu){



                            }



                            }
                            else
                            {
                                dismissProgressBar();
                                Log.e("error", e.toString());
                            }
                        }
                    }

                    );

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