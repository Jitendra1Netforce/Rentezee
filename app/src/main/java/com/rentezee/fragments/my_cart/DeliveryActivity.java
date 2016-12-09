package com.rentezee.fragments.my_cart;

import android.content.Context;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;

import java.util.ArrayList;

public class DeliveryActivity extends AppCompatActivity
{

    public CoordinatorLayout coordinatorLayout;
    RecyclerView mycartProducts;
    Context context;
    LinearLayout layoutBottom;
    ArrayList<MyCartData> deliveryDatas = new ArrayList<>();
    DeliveryAdapter mydeliveryAdapter;
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
        setContentView(R.layout.activity_delivery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();



        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Cart");
        }

        deliveryDatas = MyCart.myCartDatas;


        System.out.println("delivery data ----------"+  deliveryDatas.get(0).to_date.toString());

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





        mydeliveryAdapter = new DeliveryAdapter(context,deliveryDatas,this);
        mycartProducts.setAdapter(mydeliveryAdapter);
        mycartProducts.setLayoutManager(mLayoutManager);

        mycartProducts.setNestedScrollingEnabled(false);

        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();



    }



}
