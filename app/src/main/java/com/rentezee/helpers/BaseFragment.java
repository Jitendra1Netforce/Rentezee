package com.rentezee.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.VolleyError;
import com.rentezee.main.R;
import com.rentezee.views.CircleProgressBar;


/**
 * Created by JitendraSingh on 14,Sep 2016.
 * Email jitendra@live.in
 */
public abstract class BaseFragment extends Fragment {
    private Dialog dialog;

    public void showProgressBar(Context context) {
        try {
            dismissProgressBar();
            dialog = new Dialog(context, R.style.TransparentDialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_progress_bar, null);
            CircleProgressBar progressBar=(CircleProgressBar) view.findViewById(R.id.progress_bar);
            /*progressBar.setColorSchemeResources(android.R.color.white,
                    android.R.color.holo_red_dark,
                    android.R.color.black,
                    R.color.pink);*/
            progressBar.setColorSchemeResources(android.R.color.white);
            dialog.setContentView(view);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressBar() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
