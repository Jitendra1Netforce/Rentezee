package com.rentezee.fragments.profile.general.address;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

import java.util.ArrayList;


public class AddressFragment extends Fragment implements  View.OnClickListener
        {

       public static RecyclerView recyclerviewPastOrder;
       public static Context context;
        public static ArrayList<AddressData> addressdata = new ArrayList<>();
       public static AddressAdapter addressAdapter;
       public static BaseActivity baseActivity;
       public static   String id,user_id;
            long  userId;
        User user;
       public static RelativeLayout relativeBottomLayout;
       public  static  TextView  txtAddAddressHeading,txtAddressId;
       public static LinearLayout linearlayout;
       public static EditText Address_label,Address_line1,Address_line2, Locality,city, pincode;
       public static Button saveButton;
       TextView txtViewAddress;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        context = getActivity();

        baseActivity = new BaseActivity()
        {
          @Override
          public void showProgressBar(Context context)
        {
        super.showProgressBar(context);
        }
        };

        user = (User) new AppPreferenceManager(getActivity()).getObject(PreferenceKeys.savedUser, User.class);

        if(user != null){

        userId = user.getUserId();
        }

        user_id = Long.toString(userId);


         relativeBottomLayout    = (RelativeLayout)  view.findViewById(R.id.relativeBottomLayout);

            txtViewAddress = (TextView)  view.findViewById(R.id.txtViewAddress);
            Address_label = (EditText) view.findViewById(R.id.edtAddresslabel);

            Address_line1 = (EditText) view.findViewById(R.id.edtAddress1);
            Address_line2 = (EditText) view.findViewById(R.id.edtAddress2);
            Locality = (EditText) view.findViewById(R.id.edtLocality);
            city = (EditText) view.findViewById(R.id.edtCity);
            pincode = (EditText) view.findViewById(R.id.edtPincode);

            saveButton = (Button) view.findViewById(R.id.buttonSave);

          linearlayout = (LinearLayout)  view.findViewById(R.id.linearlayout);

         txtAddAddressHeading = (TextView)  view.findViewById(R.id.txtAddAddressHeading);

        recyclerviewPastOrder=(RecyclerView) view.findViewById(R.id.lvProducts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewPastOrder.setLayoutManager(mLayoutManager);

        recyclerviewPastOrder.setNestedScrollingEnabled(true);
        fetchData(true);

            txtViewAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

                txtAddAddressHeading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (txtAddAddressHeading.getText().toString().equals("+ Add Address")) {

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

                        } else {

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

                            update_address(AddressAdapter.product_id.toString(), view);
                        } else {
                            add_address(view);

                        }


                    }
                });


        return view;
        }

            public static void update_address(String address_id,View view)
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

                                        baseActivity.showProgressBar(context);

                                        final JsonObject jsonObject = new JsonObject();

                                        jsonObject.addProperty("address_id", address_id);
                                        jsonObject.addProperty("user_id", user_id);
                                        jsonObject.addProperty("address_label", Address_label.getText().toString());
                                        jsonObject.addProperty("address_1", Address_line1.getText().toString());
                                        jsonObject.addProperty("address_2",  Address_line2.getText().toString());
                                        jsonObject.addProperty("country", Locality.getText().toString());
                                        jsonObject.addProperty("city", city.getText().toString());
                                        jsonObject.addProperty("zip_code",pincode.getText().toString());


                                        Ion.with(context)
                                                .load("http://netforce.biz/renteeze/webservice/Users/addresses")
                                                .setJsonObjectBody(jsonObject)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>()
                                                {

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

                                                                addressdata.add(new AddressData(product_id, address_label, address1, address2, city, country, user_id, zip_code));
                                                            }
                                                            addressAdapter = new AddressAdapter(context, addressdata);
                                                            recyclerviewPastOrder.setAdapter(addressAdapter);
                                                            addressAdapter.notifyDataSetChanged();
                                                            recyclerviewPastOrder.setVisibility(View.VISIBLE);
                                                            baseActivity.dismissProgressBar();

                                                            recyclerviewPastOrder.setVisibility(View.VISIBLE);
                                                            txtAddAddressHeading.setVisibility(View.VISIBLE);
                                                            txtAddAddressHeading.setText("+ Add Address");
                                                            relativeBottomLayout.setVisibility(View.GONE);
                                                            linearlayout.setVisibility(View.GONE);

                                                        } else {

                                                            baseActivity.dismissProgressBar();
                                                            Log.e("error", e.toString());
                                                        }

                                                    }
                                                });

                                    }
                                    else{

                                        Snackbar.make(view,"Please enter 6 digit pincode", Snackbar.LENGTH_LONG).show();

                                    }

                                }
                                else
                                {

                                    Snackbar.make(view,"Please enter pincode", Snackbar.LENGTH_LONG).show();

                                }

                            }
                            else
                            {

                                Snackbar.make(view,"Please enter city", Snackbar.LENGTH_LONG).show();

                            }
                        }
                        else {

                            Snackbar.make(view,"Please enter locality", Snackbar.LENGTH_LONG).show();

                        }
                    }
                    else
                    {
                        Snackbar.make(view,"Please enter address line 1", Snackbar.LENGTH_LONG).show();

                    }

                }
                else
                {

                    Snackbar.make(view, "Please enter address label", Snackbar.LENGTH_LONG).show();
                }

            }

            public static void add_address(View view)
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

                                        baseActivity.showProgressBar(context);

                                        final JsonObject jsonObject = new JsonObject();

                                        jsonObject.addProperty("user_id", user_id);
                                        jsonObject.addProperty("address_label", Address_label.getText().toString());
                                        jsonObject.addProperty("address_1", Address_line1.getText().toString());
                                        jsonObject.addProperty("address_2",  Address_line2.getText().toString());
                                        jsonObject.addProperty("country", Locality.getText().toString());
                                        jsonObject.addProperty("city", city.getText().toString());
                                        jsonObject.addProperty("zip_code",pincode.getText().toString());


                                        Ion.with(context)
                                                .load("http://netforce.biz/renteeze/webservice/Users/addresses")
                                                .setJsonObjectBody(jsonObject)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>()
                                                {

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

                                                                addressdata.add(new AddressData(product_id, address_label, address1, address2, city, country, user_id, zip_code));
                                                            }
                                                            addressAdapter = new AddressAdapter(context, addressdata);
                                                            recyclerviewPastOrder.setAdapter(addressAdapter);
                                                            addressAdapter.notifyDataSetChanged();
                                                            recyclerviewPastOrder.setVisibility(View.VISIBLE);
                                                            baseActivity.dismissProgressBar();

                                                            recyclerviewPastOrder.setVisibility(View.VISIBLE);
                                                            txtAddAddressHeading.setVisibility(View.VISIBLE);
                                                            txtAddAddressHeading.setText("+ Add Address");
                                                            relativeBottomLayout.setVisibility(View.GONE);
                                                            linearlayout.setVisibility(View.GONE);


                                                        } else {

                                                            baseActivity.dismissProgressBar();
                                                            Log.e("error", e.toString());
                                                        }

                                                    }
                                                });

                                    }
                                    else{

                                        Snackbar.make(view,"Please enter 6 digit pincode", Snackbar.LENGTH_LONG).show();

                                    }

                                }
                                else
                                {

                                    Snackbar.make(view,"Please enter pincode", Snackbar.LENGTH_LONG).show();

                                }

                            }
                            else
                            {

                                Snackbar.make(view,"Please enter city", Snackbar.LENGTH_LONG).show();

                            }
                        }
                        else {

                            Snackbar.make(view,"Please enter locality", Snackbar.LENGTH_LONG).show();

                        }
                    }
                    else
                    {
                        Snackbar.make(view,"Please enter address line 1", Snackbar.LENGTH_LONG).show();

                    }

                }
                else
                {

                    Snackbar.make(view, "Please enter address label", Snackbar.LENGTH_LONG).show();
                }







        }


            public static void fetchData(boolean reset)
        {
        if(reset)
        {
        reset();
        }
        }

         public static void reset()
        {

        recyclerviewPastOrder.setVisibility(View.INVISIBLE);
            addressdata.clear();

        baseActivity.showProgressBar(context);

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id",user_id);


        Ion.with(context)
        .load("http://netforce.biz/renteeze/webservice/Users/address_list")
        .setJsonObjectBody(jsonObject)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {

            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (result != null)
                {
                    System.out.println("data=====" + result.toString());

                    JsonArray productListArray = result.getAsJsonArray("data");
                    System.out.println("data=====" + result.toString());

                    for (int i = 0; i < productListArray.size(); i++)
                    {
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

                        addressdata.add(new AddressData(product_id, address_label, address1, address2,city,country,user_id,zip_code));
                    }
                    addressAdapter = new AddressAdapter(context, addressdata);
                    recyclerviewPastOrder.setAdapter(addressAdapter);
                    addressAdapter.notifyDataSetChanged();
                    recyclerviewPastOrder.setVisibility(View.VISIBLE);
                    baseActivity.dismissProgressBar();


                }
                else
                {

                    baseActivity.dismissProgressBar();
                    Log.e("error", e.toString());
                }
            }
        });

        }


@Override
public void setMenuVisibility(final boolean visible) {
        if (visible) {
        //Do your stuff here
        context = getActivity();
        }
        super.setMenuVisibility(visible);
        }


@Override
public void onClick(View view) {

        }


        }
