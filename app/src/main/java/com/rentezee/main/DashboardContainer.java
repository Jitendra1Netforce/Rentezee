package com.rentezee.main;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.adapters.DashboardCategoriesAdapter;
import com.rentezee.adapters.TrendingAdapter;
import com.rentezee.adapters.ViewPagerAdapter;
import com.rentezee.fragments.DashboardSliderImage;
import com.rentezee.fragments.chat.ChatActivity;
import com.rentezee.fragments.myorder.MyOrder;
import com.rentezee.fragments.myorder.activeorder.ActiveOrders;
import com.rentezee.fragments.my_cart.MyCart;
import com.rentezee.fragments.notification.NotificationActivity;
import com.rentezee.fragments.profile.ProfileSetting;
import com.rentezee.fragments.rent_it_out.RentitOutActivity;
import com.rentezee.fragments.rentenzee_credit.CreditActivity;
import com.rentezee.fragments.wishlist.WishList;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.LocationUtil;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.Util;
import com.rentezee.pojos.User;
import com.rentezee.pojos.mdashboard.CategoryData;
import com.rentezee.pojos.mdashboard.Slider;
import com.rentezee.pojos.mdashboard.Trending;
import com.rentezee.services.GPSTracker;
import com.rentezee.views.ExpandableHeightGridView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class DashboardContainer extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        ResultCallback<LocationSettingsResult>,
        DialogInterface.OnCancelListener {

    public static  int cart_count;
    public ArrayList<CategoriesData> categoryDatas = new ArrayList<>();
    ArrayList<TrendingData> trendingDatas = new ArrayList<>();
    public  static final ArrayList<String> category_data = new ArrayList<>();
    public  static final ArrayList<String> category_id = new ArrayList<>();
    public  static final HashMap<Integer, String> param_aux = new HashMap<Integer, String>();
    private final static String TAG = DashboardContainer.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST = 1001;
    private Context context;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbarNoInternet;
    private FragmentManager fragmentManager;
    private ActionBar actionBar;
    private NavigationView navigationView;
    //Permission need to be allowed to access data accordingly by user
    private ArrayList<String> permissionsList;
    private GoogleApiClient googleApiClient;
    private LocationSettingsRequest locationSettingsRequest;
    //private double latitude, longitude;
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private ExpandableHeightGridView gvCategories;
    private ExpandableHeightGridView gvTrending;
    private ImageView ivMore;
    private TextView tvTrending;
    public ArrayList<CategoryData> fetchedCategoryDataList, categoryDataList;
    private DashboardCategoriesAdapter dashboardCategoriesAdapterAdapter;
    private ArrayList<Slider> fetchedSliderDataList;
    private ArrayList<Trending> fetchedTrendingList;
    private DisplayMetrics displayMetrics;
    User user;
    TextView tvNavName,tvNavEmail,tvNavMobile;
    LinearLayout layoutNavLogin,layoutNavEmailMobile,layoutLogout;
    CircleImageView circleImageView;
    public static  int my_cart =0;
    public static  String  device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_container);
        FacebookSdk.sdkInitialize(getApplicationContext());


        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        fragmentManager = getSupportFragmentManager();
        actionBar = getSupportActionBar();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        /*googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/
        //on location
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //set custom drawer icon
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.mipmap.ic_menu);

        //setting listener to custom drawer icon
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


                if (drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        //Screen height and width
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
         layoutLogout = (LinearLayout) navigationView.findViewById(R.id.layout_logout);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.getLayoutParams().height = (int) (displayMetrics.widthPixels * .5834);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        gvCategories = (ExpandableHeightGridView) findViewById(R.id.gvCategories);
        ivMore = (ImageView) findViewById(R.id.ivMore);
        ivMore.setOnClickListener(this);
        tvTrending = (TextView) findViewById(R.id.tvTrending);
        gvTrending = (ExpandableHeightGridView) findViewById(R.id.gvTrending);

        //Navigation header
        View headerLayout = navigationView.getHeaderView(0);
         circleImageView = (CircleImageView) headerLayout.findViewById(R.id.imageView);
         tvNavName = (TextView) headerLayout.findViewById(R.id.tvNavName);
         tvNavEmail = (TextView) headerLayout.findViewById(R.id.tvNavEmail);
         tvNavMobile = (TextView) headerLayout.findViewById(R.id.tvNavMobile);
         layoutNavLogin = (LinearLayout) headerLayout.findViewById(R.id.layoutNavLogin);
         layoutNavEmailMobile = (LinearLayout) headerLayout.findViewById(R.id.layoutNavEmailMobile);

        User user = (User) new AppPreferenceManager(context).getObject(PreferenceKeys.savedUser, User.class);

        if (user != null)
        {
            layoutNavEmailMobile.setVisibility(View.VISIBLE);
            layoutNavLogin.setVisibility(View.GONE);
            layoutLogout.setOnClickListener(this);
            tvNavName.setText("Welcome " + user.getName());
            tvNavEmail.setText(user.getEmail());
            tvNavMobile.setText(user.getMobile());

                    Glide.with(context)
                    .load(user.getImageUrl())
                    .centerCrop()
                            //.placeholder(R.mipmap.ic_loading)
                    .crossFade()
                    .into(circleImageView);

            headerLayout.findViewById(R.id.ivArrow).setOnClickListener(this);
        } else {
            layoutLogout.setVisibility(View.GONE);
            layoutNavEmailMobile.setVisibility(View.GONE);
            layoutNavLogin.setVisibility(View.VISIBLE);
            layoutNavLogin.setOnClickListener(this);
            tvNavName.setText("Welcome Guest!");
        }

        System.out.println("my_cart==============" + my_cart);


        setMenuCounter(R.id.nav_notifications, 0);
        setMenuCredits(R.id.nav_rentezee_credits, 0);

        //check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //check permission at runtime
            checkPermissions();
        } else {
            init();
        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        /**
         * order for condition('s) check must be same as we store permissions in an array
         */
        permissionsList = new ArrayList<>();
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        //if permission new to be added, list must have size greater than 0
        if (permissionsList != null && permissionsList.size() > 0) {
            requestPermission();
        } else {
            init();
        }
    }


    private void requestPermission() {

        //convert permission list to string[]
        final String[] permissions = new String[permissionsList.size()];
        for (int i = 0; i < permissionsList.size(); i++) {
            permissions[i] = permissionsList.get(i);
        }

        /**
         * Should we show an explanation? Requests the permission.
         * If the permission has been denied previously, a SnackBar will prompt the user to grant the
         * permission, otherwise it is requested directly.
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardContainer.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            Snackbar.make(coordinatorLayout, R.string.permission_message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.okay,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(DashboardContainer.this,
                                            permissions,
                                            MY_PERMISSIONS_REQUEST);
                                }
                            }
                    ).show();
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(DashboardContainer.this,
                    permissions,
                    MY_PERMISSIONS_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            /*boolean allPermissionsGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }*/
            checkPermissions();
        }
    }


    private void setMenuCounter(@IdRes int itemId, int count) {
        View view = navigationView.getMenu().findItem(itemId).getActionView();
        if (count > 0) {
            TextView tvCount = (TextView) view.findViewById(R.id.tvCount);
            tvCount.setText(String.valueOf(count));
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void setMenuCredits(@IdRes int itemId, int credits) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        if (credits > 0) {
            view.setText(String.format("%s%s", getString(R.string.rs), String.valueOf(credits)));
        } else {
            view.setVisibility(View.GONE);
        }
    }


    private void showActionBar() {
        if (actionBar != null && !actionBar.isShowing()) {
            actionBar.show();
        }
    }

    private void hideActionBar() {
        if (actionBar != null && actionBar.isShowing()) {
            actionBar.hide();
        }
    }

    //starting function
    private void init() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)
                .build();

        LocationUtil.requestLocation(googleApiClient, locationSettingsRequest, this);
    }

    /*private void gotoDashBoard(double latitude, double longitude){
        final Fragment fragment=new Dashboard();
        Bundle bundle=new Bundle();
        bundle.putDouble(Constants.LAT, latitude);
        bundle.putDouble(Constants.LNG, longitude);
        fragment.setArguments(bundle);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame, fragment, Constants.DASHBOARD_FRAGMENT)
                        .commit();
            }
        });
    }*/


    BroadcastReceiver noInternetLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showOrHideNoInternetSnackBar(intent.getBooleanExtra(Constants.noInternet, false));
        }
    };

    private void showOrHideNoInternetSnackBar(boolean isConnectedToInternet) {
        if (isConnectedToInternet) {
            //internet available
            if (snackbarNoInternet != null) {
                snackbarNoInternet.dismiss();
            }
        } else {
            //internet not available
            if (snackbarNoInternet == null) {
                snackbarNoInternet = Snackbar.make(coordinatorLayout,
                        getString(R.string.no_internet_error),
                        Snackbar.LENGTH_INDEFINITE);
            }
            snackbarNoInternet.show();
        }
    }


    private String getCurrentFragmentTag() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            return fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        } else {
            return null;
        }
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)

    {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                Intent dashboard=new Intent(context, DashboardContainer.class);
                gotoActivity(dashboard);
                finish();

                break;
            case R.id.nav_orders:
                Intent intent=new Intent(context, MyOrder.class);
                gotoActivity(intent);
                break;
            case R.id.nav_cart:
                Intent cart=new Intent(context, MyCart.class);
                gotoActivity(cart);
                break;
            case R.id.nav_profile:
                Intent profile=new Intent(context, ProfileSetting.class);
                gotoActivity(profile);
                break;
            case R.id.nav_wishlist:
                Intent intent2=new Intent(context, WishList.class);
                gotoActivity(intent2);
                break;
            case R.id.nav_notifications:
                Intent notification=new Intent(context, NotificationActivity.class);
                gotoActivity(notification);
                break;
            case R.id.nav_ret_it_out:
                Intent rent_it_out=new Intent(context, RentitOutActivity.class);
                gotoActivity(rent_it_out);
                break;
            case R.id.nav_rentezee_credits:
                Intent credit=new Intent(context, CreditActivity.class);
                gotoActivity(credit);
                break;
            case R.id.nav_share:
                shareData();
                break;
            case R.id.nav_callus:
                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:123456789"));
                startActivity(callIntent);*/
                break;

            case R.id.nav_chat:
                Intent chat=new Intent(context, ChatActivity.class);
                gotoActivity(chat);
                break;

        }



        /*if (id == R.id.nav_cart) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivArrow:
                gotoActivity(EditProfileAddress.class);
                break;
            case R.id.layout_logout:
                Util.showAlertDialog(context, getString(R.string.app_name), getString(R.string.logout_msg), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

                break;
            case R.id.layoutNavLogin:
                gotoActivity(Login.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeDrawer();
                    }
                }, 300);

                break;
        }
    }

    private void logout() {
        //google logout
        try {
            if (googleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                Debugger.i(TAG, "Google signout successfully");
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //facebook
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //normal
        new AppPreferenceManager(context).clear();
        Intent intent = new Intent(context, Splash.class);
        intent.putExtra(Constants.showSplash, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (!isFinishing()) {
            finish();
        }
    }


    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Debugger.i(TAG, "googleApiClient onConnected");
        /*LocationRequest locationRequest = LocationRequest.create()
                .setFastestInterval(60 * 60 * 1000)// 10 seconds, in milliseconds
                .setInterval(60 * 60 * 1000); // 1 second, in milliseconds
        //.setSmallestDisplacement(10);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Debugger.i(TAG, "googleApiClient onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        Debugger.i(TAG, "(" + location.getLatitude() + " " + location.getLongitude() + ")");
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        Debugger.i(TAG, "googleApiClient onCancel");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        googleApiAvailability.getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Debugger.i(TAG, "onResult SUCCESS");
                fetchLocation();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    Debugger.i(TAG, "onResult  RESOLUTION_REQUIRED");
                    //start resolution and handle it result in onActivityResult
                    status.startResolutionForResult(this, LocationUtil.REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Toast.makeText(this, "Unable to complete the request", Toast.LENGTH_SHORT).show();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Toast.makeText(this, "Location settings are inadequate, and can not be set here. Dialogue is not created", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LocationUtil.REQUEST_CHECK_SETTINGS) {
            fetchLocation();
        }
    }

    private void fetchLocation() {
        GPSTracker gpsTracker = new GPSTracker(context);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        //init(latitude, longitude);
        //fetchDashboardData(latitude, longitude);
         load_refresh();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null)
        {
            googleApiClient.connect();
        }
        LocalBroadcastManager.getInstance(context)
                .registerReceiver(noInternetLocalBroadcastReceiver,
                        new IntentFilter(Constants.NO_INTERNET_BROADCAST_RECEIVER)
                );
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationUtil.stopLocationUpdates(this.googleApiClient, this);
        }
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        LocalBroadcastManager.getInstance(context).unregisterReceiver(noInternetLocalBroadcastReceiver);
        super.onStop();
    }



    private void load_refresh()
    {
        String  device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        // recyclerView.setVisibility(View.GONE);
        // homeDatas.clear();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device_id", device_id);

        showProgressBar(context);
        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/Pages/dashboard.json")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        //System.out.println("data================" + result.toString());

                        if (result != null) {
                            JsonObject v = result.getAsJsonObject("data");

                            String my_cart_c = v.get("my_cart").getAsString();

                            my_cart = Integer.parseInt(my_cart_c);

                            System.out.println("my cart==============" + my_cart_c);

                            JsonArray sliderArray = v.getAsJsonArray("sliderData");

                            JsonArray categoruArray = v.getAsJsonArray("categories");

                            JsonArray trendingsArray = v.getAsJsonArray("trendings");

                            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                            for (int i = 0; i < sliderArray.size(); i++) {
                                JsonObject jsonObject = (JsonObject) sliderArray.get(i);
                                JsonObject vo = jsonObject.getAsJsonObject("Slider");
                                String id = vo.get("id").getAsString();
                                String name = vo.get("title").getAsString();
                                String image = vo.get("image").getAsString();
                                System.out.println("imageurl ======================" + name);

                                Fragment fragment = new DashboardSliderImage();
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.URL, "http://netforce.biz/renteeze/webservice/images/slides/" + image);
                                fragment.setArguments(bundle);
                                adapter.addFragment(fragment, name);
                            }
                            viewPager.setAdapter(adapter);
                            indicator.setViewPager(viewPager);

                            category_data.add("Choose Category");

                            for (int i = 0; i < categoruArray.size(); i++) {
                                JsonObject jsonObject = (JsonObject) categoruArray.get(i);
                                JsonObject category = jsonObject.getAsJsonObject("Category");
                                String id = category.get("id").getAsString();

                                String name = category.get("name").getAsString();
                                String image = "http://netforce.biz/renteeze/webservice/images/" + category.get("image").getAsString();

                                System.out.println("imageurl ======================" + name);
                                categoryDatas.add(new CategoriesData(id, name, image));
                                category_data.add(name);
                                category_id.add(id);
                                param_aux.put(Integer.parseInt(id), name.toString());
                            }
                            dashboardCategoriesAdapterAdapter = new DashboardCategoriesAdapter(context, categoryDatas);

                            gvCategories.setFocusable(false);
                            gvCategories.setAdapter(dashboardCategoriesAdapterAdapter);
                            gvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent intent = new Intent(context, Category.class);
                                    intent.putParcelableArrayListExtra(Constants.CATEGORIES, categoryDatas);
                                    intent.putExtra(Constants.CATEGORY_ID, categoryDatas.get(position).category_id);
                                    intent.putExtra(Constants.SELECTED_TAB_POSITION, position);
                                    gotoActivity(intent);

                                }
                            });

                            for (int i = 0; i < trendingsArray.size(); i++) {
                                JsonObject jsonObject = (JsonObject) trendingsArray.get(i);
                                JsonObject trenging = jsonObject.getAsJsonObject("Product");

                                JsonObject category = jsonObject.getAsJsonObject("Category");

                                String category_name = category.get("name").getAsString();

                                String id = trenging.get("id").getAsString();
                                String name = trenging.get("name").getAsString();
                                String price = trenging.get("price").getAsString();
                                String special_price = trenging.get("special_price").getAsString();
                                String image = "http://netforce.biz/renteeze/webservice/files/products/" + trenging.get("images").getAsString();

                                System.out.println("imageurl ======================" + name);
                                trendingDatas.add(new TrendingData(id, name, image, price, special_price, category_name));

                            }
                            setMenuCounter(R.id.nav_cart, 0);

                            cart_count = my_cart;
                            tvCartCount.setText(String.valueOf(cart_count));

                            if (trendingDatas != null) {
                                tvTrending.setVisibility(View.VISIBLE);
                                int width = displayMetrics.widthPixels - (int) getResources().getDimension(R.dimen.ten);
                                TrendingAdapter trendingAdapter = new TrendingAdapter(context, trendingDatas, width);
                                gvTrending.setFocusable(false);
                                gvTrending.setAdapter(trendingAdapter);
                            } else {
                                tvTrending.setVisibility(View.GONE);
                            }
                            dismissProgressBar();
                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });

    }

/*

    private void fetchDashboardData(double lat, double lng) {


        load_refresh();

        //Post data to sever
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appTypeId", Constants.APP_TYPE_ID);
            jsonObject.put("versionName", Util.getVersionName(context));
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
            String url = "http://netforce.biz/renteeze/webservice/Pages/dashboard.json"; //URL to hit


            showProgressBar(context);
            VolleyGsonRequest<com.rentezee.pojos.mdashboard.Response> gsonRequest = new VolleyGsonRequest<>(url,
                    jsonObject,
                    new Response.Listener<com.rentezee.pojos.mdashboard.Response>()
                    {
                        @Override
                        public void onResponse(com.rentezee.pojos.mdashboard.Response response)
                        {
                            dismissProgressBar();
                            Debugger.i(TAG, "Response " + response);
                            if (response != null)
                            {
                                if (response.isSuccess())
                                {
                                    processDashboardResponse(response);
                                }
                                else
                                {
                                    showSnackBar(coordinatorLayout, response.getMessage());
                                }
                            }
                            else
                            {
                                showSnackBar(coordinatorLayout, getString(R.string.generic_error));
                            }
                        }
                    },

                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            dismissProgressBar();
                            error.printStackTrace();
                            showSnackBar(coordinatorLayout, VolleyErrorHandler.getMessage(context, error));
                        }
                    },
                    com.rentezee.pojos.mdashboard.Response.class,
                    null
            );
            AppController.getInstance().addToRequestQueue(gsonRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    private void processDashboardResponse(com.rentezee.pojos.mdashboard.Response response)
    {
        fetchedSliderDataList = response.getData().getSliderData();
        if (fetchedSliderDataList != null)
        {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            for (Slider sliderData : fetchedSliderDataList)
            {
                Fragment fragment = new DashboardSliderImage();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.URL, sliderData.getImageUrl());
                fragment.setArguments(bundle);
                adapter.addFragment(fragment, sliderData.getCategoryName());
            }
            viewPager.setAdapter(adapter);
            indicator.setViewPager(viewPager);
        }

        //Categories
        fetchedCategoryDataList = response.getData().getCategories();

        if (fetchedCategoryDataList.size() > 6)
        {
            categoryDataList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                categoryDataList.add(fetchedCategoryDataList.get(i));
            }
           // dashboardCategoriesAdapterAdapter = new DashboardCategoriesAdapter(context, categoryDataList);
            ivMore.setVisibility(View.VISIBLE);
        } else {
            ivMore.setVisibility(View.GONE);
           // dashboardCategoriesAdapterAdapter = new DashboardCategoriesAdapter(context, fetchedCategoryDataList);
        }
        gvCategories.setFocusable(false);
        gvCategories.setAdapter(dashboardCategoriesAdapterAdapter);
        gvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, Category.class);
                intent.putParcelableArrayListExtra(Constants.CATEGORIES, fetchedCategoryDataList);
                intent.putExtra(Constants.CATEGORY_ID, fetchedCategoryDataList.get(position).getCategoryId());
                intent.putExtra(Constants.SELECTED_TAB_POSITION, position);
                gotoActivity(intent);
            }
        });

        //trending data
        fetchedTrendingList = response.getData().getTrendings();
        if (fetchedTrendingList != null)
        {
            tvTrending.setVisibility(View.VISIBLE);
            int width=displayMetrics.widthPixels-(int)getResources().getDimension(R.dimen.ten);
           // TrendingAdapter trendingAdapter = new TrendingAdapter(context, fetchedTrendingList, width);
          //  gvTrending.setFocusable(false);
          //  gvTrending.setAdapter(trendingAdapter);
        }
        else
        {
            tvTrending.setVisibility(View.GONE);
        }
    }


    public  void count_cart(){

        // recyclerView.setVisibility(View.GONE);
        // homeDatas.clear();

        System.out.println("device_id-------------" + device_id);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device_id", device_id);

        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/Pages/dashboard.json")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

//                        System.out.println("data================" + result.toString());

                        if (result != null)
                        {
                            JsonObject v = result.getAsJsonObject("data");

                            String my_cart_c = v.get("my_cart").getAsString();

                            int new_my_cart = Integer.parseInt(my_cart_c);

                            tvCartCount.setText(String.valueOf(new_my_cart));

                            System.out.println("reload------------------" + cart_count);

                            //setMenuCounter(R.id.nav_cart, new_my_cart);

                        }
                        else
                        {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });


    }


    @Override
    protected void onResume()
    {
        super.onResume();
        try {
            invalidateOptionsMenu();
        }catch (Exception e){

        }

        count_cart();

        User user = (User) new AppPreferenceManager(context).getObject(PreferenceKeys.savedUser, User.class);

        if (user != null)
        {
            layoutNavEmailMobile.setVisibility(View.VISIBLE);
            layoutNavLogin.setVisibility(View.GONE);
            layoutLogout.setOnClickListener(this);
            tvNavName.setText("Welcome " + user.getName());
            tvNavEmail.setText(user.getEmail());
            tvNavMobile.setText(user.getMobile());

            Glide.with(context)
                    .load(user.getImageUrl())
                    .centerCrop()//.placeholder(R.mipmap.ic_loading)
                    .crossFade()
                    .into(circleImageView);

        }
        else
        {
            layoutLogout.setVisibility(View.GONE);
            layoutNavEmailMobile.setVisibility(View.GONE);
            layoutNavLogin.setVisibility(View.VISIBLE);
            layoutNavLogin.setOnClickListener(this);
            tvNavName.setText("Welcome Guest!");
        }

    }


    private void shareData()
    {
        String shareBody = "This is Rentenzee Referal code";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "This is my referal code"));
    }
}
