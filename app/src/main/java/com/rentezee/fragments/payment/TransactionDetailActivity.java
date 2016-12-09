package com.rentezee.fragments.payment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.rentezee.fragments.chooseaddress.ChooseAddressAdapter;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.R;

public class TransactionDetailActivity extends BaseActivity {

    Context context;
    TextView tvTransactionId,tvTotalItems,tvOrderId,tvtotalPayment,tvScheduleDate,tvItemsName1;
    String order;
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


        Intent intent = getIntent();

        String transaction_id = intent.getStringExtra("transaction_id");
        String order_id = intent.getStringExtra("order_id");
        String total_order_item = intent.getStringExtra("total_order_item");
        String total_payment = intent.getStringExtra("total_payment");
        String order_date = intent.getStringExtra("order_date");
        String delivery_date = intent.getStringExtra("delivery_date");

        tvTransactionId = (TextView) findViewById(R.id.tvTransactionId);

        tvOrderId = (TextView) findViewById(R.id.tvOrderId);
        tvTotalItems = (TextView) findViewById(R.id.tvTotalItems);
        tvtotalPayment = (TextView) findViewById(R.id.tvtotalPayment);
        tvScheduleDate = (TextView) findViewById(R.id.tvScheduleDate);

        tvItemsName1 = (TextView) findViewById(R.id.tvItemsName1);


        tvTransactionId.setText(transaction_id.toString());
        tvOrderId.setText(order_id.toString());
        tvTotalItems.setText(total_order_item.toString());
        tvtotalPayment.setText(total_payment.toString());
        tvScheduleDate.setText(delivery_date.toString().replace("Order will be deliver within",""));



        for (int i= 0; i< ChooseAddressAdapter.order_name.size();i++)
        {

            if(i==0)
            {
                 order = ChooseAddressAdapter.order_name.get(i).toString();
            }
            else {

                order = order +"\n"+ ChooseAddressAdapter.order_name.get(i).toString();
            }
            tvItemsName1.setText(order);
        }


    }
}
