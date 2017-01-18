package com.rentezee.navigation.transaction_details;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentezee.navigation.payumoneysdk.PayUMoneyActivity;
import com.rentezee.helpers.SimpleActivity;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;

import java.util.ArrayList;

public class TransactionDetailActivity extends SimpleActivity
{

    Context context;
    TextView tvTransactionId,tvTotalItems,tvOrderId,tvtotalPayment,tvScheduleDate,tvItemsName1;
    String order;
    LinearLayout layoutHome;
    ArrayList<String>  order_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();

        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Transaction Detail");
        }

        order_name = PayUMoneyActivity.order_name;

        Intent intent = getIntent();

        String transaction_id = intent.getStringExtra("transaction_id");
        String order_id = intent.getStringExtra("order_id");
        String total_order_item = intent.getStringExtra("total_order_item");
        String total_payment = intent.getStringExtra("total_payment");
        String order_date = intent.getStringExtra("order_date");
        String delivery_date = intent.getStringExtra("delivery_date");

        tvTransactionId = (TextView) findViewById(R.id.tvTransactionId);

        layoutHome = (LinearLayout) findViewById(R.id.layoutHome);



        tvOrderId = (TextView) findViewById(R.id.tvOrderId);
        tvTotalItems = (TextView) findViewById(R.id.tvTotalItems);
        tvtotalPayment = (TextView) findViewById(R.id.tvtotalPayment);
        tvScheduleDate = (TextView) findViewById(R.id.tvScheduleDate);

        tvItemsName1 = (TextView) findViewById(R.id.tvItemsName1);


        tvTransactionId.setText(transaction_id.toString());
        tvOrderId.setText(order_id.toString());
        tvTotalItems.setText(total_order_item.toString());

        Double d = new Double(total_payment.toString());

        int total_payable_amount =d.intValue();

        tvtotalPayment.setText(String.valueOf(total_payable_amount));

        tvScheduleDate.setText(delivery_date.toString().replace("Order will be deliver within", ""));




        layoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DashboardContainer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        for (int i= 0; i< PayUMoneyActivity.order_name.size();i++)
        {

            if(i==0)
            {
                 order = PayUMoneyActivity.order_name.get(i).toString();
            }
            else {

                order = order +"\n"+ PayUMoneyActivity.order_name.get(i).toString();
            }
            tvItemsName1.setText(order);
        }


    }
}
