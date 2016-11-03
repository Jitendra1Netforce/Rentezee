package com.rentezee.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.rentezee.adapters.ViewPagerAdapter;
import com.rentezee.fragments.DashboardSliderImage;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.Util;
import com.rentezee.helpers.VolleyErrorHandler;
import com.rentezee.helpers.VolleyGsonRequest;
import com.rentezee.pojos.mdashboard.Slider;
import com.rentezee.pojos.mdetail.ProductDetail;
import com.rentezee.pojos.mdetail.Response;

import java.util.ArrayList;
import java.util.Locale;

import me.relex.circleindicator.CircleIndicator;

public class Detail extends BaseActivity
{

    private static  final String TAG=Detail.class.getSimpleName();
    private Context context;
    private CoordinatorLayout coordinatorLayout;
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private TextView tvProductName, tvProductCategoryName, tvDescription;
    private TextView tvSecurityMoney, tvPerDayRent;
    private CardView cardViewDescription;
    private LinearLayout layoutPrice, layoutBottom;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

         actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        //Screen height and width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.getLayoutParams().height = (int) (displayMetrics.widthPixels * .719);
        indicator = (CircleIndicator) findViewById(R.id.indicator);

        tvProductName=(TextView)findViewById(R.id.tvProductName);
        tvProductCategoryName=(TextView)findViewById(R.id.tvProductCategoryName);
        tvDescription=(TextView)findViewById(R.id.tvDescription);
        tvSecurityMoney=(TextView)findViewById(R.id.tvSecurityMoney);
        tvPerDayRent=(TextView)findViewById(R.id.tvPerDayRent);

        cardViewDescription=(CardView) findViewById(R.id.cardViewDescription);
        layoutPrice=(LinearLayout)findViewById(R.id.layoutPrice);
        layoutBottom=(LinearLayout)findViewById(R.id.layoutBottom);

        fetchDetail(getIntent().getIntExtra(Constants.PRODUCT_ID, 0));
    }

    private void fetchDetail(int productId){
        try {
            String url = Constants.API + "product/"+String.valueOf(productId); //URL to hit
            showProgressBar(context);
            AppController.getInstance().cancelPendingRequest(TAG);
            VolleyGsonRequest<Response> gsonRequest = new VolleyGsonRequest<>(url,
                    null,
                    new com.android.volley.Response.Listener<Response>() {
                        @Override
                        public void onResponse(Response response) {
                            dismissProgressBar();
                            Debugger.i(TAG, "Response " + response);
                            if (response != null) {
                                if (response.isSuccess()) {
                                    processProductDetailResponse(response);
                                } else {
                                    showSnackBar(coordinatorLayout, response.getMessage());
                                }
                            } else {
                                showSnackBar(coordinatorLayout, getString(R.string.generic_error));
                            }
                        }
                    },

                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgressBar();
                            error.printStackTrace();
                            showSnackBar(coordinatorLayout, VolleyErrorHandler.getMessage(context, error));
                        }
                    },
                    Response.class,
                    null
            );
            AppController.getInstance().addToRequestQueue(gsonRequest, TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processProductDetailResponse(Response response){

        ProductDetail productDetail=response.getData();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ArrayList<String> images=productDetail.getImages();
        for (String imageUrl : images) {
            Fragment fragment = new DashboardSliderImage();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.URL, imageUrl);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, "");

        }
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        actionBar.setTitle(productDetail.getProductName());
        tvProductName.setText(productDetail.getProductName());
        tvProductCategoryName.setText(productDetail.getProductName());
        tvDescription.setText(productDetail.getDescription());
        cardViewDescription.setVisibility(View.VISIBLE);

        String rs=getString(R.string.rs);

        tvSecurityMoney.setText(String.format(Locale.ENGLISH, "%s%s", rs, String.valueOf(productDetail.getSecurityPrice())));
        tvPerDayRent.setText(String.format(Locale.ENGLISH, "%s%s", rs, String.valueOf(productDetail.getPrice())));
        layoutPrice.setVisibility(View.VISIBLE);

        layoutBottom.setVisibility(View.VISIBLE);
    }
}
