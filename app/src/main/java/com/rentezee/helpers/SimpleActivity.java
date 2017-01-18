package com.rentezee.helpers;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;
import com.rentezee.views.CircleProgressBar;


/**
 * Created by JitendraSingh on 03,Sep 2016.
 * Email jitendra@live.in
 */
public abstract  class SimpleActivity extends AppCompatActivity {

    private Dialog dialog;
    RelativeLayout relativeCart;
    public static TextView tvCartCount ;


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.simple_menu, menu);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public void dismissProgressBar(){
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void gotoActivity(Intent intent){
        startActivity(intent);
    }

    public void gotoActivity(Class<?> classType) {
        Intent intent = new Intent(this, classType);
        startActivity(intent);
    }
    public void gotoActivityByClearingBackStack(Class<?> classType) {
        Intent intent = new Intent(this, classType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void showSnackBar(CoordinatorLayout coordinatorLayout, String msg) {
        if(msg!=null) {
            Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
        }else{
            Snackbar.make(coordinatorLayout, getString(R.string.generic_error), Snackbar.LENGTH_LONG).show();
        }
    }

    public void showSnackBar(CoordinatorLayout coordinatorLayout, String msg, int length) {
        if(msg!=null) {
            Snackbar.make(coordinatorLayout, msg, length).show();
        }else{
            Snackbar.make(coordinatorLayout, getString(R.string.generic_error), length).show();
        }
    }

    public void showSnackBar(CoordinatorLayout coordinatorLayout, String msg, String actionText, View.OnClickListener onClickListener) {
        if(msg!=null) {
            Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE)
                    .setAction(actionText, onClickListener)
                    .show();
        }else{
            Snackbar.make(coordinatorLayout, getString(R.string.generic_error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(actionText, onClickListener)
                    .show();
        }
    }



}
