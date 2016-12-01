package com.rentezee.fragments.my_cart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.VolleyErrorHandler;
import com.rentezee.helpers.VolleyGsonRequest;
import com.rentezee.main.AppController;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.Detail;
import com.rentezee.main.R;
import com.rentezee.pojos.GenericResponse;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by John on 11/10/2016.
 */
public class MyCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener
{
    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values=new ArrayList<>();
    private List<MyCartData> itemList;
    private Context context;
    MyCartHolder myCartHolder;
    MyCart myCart;
    DashboardContainer dashboardContainer;
    public  static Boolean txt_from_date = false;
    public  static Boolean txt_to_date = false;


    public MyCartAdapter(Context context, List<MyCartData> itemList, MyCart myCart)
    {
        this.myCart=myCart;
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        dashboardContainer = new DashboardContainer();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_my_cart, parent, false);
        myCartHolder = new MyCartHolder(view);

        return myCartHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        MyCartHolder homeHolder = (MyCartHolder) holder;

        homeHolder.tvProductName.setText(itemList.get(position).product_name);
        homeHolder.tvRentPrice.setText(itemList.get(position).rental_price+"per day");
        homeHolder.tvSecurytiFee.setText(itemList.get(position).security_price);
        homeHolder.tvCategoriesName.setText(itemList.get(position).category_name);

         // homeHolder.tvTotal.setText(Integer.parseInt(homeHolder.tvRentPrice.getText().toString())+Integer.parseInt(homeHolder.tvSecurytiFee.getText().toString()));

        Glide.with(context)
                .load(itemList.get(position).image_url)
                .centerCrop()
                        //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(homeHolder.imProductImage);

        String current_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        homeHolder.textview_from_date.setText(current_date);

        homeHolder.textview_to_date.setText(current_date);

        homeHolder.layoutRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                /* *//*  String cart_id = itemList.get(position).my_cart_id.toString();
                add_to_cart(cart_id);*//*
                itemList.remove(position);
                notifyItemRemoved(position);*/
                String cart_id = itemList.get(position).my_cart_id.toString();
                delete_to_cart(cart_id);



            }
        });

        homeHolder.textview_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                txt_from_date =true;
                txt_to_date= false;
                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MyCartAdapter.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(((AppCompatActivity)context).getFragmentManager(), "Datepickerdialog");

                // myCart.pick_date();

            }
        });


        homeHolder.textview_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                txt_to_date= true;
                txt_from_date=false;

                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MyCartAdapter.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(((AppCompatActivity)context).getFragmentManager(), "Datepickerdialog");
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

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", cart_id);

        Ion.with(context)
                .load("http://netforce.biz/renteeze/webservice/Products/delete_cart")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null) {

                            myCart.fetchData(true);
                            dashboardContainer.count_cart();

                        }

                    }

                });

    }



    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {

        if(txt_from_date)
        {

            MyCartHolder.textview_from_date.setText(dayOfMonth+"/"+String.valueOf(monthOfYear+1)+"/"+year);

            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date date1 = myFormat.parse(MyCartHolder.textview_from_date.getText().toString());
                Date date2 = myFormat.parse(MyCartHolder.textview_to_date.getText().toString());
                long diff = date2.getTime() - date1.getTime();

                System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                String day = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                MyCartHolder.tv_rent_duration.setText(day+"days");

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        if(txt_to_date)
        {

            MyCartHolder.textview_to_date.setText(dayOfMonth+"/"+String.valueOf(monthOfYear+1)+"/"+year);

            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date date1 = myFormat.parse(MyCartHolder.textview_from_date.getText().toString());
                Date date2 = myFormat.parse(MyCartHolder.textview_to_date.getText().toString());
                long diff = date2.getTime() - date1.getTime();

                System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                String day = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                MyCartHolder.tv_rent_duration.setText(day + "days");

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }



    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        //month_txt.setText(time);
    }



}