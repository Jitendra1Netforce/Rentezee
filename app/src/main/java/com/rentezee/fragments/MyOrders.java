package com.rentezee.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Debugger;
import com.rentezee.main.R;


public class MyOrders extends BaseActivity {

    private static final String TAG = MyOrders.class.getSimpleName();
    private Context context;
    private TextView tvPoint1, tvPoint2, tvPoint3, tvPoint4;
    private TextView tvLine1, tvLine2, tvLine3;
    private TextView tvActiveLine, tvToPoint;
    private int lineWidth;
    private int activeColor;


    protected  void  onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("MY Order");
        }


        activeColor= ContextCompat.getColor(context, android.R.color.holo_green_dark);

        tvPoint1 = (TextView)findViewById(R.id.tvPoint1);
        tvPoint2 = (TextView) findViewById(R.id.tvPoint2);
        tvPoint3 = (TextView) findViewById(R.id.tvPoint3);
        tvPoint4 = (TextView) findViewById(R.id.tvPoint4);
        tvLine1 = (TextView) findViewById(R.id.tvLine1);
        tvLine2 = (TextView) findViewById(R.id.tvLine2);
        tvLine3 = (TextView) findViewById(R.id.tvLine3);

        /*int colorFrom = ContextCompat.getColor(context, android.R.color.holo_red_dark);
        int colorTo =  ContextCompat.getColor(context, android.R.color.black);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(2500); // milliseconds

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                tv.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();*/

        lineWidth=(int)getApplicationContext().getResources().getDimension(R.dimen.order_status_line_width);
        Debugger.i("Width", lineWidth+"");
        lineWidth+=1;
        StringBuilder builder=new StringBuilder();
        for(int i=0; i<lineWidth; i++){
            builder.append(" ");
        }
        tvLine1.setText(builder.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < lineWidth; i++) {
                        Thread.sleep(30);
                        Log.i("Thread",i+" ");
                        handler.sendEmptyMessage(i);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Debugger.i("What", msg.what+" ");
            msg.getData();
            Spannable spannableString = new SpannableString(tvLine1.getText());
            spannableString.setSpan(new BackgroundColorSpan(activeColor),
                    0,
                    msg.what,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tvLine1.setText(spannableString, TextView.BufferType.SPANNABLE);
            return false;
        }
    });

}
