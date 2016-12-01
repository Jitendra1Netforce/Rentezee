package com.rentezee.fragments.chooseaddress;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.fragments.profile.general.address.AddressAdapter;
import com.rentezee.fragments.profile.general.address.AddressData;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

import java.util.ArrayList;

public class ChooseAddressActivity extends BaseActivity
{

    public  RecyclerView recyclerviewPastOrder;
    public  Context context;
    public static ArrayList<ChooseAddressData> addressdata = new ArrayList<>();
    public  ChooseAddressAdapter addressAdapter;
    public static String id, user_id;
    long userId;
    User user;
    public  RelativeLayout relativeBottomLayout;
    public  TextView txtAddAddressHeading,txtAddressId;
    public  LinearLayout linearlayout;
    public  EditText Address_label, Address_line1, Address_line2, Locality, city, pincode;
    public  Button saveButton;
   CoordinatorLayout coordinatorLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Select Address");
        }

        user = (User) new AppPreferenceManager(getApplicationContext()).getObject(PreferenceKeys.savedUser, User.class);

        if (user != null)
        {

            userId = user.getUserId();
        }

        user_id = Long.toString(userId);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        relativeBottomLayout = (RelativeLayout) findViewById(R.id.relativeBottomLayout);

        Address_label = (EditText) findViewById(R.id.edtAddresslabel);

        Address_line1 = (EditText) findViewById(R.id.edtAddress1);
        Address_line2 = (EditText) findViewById(R.id.edtAddress2);
        Locality = (EditText) findViewById(R.id.edtLocality);
        city = (EditText) findViewById(R.id.edtCity);
        pincode = (EditText) findViewById(R.id.edtPincode);

        saveButton = (Button) findViewById(R.id.buttonSave);

        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);

        txtAddAddressHeading = (TextView) findViewById(R.id.txtAddAddressHeading);

        recyclerviewPastOrder = (RecyclerView) findViewById(R.id.lvProducts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerviewPastOrder.setLayoutManager(mLayoutManager);

        recyclerviewPastOrder.setNestedScrollingEnabled(true);
        fetchData(true);

        txtAddAddressHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtAddAddressHeading.getText().toString().equals("+ Add Address"))
                {

                    txtAddAddressHeading.setText("View Address");
                    Address_label.setText("");
                    Address_line1.setText("");
                    Address_line2.setText("");
                    Locality.setText("");
                    city.setText("");
                    pincode.setText("");
                    saveButton.setText("Save");

                    recyclerviewPastOrder.setVisibility(View.GONE);
                    relativeBottomLayout.setVisibility(View.VISIBLE);
                    linearlayout.setVisibility(View.VISIBLE);

                }
                else
                {

                    txtAddAddressHeading.setText("+ Add Address");
                    recyclerviewPastOrder.setVisibility(View.VISIBLE);
                    txtAddAddressHeading.setVisibility(View.VISIBLE);
                    relativeBottomLayout.setVisibility(View.GONE);
                    linearlayout.setVisibility(View.GONE);

                }

            }
        });


        relativeBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveButton.getText().toString().equals("Update")) {

                    update_address(ChooseAddressAdapter.product_id.toString());


                } else {
                    add_address();

                }


            }
        });


    }

    public  void update_address(String address_id)
    {

        String address_label = Address_label.getText().toString();
        if (!address_label.isEmpty())
        {
            String address_line1 = Address_line1.getText().toString();
            if (!address_line1.isEmpty())
            {

                String locality = Locality.getText().toString();
                if (!locality.isEmpty())
                {

                    String city_d = city.getText().toString();
                    if (!city_d.isEmpty())
                    {

                        String pin = pincode.getText().toString();
                        if (!pin.isEmpty())
                        {

                            int pin_length = pincode.getText().length();
                            if (pin_length == 6)
                            {
                                recyclerviewPastOrder.setVisibility(View.INVISIBLE);
                                addressdata.clear();

                                showProgressBar(context);

                                final JsonObject jsonObject = new JsonObject();

                                jsonObject.addProperty("address_id", address_id);
                                jsonObject.addProperty("user_id", user_id);
                                jsonObject.addProperty("address_label", Address_label.getText().toString());
                                jsonObject.addProperty("address_1", Address_line1.getText().toString());
                                jsonObject.addProperty("address_2", Address_line2.getText().toString());
                                jsonObject.addProperty("country", Locality.getText().toString());
                                jsonObject.addProperty("city", city.getText().toString());
                                jsonObject.addProperty("zip_code", pincode.getText().toString());


                                Ion.with(context)
                                        .load("http://netforce.biz/renteeze/webservice/Users/addresses")
                                        .setJsonObjectBody(jsonObject)
                                        .asJsonObject()
                                        .setCallback(new FutureCallback<JsonObject>() {

                                            @Override
                                            public void onCompleted(Exception e, JsonObject result) {

                                                if (result != null) {
                                                    System.out.println("data=====" + result.toString());

                                                    JsonArray productListArray = result.getAsJsonArray("data");
                                                    System.out.println("data=====" + result.toString());

                                                    for (int i = 0; i < productListArray.size(); i++) {
                                                        JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                                        JsonObject address = jsonObject.getAsJsonObject("Address");

                                                        String product_id = address.get("id").getAsString();
                                                        String address_label = address.get("address_label").getAsString();
                                                        String address1 = address.get("address_1").getAsString();
                                                        String address2 = address.get("address_2").getAsString();
                                                        String city = address.get("city").getAsString();
                                                        String country = address.get("country").getAsString();
                                                        String user_id = address.get("user_id").getAsString();
                                                        String zip_code = address.get("zip_code").getAsString();

                                                        addressdata.add(new ChooseAddressData(product_id, address_label, address1, address2, city, country, user_id, zip_code));
                                                    }
                                                    addressAdapter = new ChooseAddressAdapter(context, addressdata,ChooseAddressActivity.this );
                                                    recyclerviewPastOrder.setAdapter(addressAdapter);
                                                    addressAdapter.notifyDataSetChanged();
                                                    recyclerviewPastOrder.setVisibility(View.VISIBLE);
                                                    dismissProgressBar();

                                                    recyclerviewPastOrder.setVisibility(View.VISIBLE);
                                                    txtAddAddressHeading.setVisibility(View.VISIBLE);
                                                    txtAddAddressHeading.setText("+ Add Address");
                                                    relativeBottomLayout.setVisibility(View.GONE);
                                                    linearlayout.setVisibility(View.GONE);


                                                } else {

                                                    dismissProgressBar();
                                                    Log.e("error", e.toString());
                                                }

                                            }
                                        });

                            }
                            else{

                                Snackbar.make(coordinatorLayout, "Please enter 6 digit pincode", Snackbar.LENGTH_LONG).show();

                            }

                        }
                        else
                        {

                            Snackbar.make(coordinatorLayout,"Please enter pincode", Snackbar.LENGTH_LONG).show();

                        }

                    }
                    else
                    {

                        Snackbar.make(coordinatorLayout,"Please enter city", Snackbar.LENGTH_LONG).show();

                    }
                }
                else {

                    Snackbar.make(coordinatorLayout,"Please enter locality", Snackbar.LENGTH_LONG).show();

                }
            }
            else
            {
                Snackbar.make(coordinatorLayout,"Please enter address line 1", Snackbar.LENGTH_LONG).show();
            }

        }
        else
        {

            Snackbar.make(coordinatorLayout, "Please enter address label", Snackbar.LENGTH_LONG).show();
        }





    }

    public  void add_address() {

        String address_label = Address_label.getText().toString();
        if (!address_label.isEmpty())
        {
            String address_line1 = Address_line1.getText().toString();
            if (!address_line1.isEmpty())
            {

                String locality = Locality.getText().toString();
                if (!locality.isEmpty())
                {

                    String city_d = city.getText().toString();
                    if (!city_d.isEmpty())
                    {

                        String pin = pincode.getText().toString();
                        if (!pin.isEmpty())
                        {

                            int pin_length = pincode.getText().length();
                            if (pin_length == 6)
                            {

                                recyclerviewPastOrder.setVisibility(View.INVISIBLE);
                                addressdata.clear();

                                showProgressBar(context);

                                final JsonObject jsonObject = new JsonObject();

                                jsonObject.addProperty("user_id", user_id);
                                jsonObject.addProperty("address_label", Address_label.getText().toString());
                                jsonObject.addProperty("address_1", Address_line1.getText().toString());
                                jsonObject.addProperty("address_2", Address_line2.getText().toString());
                                jsonObject.addProperty("country", Locality.getText().toString());
                                jsonObject.addProperty("city", city.getText().toString());
                                jsonObject.addProperty("zip_code", pincode.getText().toString());


                                Ion.with(context)
                                        .load("http://netforce.biz/renteeze/webservice/Users/addresses")
                                        .setJsonObjectBody(jsonObject)
                                        .asJsonObject()
                                        .setCallback(new FutureCallback<JsonObject>() {

                                            @Override
                                            public void onCompleted(Exception e, JsonObject result) {

                                                if (result != null) {
                                                    System.out.println("data=====" + result.toString());

                                                    JsonArray productListArray = result.getAsJsonArray("data");
                                                    System.out.println("data=====" + result.toString());

                                                    for (int i = 0; i < productListArray.size(); i++) {
                                                        JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                                        JsonObject address = jsonObject.getAsJsonObject("Address");

                                                        String product_id = address.get("id").getAsString();
                                                        String address_label = address.get("address_label").getAsString();
                                                        String address1 = address.get("address_1").getAsString();
                                                        String address2 = address.get("address_2").getAsString();
                                                        String city = address.get("city").getAsString();
                                                        String country = address.get("country").getAsString();
                                                        String user_id = address.get("user_id").getAsString();
                                                        String zip_code = address.get("zip_code").getAsString();

                                                        addressdata.add(new ChooseAddressData(product_id, address_label, address1, address2, city, country, user_id, zip_code));
                                                    }
                                                    addressAdapter = new ChooseAddressAdapter(context, addressdata,ChooseAddressActivity.this );
                                                    recyclerviewPastOrder.setAdapter(addressAdapter);
                                                    addressAdapter.notifyDataSetChanged();
                                                    recyclerviewPastOrder.setVisibility(View.VISIBLE);
                                                    dismissProgressBar();

                                                    recyclerviewPastOrder.setVisibility(View.VISIBLE);
                                                    txtAddAddressHeading.setVisibility(View.VISIBLE);
                                                    txtAddAddressHeading.setText("+ Add Address");
                                                    relativeBottomLayout.setVisibility(View.GONE);
                                                    linearlayout.setVisibility(View.GONE);


                                                } else {

                                                    dismissProgressBar();
                                                    Log.e("error", e.toString());
                                                }

                                            }
                                        });


                            }
                            else{

                                Snackbar.make(coordinatorLayout, "Please enter 6 digit pincode", Snackbar.LENGTH_LONG).show();

                            }

                        }
                        else
                        {

                            Snackbar.make(coordinatorLayout,"Please enter pincode", Snackbar.LENGTH_LONG).show();

                        }

                    }
                    else
                    {

                        Snackbar.make(coordinatorLayout,"Please enter city", Snackbar.LENGTH_LONG).show();

                    }
                }
                else {

                    Snackbar.make(coordinatorLayout,"Please enter locality", Snackbar.LENGTH_LONG).show();

                }
            }
            else
            {
                Snackbar.make(coordinatorLayout,"Please enter address line 1", Snackbar.LENGTH_LONG).show();
            }

        }
        else
        {

            Snackbar.make(coordinatorLayout, "Please enter address label", Snackbar.LENGTH_LONG).show();
        }




    }


    public  void fetchData(boolean reset) {
        if (reset) {
            reset();
        }
    }

    public  void reset() {

        recyclerviewPastOrder.setVisibility(View.INVISIBLE);
        addressdata.clear();

        showProgressBar(context);

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", user_id);


        Ion.with(context)
                .load("http://netforce.biz/renteeze/webservice/Users/address_list")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {
                            System.out.println("data=====" + result.toString());

                            JsonArray productListArray = result.getAsJsonArray("data");
                            System.out.println("data=====" + result.toString());

                            for (int i = 0; i < productListArray.size(); i++) {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                JsonObject address = jsonObject.getAsJsonObject("Address");

                                String product_id = address.get("id").getAsString();
                                String address_label = address.get("address_label").getAsString();
                                String address1 = address.get("address_1").getAsString();
                                String address2 = address.get("address_2").getAsString();
                                String city = address.get("city").getAsString();
                                String country = address.get("country").getAsString();
                                String user_id = address.get("user_id").getAsString();
                                String zip_code = address.get("zip_code").getAsString();

                                addressdata.add(new ChooseAddressData(product_id, address_label, address1, address2, city, country, user_id, zip_code));
                            }
                            addressAdapter = new ChooseAddressAdapter(context, addressdata,ChooseAddressActivity.this );
                            recyclerviewPastOrder.setAdapter(addressAdapter);
                            addressAdapter.notifyDataSetChanged();
                            recyclerviewPastOrder.setVisibility(View.VISIBLE);
                            dismissProgressBar();


                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });

    }
}