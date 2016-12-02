package com.rentezee.fragments.chooseaddress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.fragments.payment.PaymentActivity;
import com.rentezee.fragments.payment.PaymentOptionActivity;
import com.rentezee.fragments.profile.general.address.AddressData;
import com.rentezee.fragments.profile.general.address.AddressFragment;
import com.rentezee.fragments.profile.general.address.AddressHolder;
import com.rentezee.main.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 11/28/2016.
 */
public class ChooseAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values=new ArrayList<>();
    private List<ChooseAddressData> itemList;
    private Context context;
    ChooseAddressHolder addressHolder;
    public  static  String product_id;
    ChooseAddressActivity chooseAddressActivity;
    ArrayList<Boolean> booleanGames = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int selectedPostition=0;

    public ChooseAddressAdapter(Context context, List<ChooseAddressData> itemList, ChooseAddressActivity chooseAddressActivity)
    {
        this.itemList = itemList;
        this.context = context;
        this.chooseAddressActivity = chooseAddressActivity;
        inflater = LayoutInflater.from(context);
        sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_choose_address, parent, false);
        addressHolder = new ChooseAddressHolder(view);

        return addressHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final ChooseAddressHolder homeHolder = (ChooseAddressHolder) holder;

        int n = position+1;
        homeHolder.txtAddressheading.setText("Address "+n);
        homeHolder.txtAddress.setText(itemList.get(position).address_label);
        homeHolder.txtAddressDetails.setText(itemList.get(position).address_1);
        homeHolder.txtAddressCity.setText(itemList.get(position).city+","+itemList.get(position).zip_code);

        if(selectedPostition==position)
        {
            homeHolder.radioButtonAddress.setChecked(true);
        }
        else
        {
            homeHolder.radioButtonAddress.setChecked(false);
        }

        homeHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                selectedPostition=position;
                homeHolder.radioButtonAddress.setChecked(true);

                Intent i = new Intent(context, PaymentActivity
                        .class);
                context.startActivity(i);
                notifyDataSetChanged();


            }
        });

        homeHolder.imgEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseAddressActivity.Address_label.setText(itemList.get(position).address_label);
                chooseAddressActivity.Address_line1.setText(itemList.get(position).address_1);
                chooseAddressActivity.Address_line2.setText(itemList.get(position).address_2);
                chooseAddressActivity.Locality.setText(itemList.get(position).country);
                chooseAddressActivity.city.setText(itemList.get(position).city);
                chooseAddressActivity.pincode.setText(itemList.get(position).zip_code);

                chooseAddressActivity.recyclerviewPastOrder.setVisibility(View.GONE);
                chooseAddressActivity.txtAddAddressHeading.setText("View Address");
                chooseAddressActivity.relativeBottomLayout.setVisibility(View.VISIBLE);
                chooseAddressActivity.saveButton.setText("Update");
                chooseAddressActivity.linearlayout.setVisibility(View.VISIBLE);
                product_id = itemList.get(position).product_id;


            }
        });


    }

    private void showMessage(String s)
    {

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return itemList.size();
//        return itemList.size();
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }





}

