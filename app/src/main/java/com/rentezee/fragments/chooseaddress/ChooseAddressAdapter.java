package com.rentezee.fragments.chooseaddress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.fragments.my_cart.DeliveryActivity;
import com.rentezee.fragments.my_cart.MyCart;
import com.rentezee.fragments.payment.PaymentActivity;
import com.rentezee.fragments.payment.TransactionDetailActivity;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

import org.json.JSONArray;

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
    public Context context;
    ChooseAddressHolder addressHolder;
    public  static  String product_id;
    ChooseAddressActivity chooseAddressActivity;
    ArrayList<Boolean> booleanGames = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int selectedPostition=0;
    BaseActivity baseActivity;
    String transaction_id,order_id,total_order_item,total_payment,order_date,delivery_date;
    public  static  ArrayList<String>  order_name = new ArrayList<>();



    public ChooseAddressAdapter(Context context, List<ChooseAddressData> itemList, ChooseAddressActivity chooseAddressActivity)
    {
        this.itemList = itemList;
        this.context = context;
        this.chooseAddressActivity = chooseAddressActivity;
        inflater = LayoutInflater.from(context);
        sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


         baseActivity = new BaseActivity() {
            @Override
            public void showProgressBar(Context context) {
                super.showProgressBar(context);
            }
        };


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

        homeHolder.relativeLayout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                selectedPostition=position;
                homeHolder.radioButtonAddress.setChecked(true);

                notifyDataSetChanged();
                post_data_server( itemList.get(position).product_id);

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

    private void post_data_server(String address_id)
    {

        order_name.clear();
        baseActivity.showProgressBar(context);
        System.out.println("user_id============"+ChooseAddressActivity.user_id.toString());

        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(MyCart.myCartDatas).getAsJsonArray();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("product_array", myCustomArray.toString());
        jsonObject.addProperty("user_id",ChooseAddressActivity.user_id.toString());
        jsonObject.addProperty("order_qty",myCustomArray.size());
        jsonObject.addProperty("address_id",address_id.toString());
        jsonObject.addProperty("discount_amount",ChooseAddressActivity.discount_amount.toString());
        jsonObject.addProperty("rentenzee_credit_amount",ChooseAddressActivity.rentenzee_credit_amount.toString());
        jsonObject.addProperty("service_tax",ChooseAddressActivity.service_tax.toString());
        jsonObject.addProperty("other_tax",ChooseAddressActivity.other_tax.toString());
        jsonObject.addProperty("shipping_charge",ChooseAddressActivity.shipping_charge.toString());
        jsonObject.addProperty("subtotal",ChooseAddressActivity.subtotal.toString());
        jsonObject.addProperty("total_amount",ChooseAddressActivity.total_amount.toString());

        Log.e("my data",jsonObject.toString());

        Ion.with(context)
                .load("http://netforce.biz/renteeze/webservice/shop/checkout")
                  .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null)
                        {

                            System.out.println("array data ============"+ result.toString());

                            JsonObject jsonObject1 = result.getAsJsonObject("trasaction_details");

                             transaction_id = jsonObject1.get("transaction_id").getAsString();

                             order_id = jsonObject1.get("order_id").getAsString();

                             total_order_item = jsonObject1.get("total_order_item").getAsString();

                             total_payment = jsonObject1.get("total_payment").getAsString();

                             order_date = jsonObject1.get("order_date").getAsString();

                             delivery_date = jsonObject1.get("deliveri_date").getAsString();



                            JsonArray productListArray = jsonObject1.getAsJsonArray("orderList");

                            for (int i= 0; i<productListArray.size(); i++)
                            {

                                JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                String product_id = jsonObject.get("product_id").getAsString();

                                String product_name = jsonObject.get("product_name").getAsString();

                                //JsonArray order_item = productListArray.;
                                order_name.add(product_name);

                            }

                            baseActivity.dismissProgressBar();


                            Intent i = new Intent(context, TransactionDetailActivity.class);
                            i.putExtra("transaction_id",transaction_id);
                            i.putExtra("order_id",order_id);
                            i.putExtra("total_order_item",total_order_item);
                            i.putExtra("total_payment",total_payment);
                            i.putExtra("order_date",order_date);
                            i.putExtra("delivery_date",delivery_date);
                            context.startActivity(i);


                        }

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

