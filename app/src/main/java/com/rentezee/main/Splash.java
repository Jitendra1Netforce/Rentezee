package com.rentezee.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;

import com.rentezee.adapters.ViewPagerAdapter;
import com.rentezee.fragments.SplashSliderIntro;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.pojos.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import me.relex.circleindicator.CircleIndicator;

public class Splash extends BaseActivity implements View.OnClickListener {

    private final static String TAG = Splash.class.getSimpleName();
    private Context context;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        printHashKey(context);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        viewPager.getLayoutParams().height = (int) (displayMetrics.heightPixels * .7);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        setupViewPager(viewPager);
        indicator.setViewPager(viewPager);

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        findViewById(R.id.tvLogin).setOnClickListener(this);
        findViewById(R.id.tvSignUp).setOnClickListener(this);
        findViewById(R.id.tvSkipLogin).setOnClickListener(this);

        init();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle;
        Fragment fragment;

        fragment = new SplashSliderIntro();
        bundle = new Bundle();
        bundle.putInt("imageResId", R.mipmap.intro1);
        bundle.putString("introText", getString(R.string.intro1));
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "ONE");

        fragment = new SplashSliderIntro();
        bundle = new Bundle();
        bundle.putInt("imageResId", R.mipmap.intro2);
        bundle.putString("introText", getString(R.string.intro2));
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "TWO");

        fragment = new SplashSliderIntro();
        bundle = new Bundle();
        bundle.putInt("imageResId", R.mipmap.intro3);
        bundle.putString("introText", getString(R.string.intro3));
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "THREE");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSkipLogin:
                gotoActivityByClearingBackStack(DashboardContainer.class);
                break;
            case R.id.tvLogin:
                gotoActivity(Login.class);
                break;
            case R.id.tvSignUp:
                gotoActivity(SignUp.class);
                break;
        }
    }



    private void init() {

        final User user = (User) new AppPreferenceManager(context).getObject(PreferenceKeys.savedUser, User.class);
        int waitTime = 1000; // 1 sec
        if (user == null && getIntent().getBooleanExtra(Constants.showSplash, false)) {
            waitTime = 0;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    gotoActivityByClearingBackStack(DashboardContainer.class);
                    finish();
                } else {
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }

            }
        }, waitTime);
    }

    @SuppressLint("PackageManagerGetSignatures")
    public void printHashKey(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo("com.rentezee.main", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Debugger.i("FBHash", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Debugger.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Debugger.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Debugger.e("exception", e.toString());
        }
    }

}
