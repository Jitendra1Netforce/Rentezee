package com.rentezee.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.adapters.ProductsAdapter;
import com.rentezee.adapters.ViewPagerAdapter;
import com.rentezee.fragments.DashboardSliderImage;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.Util;
import com.rentezee.helpers.VolleyErrorHandler;
import com.rentezee.helpers.VolleyGsonRequest;
import com.rentezee.pojos.GenericResponse;
import com.rentezee.pojos.LoginResponse;
import com.rentezee.pojos.User;
import com.rentezee.pojos.mdashboard.Slider;
import com.rentezee.pojos.mdetail.ProductDetail;
import com.rentezee.pojos.mdetail.Response;

import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView tvProductName, tvProductCategoryName, tvDescription,tvProductID;
    private TextView tvSecurityMoney, tvPerDayRent;
    private CardView cardViewDescription;
    private LinearLayout layoutPrice, layoutBottom;
    ActionBar actionBar;
    LinearLayout layoutAddToWishlist,layoutAddToCart;
    String device_id;
    MaterialFavoriteButton materialFavoriteButton;
    public static final String PREFERENCES = "WishListPrefs";
    SharedPreferences settings;
    String id,user_id;
    long  userId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        context=this;

        actionBar=getSupportActionBar();

        settings = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

         System.out.println("android_id=================="+ device_id);

        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");

        }
        //Screen height and width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        layoutAddToWishlist = (LinearLayout) findViewById(R.id.layoutAddToWishlist);

        layoutAddToCart = (LinearLayout) findViewById(R.id.layoutAddToCart);

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.getLayoutParams().height = (int) (displayMetrics.widthPixels * .719);
        indicator = (CircleIndicator) findViewById(R.id.indicator);

        tvProductID = (TextView) findViewById(R.id.tvProductID);

        tvProductName=(TextView)findViewById(R.id.tvProductName);
        tvProductCategoryName=(TextView)findViewById(R.id.tvProductCategoryName);
        tvDescription=(TextView)findViewById(R.id.tvDescription);
        tvSecurityMoney=(TextView)findViewById(R.id.tvSecurityMoney);
        tvPerDayRent=(TextView)findViewById(R.id.tvPerDayRent);

        materialFavoriteButton = (MaterialFavoriteButton) findViewById(R.id.payment_salon_material_button);

        cardViewDescription=(CardView) findViewById(R.id.cardViewDescription);
        layoutPrice=(LinearLayout)findViewById(R.id.layoutPrice);
        layoutBottom=(LinearLayout)findViewById(R.id.layoutBottom);

        String cat_id = getIntent().getStringExtra(Constants.PRODUCT_ID);

         User user = (User) new AppPreferenceManager(context).getObject(PreferenceKeys.savedUser, User.class);

        if(user != null){

            userId = user.getUserId();

        }

         user_id = Long.toString(userId);



        layoutAddToWishlist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences.Editor editor = settings.edit();

                System.out.println("material favorite status========"+ materialFavoriteButton.isFavorite());

                if(materialFavoriteButton.isFavorite() )
                {
                    System.out.println("favorite============" + materialFavoriteButton.isFavorite());
                    materialFavoriteButton.setAnimateFavorite(true);
                    materialFavoriteButton.setBounceDuration(300);
                    materialFavoriteButton.setRotationAngle(360);
                    materialFavoriteButton.setRotationDuration(100);
                    materialFavoriteButton.setNotFavoriteResource(R.mipmap.ic_add_to_wishlist);
                    materialFavoriteButton.setFavorite(false);
                    editor.putBoolean(id, false);
                    editor.commit();

                }
                else
                {
                    System.out.println(" product_id============" +id);
                    add_wish_list(id, user_id);


                    materialFavoriteButton.setAnimateUnfavorite(true);
                    materialFavoriteButton.setBounceDuration(300);
                    materialFavoriteButton.setRotationAngle(360);
                    materialFavoriteButton.setRotationDuration(100);
                    materialFavoriteButton.setFavoriteResource(R.mipmap.ic_favorite_hdpi);
                    materialFavoriteButton.setFavorite(true);
                    editor.putBoolean(id, true);
                    editor.commit();

                }





            }
        });



        layoutAddToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });



        // fetchDetail(Integer.parseInt(cat_id));

        load_refresh(Integer.parseInt(cat_id));
    }

    private void fetchDetail(int productId)
    {


        try
        {
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


    private void add_wish_list(String productId,String user_id)
    {

       JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("product_id",productId);
            jsonObject.put("user_id", user_id);
            //registerViaId: 1 -> Normal, 2 -> Facebook, 3 -> Google

            showProgressBar(context);

            VolleyGsonRequest<GenericResponse> gsonRequest = new VolleyGsonRequest<>("http://netforce.biz/renteeze/webservice/Users/add_wishlist",
                    jsonObject,
                    new com.android.volley.Response.Listener<GenericResponse>() {
                        @Override
                        public void onResponse(GenericResponse response) {
                            dismissProgressBar();
                            Debugger.i(TAG, "Response " + response);
                            if (response != null) {
                                if (response.isSuccess())
                                {

                                    System.out.println("some data =========" + response.toString());
                                }
                                else
                                {
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
                    GenericResponse.class,
                    null
            );
            AppController.getInstance().addToRequestQueue(gsonRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




    private void load_refresh(int productId)
    {

        showProgressBar(context);

        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/products/product_details?action=details&id="+productId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null)
                        {
                            JsonObject data = result.getAsJsonObject("data");

                            JsonArray productImage = data.getAsJsonArray("ProductImage");

                            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                            for (int i = 0; i < productImage.size(); i++)
                            {
                                JsonObject jsonObject = (JsonObject) productImage.get(i);

                                String id = jsonObject.get("product_id").getAsString();

                                String image = jsonObject.get("image").getAsString();

                                Fragment fragment = new DashboardSliderImage();
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.URL, "http://netforce.biz/renteeze/webservice/files/products/"+image);
                                fragment.setArguments(bundle);
                                adapter.addFragment(fragment, "");
                            }

                            viewPager.setAdapter(adapter);
                            indicator.setViewPager(viewPager);

                            JsonObject category = data.getAsJsonObject("Category");

                            String category_name = category.get("name").getAsString();
                            JsonObject product = data.getAsJsonObject("Product");
                            id = product.get("id").getAsString();
                            String name = product.get("name").getAsString();
                            String price = product.get("price").getAsString();
                            String security_price = product.get("security_price").getAsString();
                            String description = product.get("description").getAsString();

                            tvProductID.setText(id);
                            tvProductName.setText(name);
                            tvProductCategoryName.setText(category_name);
                            tvDescription.setText(description);
                            cardViewDescription.setVisibility(View.VISIBLE);

                            String rs = getString(R.string.rs);
                            tvSecurityMoney.setText(String.format(Locale.ENGLISH, "%s%s", rs, security_price));
                            tvPerDayRent.setText(String.format(Locale.ENGLISH, "%s%s", rs, price));
                            layoutPrice.setVisibility(View.VISIBLE);

                            layoutBottom.setVisibility(View.VISIBLE);

                            Boolean yourLocked = settings.getBoolean(id, false);

                            System.out.println("yourloocked=============="+ yourLocked);

                            if(yourLocked)
                            {

                                System.out.println("data============" + settings.getBoolean(id, true));

                                materialFavoriteButton.setFavoriteResource(R.mipmap.ic_favorite_hdpi);
                                materialFavoriteButton.setAnimateFavorite(true);
                                materialFavoriteButton.setBounceDuration(300);
                                materialFavoriteButton.setRotationAngle(360);
                                materialFavoriteButton.setRotationDuration(100);
                                materialFavoriteButton.setFavorite(true);


                            }
                            else
                            {
                                System.out.println("not favo============" + settings.getBoolean(id, true));
                                materialFavoriteButton.setAnimateUnfavorite(true);
                                materialFavoriteButton.setBounceDuration(300);
                                materialFavoriteButton.setRotationAngle(360);
                                materialFavoriteButton.setRotationDuration(100);
                                materialFavoriteButton.setFavoriteResource(R.mipmap.ic_add_to_wishlist);
                                materialFavoriteButton.setFavorite(false);


                            }


                            dismissProgressBar();

                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                }) ;

    }

}
