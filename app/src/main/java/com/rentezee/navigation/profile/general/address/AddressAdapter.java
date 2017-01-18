package com.rentezee.navigation.profile.general.address;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.main.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 11/25/2016.
 */
public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private final LayoutInflater inflater;
    private List<AddressData> itemList;
    private Context context;
    AddressHolder addressHolder;
    public  static  String product_id;


    public AddressAdapter(Context context, List<AddressData> itemList)
    {

        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_address, parent, false);
        addressHolder = new AddressHolder(view);

        return addressHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        AddressHolder homeHolder = (AddressHolder) holder;
        int n = position+1;
        homeHolder.txtAddressheading.setText("Address "+n);
        homeHolder.txtAddress.setText(itemList.get(position).address_label);
        homeHolder.txtAddressDetails.setText(itemList.get(position).address_1);
        homeHolder.txtAddressCity.setText(itemList.get(position).city+","+itemList.get(position).zip_code);

        homeHolder.layoutRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_to_cart(itemList.get(position).product_id);
                AddressFragment.addressdata.remove(position);
                notifyDataSetChanged();

            }
        });

        homeHolder.layoutAddressEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                AddressFragment.Address_label.setText(itemList.get(position).address_label);
                AddressFragment.Address_line1.setText(itemList.get(position).address_1);
                AddressFragment.Address_line2.setText(itemList.get(position).address_2);
                AddressFragment.Locality.setText(itemList.get(position).country);
                AddressFragment.city.setText(itemList.get(position).city);
                AddressFragment.pincode.setText(itemList.get(position).zip_code);

                AddressFragment.recyclerviewPastOrder.setVisibility(View.GONE);
                AddressFragment.txtAddAddressHeading.setText("View Address");
                AddressFragment.relativeBottomLayout.setVisibility(View.VISIBLE);
                AddressFragment.saveButton.setText("Update");
                AddressFragment.linearlayout.setVisibility(View.VISIBLE);
                product_id =itemList.get(position).product_id;


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


    private void delete_to_cart(final String cart_id )
    {

        Ion.with(context)
                .load("https://netforcesales.com/renteeze/webservice/Users/delete_address/" + cart_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null) {

                            //myCart.fetchData(true);
                        }

                    }

                });

    }




}
