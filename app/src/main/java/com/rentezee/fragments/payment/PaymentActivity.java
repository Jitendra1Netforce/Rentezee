package com.rentezee.fragments.payment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.R;

public class PaymentActivity extends BaseActivity
{

    Context context;
    Button buttonProceedPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;
        ActionBar actionBar=getSupportActionBar();

        buttonProceedPayment = (Button) findViewById(R.id.buttonProceedPayment);

        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Payment Option");
        }


        buttonProceedPayment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent transactiopn_details = new Intent(PaymentActivity.this, TransactionDetailActivity.class);
                startActivity(transactiopn_details);

            }
        });

    }



}