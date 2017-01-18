package com.rentezee.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.JToast;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.RegisterVia;
import com.rentezee.helpers.SimpleActivity;
import com.rentezee.helpers.Util;
import com.rentezee.helpers.Validator;
import com.rentezee.helpers.VolleyErrorHandler;
import com.rentezee.helpers.VolleyGsonRequest;
import com.rentezee.pojos.LoginResponse;
import com.rentezee.pojos.SignUpResponse;
import com.rentezee.pojos.User;
import com.rentezee.services.MyFirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import static com.rentezee.main.R.id.coordinatorLayout;

public class Login extends SimpleActivity implements View.OnClickListener
{

    private static final String TAG = Login.class.getSimpleName();
    private static final int RC_SIGN_IN = 1091;
    private Context context;
    private CoordinatorLayout coordinatorLayout;
    private EditText etEmail, etPassword;
    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    String reg_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        reg_id =  FirebaseInstanceId.getInstance().getToken();

        System.out.println("token ==========="+ reg_id);

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        findViewById(R.id.ivFbLogin).setOnClickListener(this);
        findViewById(R.id.ivGoogleLogin).setOnClickListener(this);
        findViewById(R.id.btnLogIn).setOnClickListener(this);
        findViewById(R.id.tvGoForSignUp).setOnClickListener(this);
        findViewById(R.id.tvForgetPwd).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivGoogleLogin:
                try {
                    if (!googleApiClient.isConnected()) {
                        signIn();
                        return;
                    }
                    Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    Debugger.i("Status", status.toString());
                                    if (status.getStatus().getStatusCode() != GoogleSignInStatusCodes.SIGN_IN_REQUIRED) {
                                        signIn();
                                    } else {
                                        signOut();
                                    }
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.ivFbLogin:
                showProgressBar(context);
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                //JToast.makeText(context, "Facebook Login success", Toast.LENGTH_LONG).show();
                                getFbProfileData(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                dismissProgressBar();
                                JToast.makeText(context, "Facebook Login canceled", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                dismissProgressBar();
                                JToast.makeText(context, "Facebook Login error", Toast.LENGTH_LONG).show();
                                exception.printStackTrace();
                            }
                        });
                LoginManager.getInstance()
                        .logInWithReadPermissions(
                                this,
                                Arrays.asList("public_profile", "email")
                        );
                break;
            case R.id.btnLogIn:
                normalLogin();
                break;
            case R.id.tvGoForSignUp:
                gotoActivity(SignUp.class);
                break;
            case R.id.tvForgetPwd:
                gotoActivity(ForgetPassword.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        //Facebook callback manager
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        signIn();
                    }
                });

    }

    /**
     * handle google sign in data
     *
     * @param result GoogleSignInResult
     */
    private void handleSignInResult(GoogleSignInResult result)
    {
        Debugger.i(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            String name = acct.getDisplayName();
            String email = acct.getEmail();
            Uri image = acct.getPhotoUrl();
            Debugger.i(TAG, "Name " + name + ", Email " + email);
            socialLogin(name, email, RegisterVia.google);

            Debugger.i(TAG, "Name " + acct.getDisplayName() + ", Email " + acct.getEmail());
        }
        else
        {
            // Signed out, show unauthenticated UI.
            Debugger.i(TAG, "Google Sign in not authenticated");
            showSnackBar(coordinatorLayout, "Google Sign in not authenticated");
        }
    }

    private void getFbProfileData(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                        dismissProgressBar();
                        try {
                            if (jsonObject != null) {
                                String name = null;
                                if (jsonObject.has("name")) {
                                    name = jsonObject.getString("name");
                                }
                                String email = null;
                                if (jsonObject.has("email")) {
                                    email = jsonObject.getString("email");
                                }
                                Debugger.i(TAG, "Name " + name + ", Email " + email);
                                socialLogin(name, email, RegisterVia.facebook);

                            } else {
                                showSnackBar(coordinatorLayout, "Not able to fetch data from facebook");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name, first_name, last_name, email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void socialLogin(final String name, final String email, RegisterVia registerVia) {
        //Post data to sever
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("mobile", "");
            jsonObject.put("reg_id",reg_id);
            //registerViaId: 1 -> Normal, 2 -> Facebook, 3 -> Google
            if (registerVia == RegisterVia.facebook)
            {
                jsonObject.put("registeredVia", 2);
            }
            else
            {
                jsonObject.put("registeredVia", 3);
            }

            jsonObject.put("appTypeId", Constants.APP_TYPE_ID);
            jsonObject.put("versionName", Util.getVersionName(context));
             String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            jsonObject.put("app_id", android_id);

            Debugger.i(TAG, "post data " + jsonObject.toString());
            String url = Constants.API + "socialLogin"; //URL to hit
            showProgressBar(context);
            VolleyGsonRequest<LoginResponse> gsonRequest = new VolleyGsonRequest<>("https://netforcesales.com/renteeze/webservice/Users/fb_login",
                    jsonObject,
                    new Response.Listener<LoginResponse>() {
                        @Override
                        public void onResponse(LoginResponse response) {
                            dismissProgressBar();
                            Debugger.i(TAG, "Response " + response);
                            if (response != null) {
                                if (response.isSuccess())
                                {

                                    System.out.println("some data ========="+  response.getData().toString());
                                    new AppPreferenceManager(context).putObject(PreferenceKeys.savedUser, response.getData());
                                    gotoActivityByClearingBackStack(DashboardContainer.class);
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

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgressBar();
                            error.printStackTrace();
                            showSnackBar(coordinatorLayout, VolleyErrorHandler.getMessage(context, error));
                        }
                    },
                    LoginResponse.class,
                    null
            );
            AppController.getInstance().addToRequestQueue(gsonRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void normalLogin()
    {
        String email = etEmail.getText().toString();
        if (email.isEmpty()) {
            showSnackBar(coordinatorLayout, getString(R.string.error_email_empty));
            return;
        }

        if (!Validator.isValidEmail(email)) {
            showSnackBar(coordinatorLayout, getString(R.string.error_email_not_valid));
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

        /*//URL to hit
        StringBuilder stringBuilder = new StringBuilder(Constants.API + "login/1/" + Util.getVersionName(context));
        stringBuilder.append("/").append(email);
        stringBuilder.append("/").append(password);
        String url = stringBuilder.toString();
        System.out.println("url==========="+url);*/

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("reg_id",reg_id);
            //registerViaId: 1 -> Normal, 2 -> Facebook, 3 -> Google

            showProgressBar(context);

             VolleyGsonRequest<LoginResponse> gsonRequest = new VolleyGsonRequest<>("https://netforcesales.com/renteeze/webservice/Users/authenticate",
                jsonObject,
                new Response.Listener<LoginResponse>() {
                    @Override
                    public void onResponse(LoginResponse response) {
                        dismissProgressBar();
                        Debugger.i(TAG, "Response " + response);
                        if (response != null) {
                            if (response.isSuccess())
                            {
                                System.out.println("some data ========="+  response.getData().toString());
                                new AppPreferenceManager(context).putObject(PreferenceKeys.savedUser, response.getData());
                                gotoActivityByClearingBackStack(DashboardContainer.class);
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

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressBar();
                        error.printStackTrace();
                        showSnackBar(coordinatorLayout, VolleyErrorHandler.getMessage(context, error));
                    }
                },
                LoginResponse.class,
                null
        );
        AppController.getInstance().addToRequestQueue(gsonRequest);


    } catch (JSONException e) {
        e.printStackTrace();
    }


     /*   VolleyGsonRequest<LoginResponse> gsonRequest = new VolleyGsonRequest<>(url,
                null,
                new Response.Listener<LoginResponse>() {
                    @Override
                    public void onResponse(LoginResponse response) {
                        dismissProgressBar();
                        if (response != null) {
                            Debugger.i(TAG, response.toString());
                            if (response.isSuccess()) {
                                new AppPreferenceManager(context).putObject(PreferenceKeys.savedUser, response.getData());
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
                LoginResponse.class,
                null
        );
        AppController.getInstance().addToRequestQueue(gsonRequest);*/
    }

}
