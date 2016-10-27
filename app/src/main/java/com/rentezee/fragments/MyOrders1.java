package com.rentezee.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rentezee.helpers.Debugger;
import com.rentezee.main.R;


public class MyOrders1 extends Fragment {

    private static final String TAG = MyOrders1.class.getSimpleName();
    private Context context;
    private TextView tvPoint1, tvPoint2, tvPoint3, tvPoint4;
    private TextView tvLine1, tvLine2, tvLine3;
    private TextView tvActiveLine, tvToPoint;
    private int lineWidth;
    private int activeColor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        context = getActivity();

        activeColor= ContextCompat.getColor(context, android.R.color.holo_green_dark);

        tvPoint1 = (TextView) view.findViewById(R.id.tvPoint1);
        tvPoint2 = (TextView) view.findViewById(R.id.tvPoint2);
        tvPoint3 = (TextView) view.findViewById(R.id.tvPoint3);
        tvPoint4 = (TextView) view.findViewById(R.id.tvPoint4);
        tvLine1 = (TextView) view.findViewById(R.id.tvLine1);
        tvLine2 = (TextView) view.findViewById(R.id.tvLine2);
        tvLine3 = (TextView) view.findViewById(R.id.tvLine3);

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

        lineWidth=(int)getActivity().getResources().getDimension(R.dimen.order_status_line_width);
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

        return view;
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
