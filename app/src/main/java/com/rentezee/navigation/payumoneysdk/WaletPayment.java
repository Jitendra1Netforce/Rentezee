package com.rentezee.navigation.payumoneysdk;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.SimpleActivity;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

public class WaletPayment extends SimpleActivity
{

    EditText amt;
    Button pay ;
    public static final String TAG = "Rentenzee";
    public  static  String  email_id,mobile_number,user_name;
    User user;
    String transaction_id,order_id,total_order_item,total_payment,order_date,delivery_date;
    public  static ArrayList<String> order_name = new ArrayList<>();
    Context context;
    // String address_id_v;
    TextView txtPayableAmount;
    Double amount = 10.0;
    String payment_amount ;
    String user_id;
    long  userId;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_u);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Wallet Payment");
        }

        Intent intent = getIntent();

        payment_amount = intent.getStringExtra("total_credit").toString();

        txtPayableAmount = (TextView) findViewById(R.id.txtPayableAmount);

        txtPayableAmount.setText(payment_amount);

        amt = (EditText) findViewById(R.id.amount);

        pay = (Button) findViewById(R.id.pay);

        user = (User) new AppPreferenceManager(getApplicationContext()).getObject(PreferenceKeys.savedUser, User.class);

        if(user != null){

            userId = user.getUserId();
            user_id = Long.toString(userId);
        }


        email_id = user.getEmail().toString();

        mobile_number = user.getMobile().toString();

        user_name = user.getName().toString();

    }


    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private double getAmount()
    {

        if (isDouble(amt.getText().toString())) {
            amount = Double.parseDouble(amt.getText().toString());
            return amount;
        } else {
            Toast.makeText(getApplicationContext(), "Paying Default Amount ₹10", Toast.LENGTH_LONG).show();
            return amount;
        }
    }

    public void makePayment(View view)
    {

        if (amt.getText().toString().equals(""))
        {

            Toast.makeText(getApplicationContext(),"Please Enter Amount",Toast.LENGTH_SHORT).show();
            return;
        }

        String phone = mobile_number;
        String productName = "product_name";
        String firstName = user_name;
        String txnId = "0nf7" + System.currentTimeMillis();
        String email=email_id;
        String sUrl = "https://test.payumoney.com/mobileapp/payumoney/success.php";
        String fUrl = "https://test.payumoney.com/mobileapp/payumoney/failure.php";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        boolean isDebug = true;
        String key = "dRQuiA";
        String merchantId = "4928174" ;

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();


        builder.setAmount(getAmount())
                .setTnxId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(sUrl)
                .setfUrl(fUrl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setIsDebug(isDebug)
                .setKey(key)
                .setMerchantId(merchantId);

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

//             server side call required to calculate hash with the help of <salt>
//             <salt> is already shared along with merchant <key>
     /*        serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)

             (e.g.)

             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)

             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc
*/

        // Recommended
        calculateServerSideHashAndInitiatePayment(paymentParam);

//        testing purpose

       /* String salt = "";
        String serverCalculatedHash=hashCal(key+"|"+txnId+"|"+getAmount()+"|"+productName+"|"
                +firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(MyActivity.this, paymentParam);*/

    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam)
    {

        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(this, "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(WaletPayment.this, paymentParam);
                        } else {
                            Toast.makeText(WaletPayment.this,
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(WaletPayment.this,
                            WaletPayment.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WaletPayment.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            /*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*/

            if (resultCode == RESULT_OK)
            {
                add_to_wallet();

              /*  Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                showDialogMessage("Payment Success Id : " + paymentId);
                */

            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                showDialogMessage("cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("failure");
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                showDialogMessage("User returned without payment");
            }
        }
    }

    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }


    private void add_to_wallet()
    {
        showProgressBar(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", user_id);
        jsonObject.addProperty("amount", amt.getText().toString());
        jsonObject.addProperty("type", "payUMoney");

        Ion.with(context)
                .load("https://netforcesales.com/renteeze/webservice/users/add_wallet_money")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null)
                        {
                            System.out.println("result========"+result.toString());


                            JsonObject jsonObject_data = result.getAsJsonObject("data");

                            String total_amount = jsonObject_data.get("total").getAsString();

                            txtPayableAmount.setText(total_amount.toString());


                            amt.setText("");

                        }
                        dismissProgressBar();

                       finish();



                    }

                });

    }



}




