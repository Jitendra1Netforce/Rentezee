package com.rentezee.navigation.payumoneysdk;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
import com.rentezee.navigation.chooseaddress.ChooseAddressActivity;
import com.rentezee.navigation.my_cart.MyCart;
import com.rentezee.navigation.transaction_details.TransactionDetailActivity;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.SimpleActivity;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

public class PayUMoneyActivity extends SimpleActivity
{

    EditText amt;
    Button pay ;
    public static final String TAG = "Rentenzee";
    public static String  email_id,mobile_number,user_name;
    User user;
    String transaction_id,order_id,total_order_item,total_payment,order_date,delivery_date;
    public  static ArrayList<String> order_name = new ArrayList<>();
    Context context;
    String address_id_v;
    TextView txtPayableAmount,txtCredit,txtPayableAmount2,tvCreditHeading,txtPayuMoneyHeading,tvRentenzeeCreditAmount;
    Double amount;
    String payment_amount,user_credit;
    int total_payable_amount;
    double payto_payumoney_amout,wallet_deduct_amount,available_wollet_amount;
    CheckBox creditcheckbox,payumoneyCheckbox;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_umoney);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Payment");
        }

        Intent intent = getIntent();

        address_id_v = intent.getStringExtra("address_id").toString();

        payment_amount = intent.getStringExtra("total_amount").toString();

        user_credit = intent.getStringExtra("rentenzee_credit").toString();

        Double d = new Double(payment_amount.toString());

        total_payable_amount =d.intValue();

        txtPayableAmount = (TextView) findViewById(R.id.txtPayableAmount);

        tvRentenzeeCreditAmount = (TextView) findViewById(R.id.tvRentenzeeCreditAmount);

        tvCreditHeading = (TextView) findViewById(R.id.tvCreditHeading);

        txtPayuMoneyHeading = (TextView) findViewById(R.id.txtPayuMoneyHeading);

        txtPayableAmount2 = (TextView) findViewById(R.id.txtPayableAmount2);

        creditcheckbox = (CheckBox) findViewById(R.id.rentenzeeCheckbox);

        payumoneyCheckbox = (CheckBox) findViewById(R.id.payumoneyCheckbox);

        txtCredit = (TextView) findViewById(R.id.tvRentenzeeCredit);

        txtCredit.setText(user_credit);

        tvRentenzeeCreditAmount.setText(user_credit);

        txtPayableAmount.setText(String.valueOf(total_payable_amount));

        txtPayableAmount2.setText(String.valueOf(total_payable_amount));

       /* if(Integer.valueOf(txtCredit.getText().toString()) <=Integer.valueOf(txtPayableAmount.getText().toString()))
        {

            double credit = Double.valueOf(txtCredit.getText().toString());

            double amount =   Double.valueOf(txtPayableAmount.getText().toString());

            double payble_amout  =  amount-  credit;

        }
        */

        amt = (EditText) findViewById(R.id.amount);

        amt.setText(txtPayableAmount.getText().toString());

        amount = Double.parseDouble(amt.getText().toString());

        pay = (Button) findViewById(R.id.pay);

        user = (User) new AppPreferenceManager(getApplicationContext()).getObject(PreferenceKeys.savedUser, User.class);

         email_id = user.getEmail().toString();

        mobile_number = user.getMobile().toString();

        user_name = user.getName().toString();


        tvCreditHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        txtPayuMoneyHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });



        creditcheckbox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(creditcheckbox.isChecked())
                {

                    if(total_payable_amount < Integer.valueOf(user_credit))
                    {
                        txtPayableAmount2.setText("0");
                        payumoneyCheckbox.setChecked(false);
                        tvRentenzeeCreditAmount.setText(String.valueOf(Integer.valueOf(user_credit)-total_payable_amount));

                    }
                    else if(total_payable_amount == Integer.valueOf(user_credit))
                    {
                        txtPayableAmount2.setText("0");
                        payumoneyCheckbox.setChecked(false);
                        tvRentenzeeCreditAmount.setText(String.valueOf(Integer.valueOf(user_credit) - total_payable_amount));

                    }
                    else
                    {
                        int add_wollet_money = total_payable_amount - Integer.valueOf(user_credit);
                        txtPayableAmount2.setText(String.valueOf(add_wollet_money));
                        payumoneyCheckbox.setChecked(true);

                        tvRentenzeeCreditAmount.setText("0");
                    }

                }
                else
                {

                    txtPayableAmount2.setText(String.valueOf(total_payable_amount));

                    tvRentenzeeCreditAmount.setText(String.valueOf(user_credit));

                    payumoneyCheckbox.setChecked(true);

                }


            }
        });


        payumoneyCheckbox.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View view)
            {

                if (payumoneyCheckbox.isChecked())
                {
                    if(creditcheckbox.isChecked())
                    {

                        if(total_payable_amount < Integer.valueOf(user_credit))
                        {

                            txtPayableAmount2.setText("0");
                            payumoneyCheckbox.setChecked(false);

                            tvRentenzeeCreditAmount.setText(String.valueOf(Integer.valueOf(user_credit)- total_payable_amount ));

                        }
                        else if(total_payable_amount == Integer.valueOf(user_credit))
                        {

                            txtPayableAmount2.setText("0");
                            payumoneyCheckbox.setChecked(false);

                            tvRentenzeeCreditAmount.setText(String.valueOf(Integer.valueOf(user_credit) - total_payable_amount));


                        }
                        else
                        {
                            int add_wollet_money = total_payable_amount - Integer.valueOf(user_credit);
                            txtPayableAmount2.setText(String.valueOf(add_wollet_money));
                            payumoneyCheckbox.setChecked(true);

                            tvRentenzeeCreditAmount.setText("0");

                        }


                    }
                    else
                    {


                        txtPayableAmount2.setText(String.valueOf(total_payable_amount));
                        payumoneyCheckbox.setChecked(true);

                        tvRentenzeeCreditAmount.setText(user_credit);


                    }


                }
                else
                {

                   /* if(creditcheckbox.isChecked())
                    {

                        int add_wollet_money = total_payable_amount - Integer.valueOf(user_credit);
                        txtPayableAmount2.setText(String.valueOf(add_wollet_money));

                    }
                    else
                    {
                        txtPayableAmount2.setText(String.valueOf(total_payable_amount));
                    }*/


                    if(creditcheckbox.isChecked())
                    {

                        if(total_payable_amount < Integer.valueOf(user_credit))
                        {

                            txtPayableAmount2.setText("0");
                            payumoneyCheckbox.setChecked(false);

                            tvRentenzeeCreditAmount.setText(String.valueOf(Integer.valueOf(user_credit) -total_payable_amount ));


                        }
                        else if(total_payable_amount == Integer.valueOf(user_credit))
                        {

                            txtPayableAmount2.setText("0");
                            payumoneyCheckbox.setChecked(false);

                            tvRentenzeeCreditAmount.setText(String.valueOf(Integer.valueOf(user_credit) - total_payable_amount));


                        }
                        else
                        {
                            int add_wollet_money = total_payable_amount - Integer.valueOf(user_credit);
                            txtPayableAmount2.setText(String.valueOf(add_wollet_money));
                            payumoneyCheckbox.setChecked(true);


                            tvRentenzeeCreditAmount.setText("0");

                        }


                    }
                    else
                    {


                        txtPayableAmount2.setText(String.valueOf(total_payable_amount));
                        payumoneyCheckbox.setChecked(true);

                        tvRentenzeeCreditAmount.setText(user_credit);



                    }


                }
            }
        });


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
        }
        else
        {
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

        if(Integer.valueOf(txtPayableAmount.getText().toString()) > Integer.valueOf(txtCredit.getText().toString()))
        {


            if(creditcheckbox.isChecked())
            {

                double credit = Double.valueOf(txtCredit.getText().toString());

                double amount =   Double.valueOf(txtPayableAmount.getText().toString());

                payto_payumoney_amout  =  amount -  credit;

                available_wollet_amount = 0;

                wallet_deduct_amount = amount;


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


                builder.setAmount(payto_payumoney_amout)
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

                calculateServerSideHashAndInitiatePayment(paymentParam);


            }
            else
            {

                double credit = Double.valueOf(txtCredit.getText().toString());

                double amount =   Double.valueOf(txtPayableAmount.getText().toString());

                payto_payumoney_amout  =  amount;

                available_wollet_amount = credit;

                wallet_deduct_amount = 0;

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


                builder.setAmount(payto_payumoney_amout)
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

                calculateServerSideHashAndInitiatePayment(paymentParam);





            }


        }
        else if(Integer.valueOf(txtPayableAmount.getText().toString()) < Integer.valueOf(txtCredit.getText().toString()))
        {


            if (creditcheckbox.isChecked())
            {

                double credit = Double.valueOf(txtCredit.getText().toString());

                double amount =   Double.valueOf(txtPayableAmount.getText().toString());

                payto_payumoney_amout  = 0;

                available_wollet_amount = credit - amount;

                wallet_deduct_amount = amount;

                post_data_server(address_id_v);


            }
            else
            {

                double credit = Double.valueOf(txtCredit.getText().toString());

                double amount =   Double.valueOf(txtPayableAmount.getText().toString());

                payto_payumoney_amout  = amount;

                available_wollet_amount = credit;

                wallet_deduct_amount = 0;


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


                builder.setAmount(payto_payumoney_amout)
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

                calculateServerSideHashAndInitiatePayment(paymentParam);



            }


        }
        else
        {


            if (creditcheckbox.isChecked())
            {

                double credit = Double.valueOf(txtCredit.getText().toString());

                double amount =   Double.valueOf(txtPayableAmount.getText().toString());

                payto_payumoney_amout  = 0;

                available_wollet_amount = credit - amount;

                wallet_deduct_amount = amount;

                post_data_server(address_id_v);

            }
            else
            {

                double credit = Double.valueOf(txtCredit.getText().toString());

                double amount =   Double.valueOf(txtPayableAmount.getText().toString());

                payto_payumoney_amout  = amount;

                available_wollet_amount = credit;

                wallet_deduct_amount = 0;


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


                builder.setAmount(payto_payumoney_amout)
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

                calculateServerSideHashAndInitiatePayment(paymentParam);



            }


        }



    /*    if (creditcheckbox.isChecked())
        {

            if(txtPayableAmount2.getText().toString().equals("0"))
            {

                available_wollet_amount = Integer.valueOf(txtCredit.getText().toString()) - Integer.valueOf(txtPayableAmount.getText().toString());

                payto_payumoney_amout = 0;

                wallet_deduct_amount = Double.valueOf(txtPayableAmount.getText().toString());

                post_data_server(address_id_v);

            }
            else
            {

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

                double credit = Double.valueOf(txtCredit.getText().toString());

                double amount =   Double.valueOf(txtPayableAmount.getText().toString());

                payto_payumoney_amout  =  amount -  credit;

                available_wollet_amount = credit - amount;

                wallet_deduct_amount = amount;

                builder.setAmount(payto_payumoney_amout)
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

                calculateServerSideHashAndInitiatePayment(paymentParam);

            }


        }
        else
        {


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

            int credit = Integer.valueOf(txtCredit.getText().toString());

            double amount =   Double.valueOf(txtPayableAmount.getText().toString());

            payto_payumoney_amout  =  amount;

            available_wollet_amount = credit;

            wallet_deduct_amount = 0;

            builder.setAmount(payto_payumoney_amout)
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

            calculateServerSideHashAndInitiatePayment(paymentParam);


        }
*/
        /*if(Integer.valueOf(txtCredit.getText().toString()) < Integer.valueOf(txtPayableAmount.getText().toString()))
        {

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

            double credit = Double.valueOf(txtCredit.getText().toString());

            double amount =   Double.valueOf(txtPayableAmount.getText().toString());

            payto_payumoney_amout  =  amount -  credit;

            available_wollet_amount = 0;

            wallet_deduct_amount = credit;

            builder.setAmount(payto_payumoney_amout)
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

//         server side call required to calculate hash with the help of <salt>
//             <salt> is already shared along with merchant <key>
     *//*        serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)

             (e.g.)

             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)

             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc
*//*

            // Recommended
            calculateServerSideHashAndInitiatePayment(paymentParam);

//        testing purpose

       *//* String salt = "";
        String serverCalculatedHash=hashCal(key+"|"+txnId+"|"+getAmount()+"|"+productName+"|"
                +firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(MyActivity.this, paymentParam);*//*


        }
        else
        {

            available_wollet_amount = Integer.valueOf(txtCredit.getText().toString()) - Integer.valueOf(txtPayableAmount.getText().toString());

            payto_payumoney_amout = 0;

            wallet_deduct_amount = Double.valueOf(txtPayableAmount.getText().toString());

            post_data_server(address_id_v);

        }
*/

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

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(PayUMoneyActivity.this, paymentParam);
                        } else {
                            Toast.makeText(PayUMoneyActivity.this,
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
                    Toast.makeText(PayUMoneyActivity.this,
                            PayUMoneyActivity.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PayUMoneyActivity.this,
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
                post_data_server(address_id_v);

               /* Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
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
                showDialogMessage("User returned without login");
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


    private void post_data_server(String address_id)
    {

        System.out.println("wallet_deduct_amount-----"+wallet_deduct_amount+"wallet_available_amount---------"+available_wollet_amount);


        order_name.clear();
         showProgressBar(this);
        System.out.println("user_id============"+ChooseAddressActivity.user_id.toString());

        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(MyCart.myCartDatas).getAsJsonArray();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("product_array", myCustomArray.toString());
        jsonObject.addProperty("user_id",ChooseAddressActivity.user_id.toString());
        jsonObject.addProperty("order_qty",myCustomArray.size());
        jsonObject.addProperty("address_id",address_id.toString());
        jsonObject.addProperty("discount_amount",ChooseAddressActivity.discount_amount.toString());
        jsonObject.addProperty("wallet_deduct_amount",String.valueOf(wallet_deduct_amount));
        jsonObject.addProperty("wallet_available_amount",String.valueOf(available_wollet_amount));
        jsonObject.addProperty("service_tax",ChooseAddressActivity.service_tax.toString());
        jsonObject.addProperty("other_tax", ChooseAddressActivity.other_tax.toString());
        jsonObject.addProperty("shipping_charge", ChooseAddressActivity.shipping_charge.toString());
        jsonObject.addProperty("subtotal", ChooseAddressActivity.subtotal.toString());
        jsonObject.addProperty("total_amount", ChooseAddressActivity.total_amount.toString());
        jsonObject.addProperty("pay_to_payumoney",String.valueOf(payto_payumoney_amout));

        Log.e("my data", jsonObject.toString());

        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/shop/checkout")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null)
                        {

                            System.out.println("array data ============" + result.toString());

                            dismissProgressBar();

                            JsonObject jsonObject1 = result.getAsJsonObject("trasaction_details");

                            transaction_id = jsonObject1.get("transaction_id").getAsString();

                            order_id = jsonObject1.get("order_id").getAsString();

                            total_order_item = jsonObject1.get("total_order_item").getAsString();

                            total_payment = jsonObject1.get("total_payment").getAsString();

                            order_date = jsonObject1.get("order_date").getAsString();

                            delivery_date = jsonObject1.get("deliveri_date").getAsString();


                            JsonArray productListArray = jsonObject1.getAsJsonArray("orderList");

                            for (int i = 0; i < productListArray.size(); i++) {

                                JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                String product_id = jsonObject.get("product_id").getAsString();

                                String product_name = jsonObject.get("product_name").getAsString();

                                //JsonArray order_item = productListArray.;
                                order_name.add(product_name);

                            }

                            dismissProgressBar();

                            txtCredit.setText(String.valueOf(available_wollet_amount));

                            String  device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                            delete_to_cart(device_id);

                            Intent i = new Intent(context, TransactionDetailActivity.class);
                            i.putExtra("transaction_id", transaction_id);
                            i.putExtra("order_id", order_id);
                            i.putExtra("total_order_item", total_order_item);
                            i.putExtra("total_payment", total_payment);
                            i.putExtra("order_date", order_date);
                            i.putExtra("delivery_date", delivery_date);

                            context.startActivity(i);
                            ((Activity)context).finish();

                        }

                    }
                });


    }


    private void delete_to_cart(final String device_id) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device_id", device_id);

        Ion.with(context)
                .load("https://netforcesales.com/renteeze/webservice/products/clear_cart")
                .setJsonObjectBody(jsonObject)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        if (result != null)
                        {

                            System.out.println("result========"+result.toString());

                        }

                    }

                });

    }



}




