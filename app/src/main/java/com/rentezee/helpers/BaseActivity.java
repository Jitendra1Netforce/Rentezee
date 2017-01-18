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

import com.rentezee.navigation.my_cart.MyCart;
import com.rentezee.main.R;
import com.rentezee.main.Search;
import com.rentezee.views.CircleProgressBar;


/**
 * Created by JitendraSingh on 03,Sep 2016.
 * Email jitendra@live.in
 */
public abstract  class BaseActivity extends AppCompatActivity {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_container, menu);

        View view = menu.findItem(R.id.menu_cart).getActionView();

         tvCartCount = (TextView) view.findViewById(R.id.tvCartCount);

        relativeCart = (RelativeLayout) view.findViewById(R.id.relativeCart);


        relativeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoActivity(MyCart.class);
            }
        });

        //tvCartCount.setText(String.valueOf(DashboardContainer.cart_count));

     /*   ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setRepeatMode(ScaleAnimation.INFINITE);
        scale.setRepeatCount(Integer.MAX_VALUE);
        scale.setInterpolator(new OvershootInterpolator());
        tvCartCount.startAnimation(scale);*/

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_search:
                /*hideActionBar();
                addFragment(new Search1(), Constants.SEARCH_FRAGMENT);*/
                gotoActivity(Search.class);
                break;
            case R.id.menu_cart:
                gotoActivity(MyCart.class);
                break;
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
