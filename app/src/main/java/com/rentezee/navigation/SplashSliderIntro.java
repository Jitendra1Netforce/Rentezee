package com.rentezee.navigation;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentezee.helpers.Util;
import com.rentezee.main.R;

public class SplashSliderIntro extends Fragment {

    private ImageView ivIntroImage;
    private Bitmap bmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_splash_slider_intro, container, false);

        Bundle bundle=getArguments();
        ivIntroImage=(ImageView)view.findViewById(R.id.ivIntroImage);
        setImage(bundle.getInt("imageResId"));

        TextView tvIntroText=(TextView)view.findViewById(R.id.tvIntroText);
        tvIntroText.setText(bundle.getString("introText"));
        return view;
    }

    private void setImage(final int resId) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = (int) (displayMetrics.heightPixels * .7);
        final int width = displayMetrics.widthPixels;
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    bmp = Util.decodeSampledBitmapFromResource(getResources(), resId, width, height);
                    // when Bitmap conversion completed, send signal to handler
                    handler.sendEmptyMessage(0);
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                Drawable dr = new BitmapDrawable(getResources(),bmp);
                ivIntroImage.setBackground(dr);
            }
            return true;
        }
    });

}
