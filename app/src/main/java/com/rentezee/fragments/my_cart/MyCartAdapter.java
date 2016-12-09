package com.rentezee.fragments.my_cart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import java.text.DateFormat;
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
public class MyCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values = new ArrayList<>();
    public static ArrayList<MyCartData> itemList;
    public  ArrayList<MyCartData> new_mycart_data = new ArrayList<>();
    private Context context;
    MyCartHolder myCartHolder;
    MyCart myCart;
    DashboardContainer dashboardContainer;
    public static Boolean txt_from_date = true;
    TextView textViewFrom, textViewTo, textViewDuration, tvtotal, tvRentPrice, tvSecurytiFee;
    boolean fromFlag = false, toFlag = false;
    double payable_amount = 0, all_text_charge, total_payable_amount;

   public static   int myposition;


    public MyCartAdapter(Context context, ArrayList<MyCartData> itemList, MyCart myCart)
    {
        this.myCart = myCart;
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

        MyCartAdapter.this.textViewFrom = myCartHolder.textview_from_date;

        MyCartAdapter.this.textViewTo = myCartHolder.textview_to_date;

        MyCartAdapter.this.textViewDuration = myCartHolder.tv_rent_duration;

        MyCartAdapter.this.tvtotal = myCartHolder.tvtotal;

        MyCartAdapter.this.tvRentPrice = myCartHolder.tvRentPrice;

        MyCartAdapter.this.tvSecurytiFee = myCartHolder.tvSecurytiFee;

        String current_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String nextday = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(current_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);  // number of days to add
        nextday = sdf.format(c.getTime());  // dt is now the new date
        this.textViewFrom.setText(current_date);

        this.textViewTo.setText(nextday);


        myCartHolder.tvtotal.setText(String.valueOf(itemList.get(parent.getChildCount()).total_per_item));

        payable_amount = payable_amount + Double.valueOf(itemList.get(parent.getChildCount()).total_per_item);

        myCart.tv_total.setText(String.valueOf(payable_amount));

        System.out.println("arvind data============" + parent.getChildCount());


        if (itemList.size() == parent.getChildCount() + 1)
        {

            System.out.println("this is pisitiion==========" + parent.getChildCount() + 1 + "itemList.size()=======" + itemList.size());

            myCart.tvOthertax.setText(String.valueOf(Double.valueOf(myCart.tv_total.getText().toString()) * 2 / 100));

            myCart.tvService_tax.setText(String.valueOf(Double.valueOf(myCart.tv_total.getText().toString()) * 12 / 100));

            all_text_charge = Double.valueOf(myCart.tv_total.getText().toString()) * 12 / 100 + Double.valueOf(myCart.tv_total.getText().toString()) * 2 / 100 + Double.valueOf(myCart.tvSerciceCharge.getText().toString());

            System.out.println("all_txt_===============" + all_text_charge);

            total_payable_amount = Double.valueOf(myCart.tv_total.getText().toString()) + all_text_charge;

            myCart.tv_total.setText(String.valueOf(total_payable_amount));

        }


        return myCartHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MyCartHolder homeHolder = (MyCartHolder) holder;

        if (itemList.get(position).security_price.toString().equals("0")) {

            homeHolder.tvRentalHeading.setText("Discription");

            homeHolder.tvsecurityheading.setText(itemList.get(position).category_name);

            homeHolder.tvRentPrice.setVisibility(View.INVISIBLE);
            homeHolder.tvSecurytiFee.setVisibility(View.INVISIBLE);

            homeHolder.tvrentDurationHeading.setVisibility(View.INVISIBLE);
            homeHolder.tv_rent_duration.setVisibility(View.INVISIBLE);

            homeHolder.textview_from_date.setVisibility(View.INVISIBLE);
            homeHolder.textview_to_date.setVisibility(View.INVISIBLE);

            homeHolder.tvTotalSecurity.setText("ITEM TOTAL");

            homeHolder.tv_to.setVisibility(View.INVISIBLE);

            homeHolder.tvItemTotal.setVisibility(View.INVISIBLE);

            Glide.with(context)
                    .load(itemList.get(position).image_url)
                    .centerCrop()
                    //.placeholder(R.mipmap.ic_loading)
                    .crossFade()
                    .into(homeHolder.imProductImage);




        } else {

            homeHolder.tvRentalHeading.setText("Rental:");
            homeHolder.tvSecurytiFee.setText("Security Fee:");

            homeHolder.tvRentPrice.setVisibility(View.VISIBLE);
            homeHolder.tvSecurytiFee.setVisibility(View.VISIBLE);

            homeHolder.tvrentDurationHeading.setVisibility(View.VISIBLE);
            homeHolder.tv_rent_duration.setVisibility(View.VISIBLE);

            homeHolder.textview_from_date.setVisibility(View.VISIBLE);
            homeHolder.textview_to_date.setVisibility(View.VISIBLE);
            homeHolder.tv_to.setVisibility(View.VISIBLE);
            homeHolder.tvItemTotal.setVisibility(View.VISIBLE);


            homeHolder.tvProductName.setText(itemList.get(position).product_name);
            homeHolder.tvRentPrice.setText(itemList.get(position).rental_price);
            homeHolder.tvSecurytiFee.setText(itemList.get(position).security_price);
            homeHolder.tvCategoriesName.setText(itemList.get(position).category_name);

            homeHolder.tvTotalSecurity.setText("RENTAL + SECURITY");

            // homeHolder.tvTotal.setText(Integer.parseInt(homeHolder.tvRentPrice.getText().toString())+Integer.parseInt(homeHolder.tvSecurytiFee.getText().toString()));
            Glide.with(context)
                    .load(itemList.get(position).image_url)
                    .centerCrop()
                    //.placeholder(R.mipmap.ic_loading)
                    .crossFade()
                    .into(homeHolder.imProductImage);

           /* String current_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

            int total_amount = Integer.valueOf(homeHolder.tvRentPrice.getText().toString())*1+Integer.valueOf(homeHolder.tvSecurytiFee.getText().toString());

            System.out.println("total_amount=============" + total_amount);

             payable_amount = payable_amount +total_amount;

            myCart.tv_total.setText(String.valueOf(payable_amount));*/


        }


        homeHolder.layoutRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* *//*  String cart_id = itemList.get(position).my_cart_id.toString();
                add_to_cart(cart_id);*//*
                itemList.remove(position);
                notifyItemRemoved(position);*/
                String cart_id = itemList.get(position).my_cart_id.toString();
                delete_to_cart(cart_id);

                double pre_tvtotal = Double.valueOf(itemList.get(position).total_per_item);
                payable_amount = payable_amount - pre_tvtotal;

                myCart.tv_total.setText(String.valueOf(payable_amount));

            }
        });

        homeHolder.textview_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myposition = position;
                txt_from_date = true;

                MyCartAdapter.this.textViewDuration = homeHolder.tv_rent_duration;

                MyCartAdapter.this.textViewTo = homeHolder.textview_to_date;

                MyCartAdapter.this.textViewFrom = homeHolder.textview_from_date;

                MyCartAdapter.this.tvtotal = homeHolder.tvtotal;

                MyCartAdapter.this.tvRentPrice = homeHolder.tvRentPrice;

                MyCartAdapter.this.tvSecurytiFee = homeHolder.tvSecurytiFee;

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);

                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MyCartAdapter.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setMinDate(calendar);
                dpd.show(((AppCompatActivity) context).getFragmentManager(), "Datepickerdialog");





            }
        });

        homeHolder.textview_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                myposition = position;
                MyCartAdapter.this.textViewTo = homeHolder.textview_to_date;

                MyCartAdapter.this.textViewFrom = homeHolder.textview_from_date;

                MyCartAdapter.this.textViewDuration = homeHolder.tv_rent_duration;

                MyCartAdapter.this.tvtotal = homeHolder.tvtotal;

                MyCartAdapter.this.tvRentPrice = homeHolder.tvRentPrice;

                MyCartAdapter.this.tvSecurytiFee = homeHolder.tvSecurytiFee;

                txt_from_date = false;

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);

                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MyCartAdapter.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(calendar);
                dpd.show(((AppCompatActivity) context).getFragmentManager(), "Datepickerdialog");

                //new_mycart_data.add()

                System.out.println("from_date========="+position);



              //  myCart.myCartDatas.set(position,new MyCartData(itemList.get(position).my_cart_id,itemList.get(position).product_id, itemList.get(position).product_name, itemList.get(position).image_url, itemList.get(position).rental_price, itemList.get(position).security_price,itemList.get(position).category_name,MyCartAdapter.this.textViewDuration.getText().toString(),MyCartAdapter.this.textViewFrom.getText().toString(),MyCartAdapter.this.textViewTo.getText().toString(),MyCartAdapter.this.tvtotal.getText().toString()));




            }
        });


    }

    private void showMessage(String s) {

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


    private void delete_to_cart(final String cart_id) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", cart_id);

        Ion.with(context)
                .load("http://netforce.biz/renteeze/webservice/Products/delete_cart")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            myCart.fetchData(true);
                            dashboardContainer.count_cart();
                            notifyDataSetChanged();

                        }

                    }

                });

    }


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {


        if (txt_from_date)
        {
            //            fromFlag = true;
            this.textViewFrom.setText(dayOfMonth + "/" + String.valueOf(monthOfYear + 1) + "/" + year);

            if(validateDate(this.textViewFrom.getText().toString(),this.textViewTo.getText().toString()))
            {

                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Date date1 = myFormat.parse(this.textViewFrom.getText().toString());
                    if (true) {
                        Date date2 = myFormat.parse(MyCartAdapter.this.textViewTo.getText().toString());
                        long diff = date2.getTime() - date1.getTime();

                        System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                        String day = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                        double pre_tvtotal = Double.valueOf(this.tvtotal.getText().toString());

                        this.textViewDuration.setText(day + "days");

                        double total_amount = Double.valueOf(this.tvRentPrice.getText().toString()) * Double.valueOf(day) + Double.valueOf(this.tvSecurytiFee.getText().toString());

                        System.out.println("total_amount=============" + total_amount);

                        this.tvtotal.setText(String.valueOf(total_amount));

                        System.out.println("my data ============" + this.tvtotal.getText().toString());

                        notifyDataSetChanged();

                        payable_amount = payable_amount + total_amount - pre_tvtotal;

                        myCart.tv_total.setText(String.valueOf(payable_amount));

                  /*  int new_total = Integer.valueOf(myCart.tv_total.getText().toString()) - pre_tvtotal+ total_amount;
                     myCart.tv_total.setText(String.valueOf(new_total));
                  */

                        all_text_charge = Double.valueOf(myCart.tv_total.getText().toString()) * 12 / 100 + Double.valueOf(myCart.tv_total.getText().toString()) * 2 / 100 + Double.valueOf(myCart.tvSerciceCharge.getText().toString());


                        myCart.tvOthertax.setText(String.valueOf(Double.valueOf(myCart.tv_total.getText().toString()) * 2 / 100));

                        myCart.tvService_tax.setText(String.valueOf(Double.valueOf(myCart.tv_total.getText().toString()) * 12 / 100));

                        System.out.println("all_txt_===============" + all_text_charge);

                        total_payable_amount = Double.valueOf(myCart.tv_total.getText().toString()) + all_text_charge;

                        myCart.tv_total.setText(String.valueOf(total_payable_amount));


                        MyCart.myCartDatas.set(myposition,new MyCartData(itemList.get(myposition).my_cart_id,itemList.get(myposition).product_id, itemList.get(myposition).product_name, itemList.get(myposition).image_url, itemList.get(myposition).rental_price, itemList.get(myposition).security_price,itemList.get(myposition).category_name,MyCartAdapter.this.textViewDuration.getText().toString(),MyCartAdapter.this.textViewFrom.getText().toString(),MyCartAdapter.this.textViewTo.getText().toString(),MyCartAdapter.this.tvtotal.getText().toString()));

                        notifyDataSetChanged();
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            notifyDataSetChanged();


        }
        else
        {
            //toFlag = true;
            this.textViewTo.setText(dayOfMonth + "/" + String.valueOf(monthOfYear + 1) + "/" + year);

            if(validateDate(this.textViewFrom.getText().toString(),this.textViewTo.getText().toString())) {

                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Date date2 = myFormat.parse(this.textViewTo.getText().toString());
                    if (true) {
                        Date date1 = myFormat.parse(this.textViewFrom.getText().toString());
                        long diff = date2.getTime() - date1.getTime();

                        System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                        String day = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                        this.textViewDuration.setText(day + "days");

                        double pre_tvtotal = Double.valueOf(this.tvtotal.getText().toString());

                        double total_amount = Double.valueOf(this.tvRentPrice.getText().toString()) * Double.valueOf(day) + Double.valueOf(this.tvSecurytiFee.getText().toString());

                        System.out.println("total_amount=============" + total_amount);

                        this.tvtotal.setText(String.valueOf(total_amount));

                        System.out.println("my data ============" + this.tvtotal.getText().toString());

                        payable_amount = payable_amount + total_amount - pre_tvtotal;

                        myCart.tv_total.setText(String.valueOf(payable_amount));

                        all_text_charge = Double.valueOf(myCart.tv_total.getText().toString()) * 12 / 100 + Double.valueOf(myCart.tv_total.getText().toString()) * 2 / 100 + Double.valueOf(myCart.tvSerciceCharge.getText().toString());

                        myCart.tvOthertax.setText(String.valueOf(Double.valueOf(myCart.tv_total.getText().toString()) * 2 / 100));

                        myCart.tvService_tax.setText(String.valueOf(Double.valueOf(myCart.tv_total.getText().toString()) * 12 / 100));


                        System.out.println("all_txt_===============" + all_text_charge);

                        total_payable_amount = Double.valueOf(myCart.tv_total.getText().toString()) + all_text_charge;

                        myCart.tv_total.setText(String.valueOf(total_payable_amount));


                        Log.i("mycart",myCart.myCartDatas.get(0).to_date.toString());
                        MyCart.myCartDatas.set(myposition,new MyCartData(itemList.get(myposition).my_cart_id,itemList.get(myposition).product_id, itemList.get(myposition).product_name, itemList.get(myposition).image_url, itemList.get(myposition).rental_price, itemList.get(myposition).security_price,itemList.get(myposition).category_name,this.textViewDuration.getText().toString(),MyCartAdapter.this.textViewFrom.getText().toString(),MyCartAdapter.this.textViewTo.getText().toString(),MyCartAdapter.this.tvtotal.getText().toString()));

                        // MyCart.myCartDatas.get(myposition).to_date=MyCartAdapter.this.textViewTo.getText().toString();
                        notifyDataSetChanged();

                        Log.i("mycart",myCart.myCartDatas.get(0).to_date.toString());
                        System.out.println("to date--------"+ itemList.get(myposition).to_date.toString());



                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            notifyDataSetChanged();
        }


    }




    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
        //month_txt.setText(time);
    }

    private boolean validateDate(String date1 , String date2)
    {
        Date etd, eta, returnetd, returneta;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.i("datecheck", date1 + ":" + date2);
        try {
            etd = dateFormat.parse(date1);
        } catch (ParseException e) {
            showMessage("Departure date not set");
            return false;
        }
        try {
            eta = dateFormat.parse(date2);
        } catch (ParseException e) {
            showMessage("Arival date not set");
            return false;
        }
        Log.i("datecheck", etd.toString() + ":" + eta.toString());
        if (etd.after(eta)) {

            showMessage("From date should be after To date");
            return false;
        }


      /*  if (returnFlag) {
            String date3 = textViewReturnETD.getText().toString();
            String date4 = textViewReturnETA.getText().toString();
            try {
                returnetd = dateFormat.parse(date3);
            } catch (ParseException e) {
                showMessage("Departure date not set");
                return false;
            }
            try {
                returneta = dateFormat.parse(date4);
            } catch (ParseException e) {
                showMessage("Arival date not set");
                return false;
            }
            Log.i("datecheck", returnetd.toString() + ":" + returneta.toString());
            if (returnetd.after(returneta)) {

                showMessage("Arival time should be after Departure time");
                return false;
            }
            if (etd.after(returnetd)) {
                showMessage("Return time should be after out trip time");
                return false;
            }

        }*/
        return true;


    }


}