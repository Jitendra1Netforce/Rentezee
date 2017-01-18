package com.rentezee.navigation.rentenzee_credit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.navigation.payumoneysdk.WaletPayment;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.SimpleActivity;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.Login;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

import java.util.ArrayList;

public class CreditActivity extends SimpleActivity
{

    private final static String TAG = CreditActivity.class.getSimpleName();
    private Context context;
    RecyclerView lvProducts;
    ArrayList<CreditData> creditDatas = new ArrayList<>();
    CreditAdapter creditAdapter;
    String user_id;
    long  userId;
    User user;
    TextView txtCreditAmount;
    LinearLayoutManager linearLayoutManager;
    DashboardContainer dashboardContainer;
    Button buttonAddMoney;
    public static int i;



    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Rentenzee Credits");
        }

        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();


        user = (User) new AppPreferenceManager(context).getObject(PreferenceKeys.savedUser, User.class);

        if(user != null){

            userId = user.getUserId();
            user_id = Long.toString(userId);
        }
        else
        {

            Intent i = new Intent(CreditActivity.this, Login.class);
            startActivity(i);
            finish();

        }


        txtCreditAmount = (TextView) findViewById(R.id.txtCreditAmount);

        lvProducts=(RecyclerView)findViewById(R.id.lvMyCart);

        buttonAddMoney = (Button) findViewById(R.id.buttonAddMoney);

        i=0;

        buttonAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                i = 1;
                Intent i = new Intent(CreditActivity.this, WaletPayment.class);
                i.putExtra("total_credit",txtCreditAmount.getText().toString());
                startActivity(i);

            }
        });

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        lvProducts.setLayoutManager(linearLayoutManager);
        creditAdapter = new CreditAdapter(context, creditDatas);
        lvProducts.setAdapter(creditAdapter);


        if (user !=null)
        {
            load_refresh();

        }

       /* lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra(Constants.PRODUCT_ID, productListDatas.get(position).product_id);
                gotoActivity(intent);
            }
        });
       */



    }



    @Override
    protected void onResume() {
        super.onResume();


        try {
            invalidateOptionsMenu();
        } catch (Exception e) {

        }

        dashboardContainer.count_cart();
        if(i== 1){



            load_refresh();
        }


    }



    public void load_refresh()
    {
        // recyclerView.setVisibility(View.GONE);
        creditDatas.clear();

        showProgressBar(context);

        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/users/wallet_history/" + user_id)

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        System.out.println("data " + result);

                        if (result != null) {

                            System.out.println("data " + result);

                            JsonObject jsonObject_data = result.getAsJsonObject("data");

                            String credit_amount = jsonObject_data.get("total").getAsString();

                            JsonArray productListArray = jsonObject_data.getAsJsonArray("history");

                            System.out.println("data=====" + productListArray.size());


                            for (int i = 0; i < productListArray.size(); i++)
                            {

                                JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                System.out.println("pro----------"+ jsonObject.get("credit_via").getAsString());

                                String cart_via = jsonObject.get("credit_via").getAsString();
                                String amount = jsonObject.get("amount").getAsString();
                                String created = jsonObject.get("created").getAsString();
                                String type = jsonObject.get("type").getAsString();

                                creditDatas.add(new CreditData(cart_via, amount, created,type));


                            }
                            creditAdapter.notifyDataSetChanged();

                            System.out.println("credit_amount----------"+credit_amount.toString());

                            txtCreditAmount.setText(credit_amount.toString());


                            dismissProgressBar();


                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }


                        dismissProgressBar();

                    }
                });

    }

}
