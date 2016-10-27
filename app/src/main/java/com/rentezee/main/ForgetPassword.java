package com.rentezee.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.Util;
import com.rentezee.helpers.Validator;
import com.rentezee.helpers.VolleyErrorHandler;
import com.rentezee.helpers.VolleyGsonRequest;
import com.rentezee.pojos.GenericResponse;
import com.rentezee.pojos.LoginResponse;

public class ForgetPassword extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ForgetPassword.class.getSimpleName();
    private Context context;
    private CoordinatorLayout coordinatorLayout;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        etEmail = (EditText) findViewById(R.id.etEmail);
        findViewById(R.id.btnSendMail).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendMail:
                String email = etEmail.getText().toString();
                if (email.isEmpty()) {
                    showSnackBar(coordinatorLayout, getString(R.string.error_email_empty));
                    return;
                }

                if (!Validator.isValidEmail(email)) {
                    showSnackBar(coordinatorLayout, getString(R.string.error_email_not_valid));
                    return;
                }
                sendEmail(email);
                break;
        }
    }

    private void sendEmail(String email){
        //URL to hit
        String url= Constants.API + "sendEmail/passwordRecovery/" + email;
        showProgressBar(context);
        VolleyGsonRequest<GenericResponse> gsonRequest = new VolleyGsonRequest<>(url,
                null,
                new Response.Listener<GenericResponse>() {
                    @Override
                    public void onResponse(GenericResponse response) {
                        dismissProgressBar();
                        if(response!=null){
                            Debugger.i(TAG, response.toString());
                            if(response.isSuccess()){
                                goToLoginPage(response.getMessage());
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
                GenericResponse.class,
                null
        );
        AppController.getInstance().addToRequestQueue(gsonRequest);
    }

    private void goToLoginPage(String msg){
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(Login.class);
                finish();
            }
        };
        showSnackBar(coordinatorLayout, msg, getString(R.string.log_in), onClickListener);
    }
}
