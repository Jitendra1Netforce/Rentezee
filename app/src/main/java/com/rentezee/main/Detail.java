package com.rentezee.main;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView tvUserName,tvUserEmail,tvUserMobile,tvProductName,tvProductCategoryName,tvDescription,tvProductID;
    private TextView tvSecurityMoney, tvPerDayRent,tvSecurityMoneyHeading,tvPerDayRentHeading,tvPriceHeading,tvPrice;
    private CardView cardViewDescription;
    private LinearLayout layoutPrice, layoutBottom,layoutSecurityPrice,layoutRentPrice;
    ActionBar actionBar;
    LinearLayout layoutAddToWishlist,layoutAddToCart;
    String device_id;
    MaterialFavoriteButton materialFavoriteButton;
    public static final String PREFERENCES = "WishListPrefs";
    SharedPreferences settings;
    String id,user_id, user_mobile;
    long  userId;
    User user;
    DashboardContainer dashboardContainer;
    ImageView imageView1;


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

        imageView1 = (ImageView) findViewById(R.id.imageView1);

        tvUserName = (TextView) findViewById(R.id.txtUsername);

        tvPriceHeading = (TextView) findViewById(R.id.tvPriceHeading);

        tvPrice = (TextView) findViewById(R.id.tvPrice);

        tvUserEmail = (TextView) findViewById(R.id.txtUserEmail);

        tvUserMobile = (TextView) findViewById(R.id.txtMobileNo);

        tvProductID = (TextView) findViewById(R.id.tvProductID);

        tvProductName=(TextView)findViewById(R.id.tvProductName);

        tvProductCategoryName=(TextView)findViewById(R.id.tvProductCategoryName);

        tvDescription=(TextView)findViewById(R.id.tvDescription);

        tvSecurityMoney=(TextView)findViewById(R.id.tvSecurityMoney);

        tvPerDayRent=(TextView)findViewById(R.id.tvPerDayRent);

        tvSecurityMoneyHeading = (TextView) findViewById(R.id.tvSecurityMoneyHeading);

        tvPerDayRentHeading = (TextView) findViewById(R.id.tvPerDayRentHeading);

        materialFavoriteButton = (MaterialFavoriteButton) findViewById(R.id.payment_salon_material_button);

        cardViewDescription=(CardView) findViewById(R.id.cardViewDescription);

        layoutPrice=(LinearLayout)findViewById(R.id.layoutPrice);

        layoutSecurityPrice = (LinearLayout) findViewById(R.id.layoutSecurityPrice);

        layoutRentPrice= (LinearLayout) findViewById(R.id.layoutRentPrice);


        layoutBottom=(LinearLayout)findViewById(R.id.layoutBottom);

        String cat_id = getIntent().getStringExtra(Constants.PRODUCT_ID);

          user = (User) new AppPreferenceManager(context).getObject(PreferenceKeys.savedUser, User.class);

        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();

        if(user != null){

            userId = user.getUserId();
        }

         user_id = Long.toString(userId);


        System.out.println("user_id---------------"+ user_id);

        layoutAddToWishlist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences.Editor editor = settings.edit();

                System.out.println("material favorite status========" + materialFavoriteButton.isFavorite());

                if(user != null){

                    if(materialFavoriteButton.isFavorite() )
                    {
                        add_wish_list(id, user_id);
                        System.out.println("favorite============" + materialFavoriteButton.isFavorite());
                        materialFavoriteButton.setAnimateFavorite(true);
                        materialFavoriteButton.setBounceDuration(300);
                        materialFavoriteButton.setRotationAngle(360);
                        materialFavoriteButton.setRotationDuration(100);
                        materialFavoriteButton.setNotFavoriteResource(R.mipmap.ic_add_to_wishlist);
                        materialFavoriteButton.setFavorite(false);
                  /*  editor.putBoolean(id, false);
                    editor.commit();*/

                    }
                    else
                    {
                        add_wish_list(id, user_id);
                        System.out.println(" product_id============" + id);
                        materialFavoriteButton.setAnimateUnfavorite(true);
                        materialFavoriteButton.setBounceDuration(300);
                        materialFavoriteButton.setRotationAngle(360);
                        materialFavoriteButton.setRotationDuration(100);
                        materialFavoriteButton.setFavoriteResource(R.mipmap.ic_favorite_hdpi);
                        materialFavoriteButton.setFavorite(true);
                  /*  editor.putBoolean(id, true);
                    editor.commit();*/

                    }
                }
                else
                {

                    Intent intent = new Intent(Detail.this,Login.class);
                    startActivity(intent);

                }



            }
        });



        layoutAddToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                add_to_cart(device_id, id);
                dashboardContainer.count_cart();


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

    private void processProductDetailResponse(Response response)
    {

        ProductDetail productDetail=response.getData();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ArrayList<String> images=productDetail.getImages();
        for (String imageUrl : images)
        {
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

    private void add_to_cart(String device_id ,String product_id)
    {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("device_id",device_id);
            jsonObject.put("product_id", product_id);
            //registerViaId: 1 -> Normal, 2 -> Facebook, 3 -> Google

            showProgressBar(context);

            VolleyGsonRequest<GenericResponse> gsonRequest = new VolleyGsonRequest<>("http://netforce.biz/renteeze/webservice/Products/addtocart",
                    jsonObject,
                    new com.android.volley.Response.Listener<GenericResponse>() {
                        @Override
                        public void onResponse(GenericResponse response) {
                            dismissProgressBar();
                            Debugger.i(TAG, "Response " + response);
                            if (response != null)
                            {
                                if (response.isSuccess())
                                {
                                    System.out.println("some data =========" + response.toString());

                                    Toast.makeText(getApplicationContext(),response.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                    dashboardContainer = new DashboardContainer();
                                    dashboardContainer.count_cart();

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

        System.out.println("user_id"+user_id+"Prodcuct"+String.valueOf(productId));
        JsonObject json = new JsonObject();

       if(user != null){

            json.addProperty("action", "details");
            json.addProperty("id", String.valueOf(productId));
            json.addProperty("device_id", device_id);
            json.addProperty("user_id", user_id);

        }
        else
        {

            System.out.println("Not Login =================");
            json.addProperty("action", "details");
            json.addProperty("id", String.valueOf(productId));
            json.addProperty("device_id", device_id);
            json.addProperty("user_id", "");

        }


        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/products/product_details")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null)
                        {

                            System.out.println("result==============" + result);

                            JsonObject data = result.getAsJsonObject("data");

                             String wishlist_status = result.get("wishlist").getAsString();

                            JsonArray productImage = data.getAsJsonArray("ProductImage");

                            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                            for (int i = 0; i < productImage.size(); i++)
                            {
                                JsonObject jsonObject = (JsonObject) productImage.get(i);

                                String id = jsonObject.get("product_id").getAsString();

                                String image = jsonObject.get("image").getAsString();

                                Fragment fragment = new DashboardSliderImage();
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.URL, "http://netforce.biz/renteeze/webservice/files/products/" + image);
                                fragment.setArguments(bundle);
                                adapter.addFragment(fragment, "");
                            }

                            viewPager.setAdapter(adapter);
                            indicator.setViewPager(viewPager);

                            JsonObject category = data.getAsJsonObject("Category");

                            String category_name = category.get("name").getAsString();

                            JsonObject user_details = data.getAsJsonObject("User");

                            String user_name = user_details.get("name").getAsString();

                            String user_email = user_details.get("email").getAsString();



                            try {

                                user_mobile = user_details.get("mobile").getAsString();


                                System.out.println("user_mobile============="+user_mobile.toString());

                               /* if(user_mobile.toString().equals("null")){

                                    imageView1.setVisibility(View.INVISIBLE);
                                }
                                else
                                {
                                    imageView1.setVisibility(View.VISIBLE);

                                }

                                System.out.println("user_mobile============"+user_mobile);*/

                            }
                            catch (Exception exe)
                            {


                            }

                            tvUserName.setText(user_name);
                            tvUserEmail.setText(user_email);
                            tvUserMobile.setText(user_mobile);

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

                            if(security_price.equals("0"))
                            {

                                System.out.println("hi data=============");

                                tvPriceHeading.setVisibility(View.VISIBLE);
                                tvPrice.setVisibility(View.VISIBLE);

                                tvPrice.setText(String.format(Locale.ENGLISH, "%s%s", rs, price));

                                layoutPrice.setVisibility(View.GONE);

                            }
                            else
                            {
                                tvPriceHeading.setVisibility(View.GONE);
                                tvPrice.setVisibility(View.GONE);

                                tvSecurityMoney.setText(String.format(Locale.ENGLISH, "%s%s", rs, security_price));
                                tvPerDayRent.setText(String.format(Locale.ENGLISH, "%s%s", rs, price));
                                layoutPrice.setVisibility(View.VISIBLE);
                                layoutRentPrice.setVisibility(View.VISIBLE);


                            }



                            layoutBottom.setVisibility(View.VISIBLE);

                          //  Boolean yourLocked = settings.getBoolean(id, false);

                            System.out.println("wishlist_status==============" + wishlist_status);

                            if (wishlist_status.equals("1")) {

                                System.out.println("data============" + settings.getBoolean(id, true));

                                materialFavoriteButton.setFavoriteResource(R.mipmap.ic_favorite_hdpi);
                                materialFavoriteButton.setAnimateFavorite(true);
                                materialFavoriteButton.setBounceDuration(300);
                                materialFavoriteButton.setRotationAngle(360);
                                materialFavoriteButton.setRotationDuration(100);
                                materialFavoriteButton.setFavorite(true);


                            }
                            else {
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
