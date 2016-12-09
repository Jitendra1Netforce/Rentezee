package com.rentezee.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.JToast;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.RegisterVia;
import com.rentezee.helpers.Util;
import com.rentezee.helpers.Validator;
import com.rentezee.helpers.VolleyErrorHandler;
import com.rentezee.helpers.VolleyGsonRequest;
import com.rentezee.pojos.LoginResponse;
import com.rentezee.pojos.SignUpResponse;
import com.rentezee.pojos.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class SignUp extends BaseActivity implements View.OnClickListener
{

    private static final String TAG = SignUp.class.getSimpleName();
    private Context context;
    private CoordinatorLayout coordinatorLayout;
    private EditText etName, etEmail, etPhone, etPassword;
    public static final String PREFERENCES = "WishListPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;
    String reg_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        reg_id =  FirebaseInstanceId.getInstance().getToken();

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);

        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.tvGoForSignIn).setOnClickListener(this);

        try {
            Bundle bundle = getIntent().getExtras();
            if(bundle.getBoolean(Constants.fillData, false)) {
                fillNameEmail(bundle.getString(Constants.name), bundle.getString(Constants.email));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void fillNameEmail(String name, String email){
        if(name!=null) {
            etName.setText(name);
        }
        if(email!=null) {
            etEmail.setText(email);
        }
        etPhone.requestFocus();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                normalSignUp();
                break;
            case R.id.tvGoForSignIn:
                gotoActivity(Login.class);
                break;
        }
    }

    private void normalSignUp() {

        final String name = etName.getText().toString();
        if (!Validator.isValidName(name)) {
            showSnackBar(coordinatorLayout, getString(R.string.error_name));
            return;
        }

        final String email = etEmail.getText().toString();
        if (email.isEmpty()) {
            showSnackBar(coordinatorLayout, getString(R.string.error_email_empty));
            return;
        }

        if (!Validator.isValidEmail(email)) {
            showSnackBar(coordinatorLayout, getString(R.string.error_email_not_valid));
            return;
        }

        final String mobile = etPhone.getText().toString();
        if (mobile.isEmpty()) {
            showSnackBar(coordinatorLayout, getString(R.string.error_mobile_empty));
            return;
        }

        if (!Validator.isValidMobile(mobile)) {
            showSnackBar(coordinatorLayout, getString(R.string.error_mobile_not_valid));
            return;
        }

        String password = etPassword.getText().toString();
        if (password.isEmpty()) {
            showSnackBar(coordinatorLayout, getString(R.string.error_pwd_empty));
            return;
        }

        if (!Validator.isValidPassword(password)) {
            showSnackBar(coordinatorLayout, getString(R.string.error_pwd_min_chars));
            return;
        }


     /*   JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("email", email);
        json.addProperty("mobile", mobile);
        json.addProperty("password", password);
        json.addProperty("registeredVia", "1");
        json.addProperty("appTypeId", "2");
        json.addProperty("versionName", "1.0");

        Ion.with(context)
                .load("http://netforce.biz/renteeze/webservice/users/signup")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("ar=============" + result);
                        // do stuff with the result or error
                    }
                });*/


     /* Ion.with(context)
                .load("POST","http://netforce.biz/renteeze/webservice/users/signup")
                .setBodyParameter("name", name)
                .setBodyParameter("email", email)
                .setBodyParameter("mobile", mobile)
                .setBodyParameter("password", password)
                .setBodyParameter("registeredVia", "1")
                .setBodyParameter("appTypeId", "2")
                .setBodyParameter("versionName", "1.0")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        System.out.println("ar=============" + result.toString());
                    }
                });

*/

       /*Ion.with(SignUp.this)
                .load("http://netforce.biz/renteeze/webservice/users/signup")


                .setMultipartParameter("name", name)
                .setMultipartParameter("email", email)
                .setMultipartParameter("mobile", mobile)
                .setMultipartParameter("password", password)
                .setMultipartParameter("registeredVia","1")
                .setMultipartParameter("appTypeId", "2")
                .setMultipartParameter("versionName", "1.0")

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        System.out.println("ar=============" + result.toString());
                    }
                }) ;
*/

       //Post data to sever
       JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("mobile", mobile);
            jsonObject.put("password", password);
            jsonObject.put("reg_id",reg_id);
            //registerViaId: 1 -> Normal, 2 -> Facebook, 3 -> Google
            jsonObject.put("registeredVia", 1);
            jsonObject.put("appTypeId", Constants.APP_TYPE_ID);
            jsonObject.put("versionName", Util.getVersionName(context));

            Debugger.i(TAG, "post data " + jsonObject.toString());
            String url = "http://netforce.biz/renteeze/webservice/users/signup"; //URL to hit
            showProgressBar(context);
            VolleyGsonRequest<SignUpResponse> gsonRequest = new VolleyGsonRequest<>(url, jsonObject,
                    new Response.Listener<SignUpResponse>()
                    {



                        @Override
                        public void onResponse(SignUpResponse response) {

                            System.out.println("arvind=============" + response);
                            dismissProgressBar();
                            Debugger.i(TAG, "Response " + response);
                            if (response != null) {
                                if (response.isSuccess())
                                {
                                    User user = new User(response.getUserId(),name, email, mobile,"");
                                   // user.setUserId(response.getUserId());

                                    System.out.println("response=============" + response.toString());

                                    new AppPreferenceManager(context).putObject(PreferenceKeys.savedUser, user);
                                    gotoActivityByClearingBackStack(DashboardContainer.class);

                                } else {
                                    showSnackBar(coordinatorLayout, response.getMessage());
                                }
                            } else {
                                showSnackBar(coordinatorLayout, getString(R.string.generic_error));
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgressBar();
                            error.printStackTrace();
                            showSnackBar(coordinatorLayout, VolleyErrorHandler.getMessage(context, error));
                        }
                    },
                    SignUpResponse.class,

              null

            );
            AppController.getInstance().addToRequestQueue(gsonRequest);


        } catch (Exception e) {
            e.printStackTrace();
        }



       /* JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("mobile", mobile);
            jsonObject.put("password", password);
            //registerViaId: 1 -> Normal, 2 -> Facebook, 3 -> Google
            jsonObject.put("registeredVia", 1);
            jsonObject.put("appTypeId", Constants.APP_TYPE_ID);
            jsonObject.put("versionName", Util.getVersionName(context));

            Debugger.i(TAG, "post data " + jsonObject.toString());
            String url = Constants.API + "signUp"; //URL to hit
            showProgressBar(context);
            VolleyGsonRequest<SignUpResponse> gsonRequest = new VolleyGsonRequest<>(url,
                    jsonObject,
                    new Response.Listener<SignUpResponse>() {
                        @Override
                        public void onResponse(SignUpResponse response) {
                            dismissProgressBar();
                            Debugger.i(TAG, "Response " + response);
                            if(response!=null){
                                if(response.isSuccess()){
                                    User user=new User(name, email, mobile);
                                    user.setUserId(response.getUserId());

                                    new AppPreferenceManager(context).putObject(PreferenceKeys.savedUser, user);
                                    gotoActivityByClearingBackStack(DashboardContainer.class);
                                }else{
                                    showSnackBar(coordinatorLayout, response.getMessage());
                                }
                            }else{
                                showSnackBar(coordinatorLayout, getString(R.string.generic_error));
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgressBar();
                            error.printStackTrace();
                            showSnackBar(coordinatorLayout, VolleyErrorHandler.getMessage(context, error));
                        }
                    },
                    SignUpResponse.class,
                    null
            );
            AppController.getInstance().addToRequestQueue(gsonRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }
*/




    }


}
