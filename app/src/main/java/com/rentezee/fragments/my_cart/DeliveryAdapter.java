package com.rentezee.fragments.my_cart;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by John on 12/7/2016.
 */

public class DeliveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values = new ArrayList<>();
    private List<MyCartData> itemList;
    private Context context;
    MyCartHolder myCartHolder;
    DeliveryActivity mydelivery;
    DashboardContainer dashboardContainer;
    public static Boolean txt_from_date = true;
    TextView textViewFrom, textViewTo, textViewDuration, tvtotal, tvRentPrice, tvSecurytiFee;
    boolean fromFlag = false, toFlag = false;
    double payable_amount = 0, all_text_charge, total_payable_amount;


    public DeliveryAdapter(Context context, List<MyCartData> itemList, DeliveryActivity myCart)
    {
        this.mydelivery = myCart;
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        dashboardContainer = new DashboardContainer();


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_delivery, parent, false);


        myCartHolder = new MyCartHolder(view);

        DeliveryAdapter.this.textViewFrom = myCartHolder.textview_from_date;

        DeliveryAdapter.this.textViewTo = myCartHolder.textview_to_date;

        DeliveryAdapter.this.textViewDuration = myCartHolder.tv_rent_duration;

        DeliveryAdapter.this.tvtotal = myCartHolder.tvtotal;

        DeliveryAdapter.this.tvRentPrice = myCartHolder.tvRentPrice;

        DeliveryAdapter.this.tvSecurytiFee = myCartHolder.tvSecurytiFee;

        System.out.println("itemList----------"+itemList.get(parent.getChildCount()).to_date);

        this.textViewFrom.setText(itemList.get(parent.getChildCount()).from_date);

        this.textViewTo.setText(itemList.get(parent.getChildCount()).to_date);

        this.textViewDuration.setText(itemList.get(parent.getChildCount()).rent_duration);


        if (itemList.size() == parent.getChildCount() + 1)
        {

            System.out.println("this is pisitiion==========" + parent.getChildCount() + 1 + "itemList.size()=======" + itemList.size());

            mydelivery.tvOthertax.setText(String.valueOf(Double.valueOf(mydelivery.tv_total.getText().toString()) * 2 / 100));

            mydelivery.tvService_tax.setText(String.valueOf(Double.valueOf(mydelivery.tv_total.getText().toString()) * 12 / 100));

            all_text_charge = Double.valueOf(mydelivery.tv_total.getText().toString()) * 12 / 100 + Double.valueOf(mydelivery.tv_total.getText().toString()) * 2 / 100 + Double.valueOf(mydelivery.tvSerciceCharge.getText().toString());

            System.out.println("all_txt_===============" + all_text_charge);

            total_payable_amount = Double.valueOf(mydelivery.tv_total.getText().toString()) + all_text_charge;

            mydelivery.tv_total.setText(String.valueOf(total_payable_amount));

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

        }
        else
        {



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


            myCartHolder.tvtotal.setText(String.valueOf(itemList.get(position).total_per_item));

            payable_amount = payable_amount + Double.valueOf(itemList.get(position).total_per_item);

            mydelivery.tv_total.setText(String.valueOf(payable_amount));

           /* String current_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

            int total_amount = Integer.valueOf(homeHolder.tvRentPrice.getText().toString())*1+Integer.valueOf(homeHolder.tvSecurytiFee.getText().toString());

            System.out.println("total_amount=============" + total_amount);

             payable_amount = payable_amount +total_amount;

            myCart.tv_total.setText(String.valueOf(payable_amount));*/


        }




        homeHolder.textview_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_from_date = true;

                DeliveryAdapter.this.textViewDuration = homeHolder.tv_rent_duration;

                DeliveryAdapter.this.textViewTo = homeHolder.textview_to_date;

                DeliveryAdapter.this.textViewFrom = homeHolder.textview_from_date;

                DeliveryAdapter.this.tvtotal = homeHolder.tvtotal;

                DeliveryAdapter.this.tvRentPrice = homeHolder.tvRentPrice;

                DeliveryAdapter.this.tvSecurytiFee = homeHolder.tvSecurytiFee;

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);

                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        DeliveryAdapter.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setMinDate(calendar);
                dpd.show(((AppCompatActivity) context).getFragmentManager(), "Datepickerdialog");

                // myCart.pick_date();
                itemList.set(position,new MyCartData(itemList.get(position).my_cart_id,itemList.get(position).product_id, itemList.get(position).product_name, itemList.get(position).image_url, itemList.get(position).rental_price, itemList.get(position).security_price,itemList.get(position).category_name,DeliveryAdapter.this.textViewDuration.getText().toString(),DeliveryAdapter.this.textViewFrom.getText().toString(),DeliveryAdapter.this.textViewTo.getText().toString(),DeliveryAdapter.this.tvtotal.getText().toString()));

                notifyDataSetChanged();


            }
        });

        homeHolder.textview_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeliveryAdapter.this.textViewTo = homeHolder.textview_to_date;

                DeliveryAdapter.this.textViewFrom = homeHolder.textview_from_date;

                DeliveryAdapter.this.textViewDuration = homeHolder.tv_rent_duration;

                DeliveryAdapter.this.tvtotal = homeHolder.tvtotal;

                DeliveryAdapter.this.tvRentPrice = homeHolder.tvRentPrice;

                DeliveryAdapter.this.tvSecurytiFee = homeHolder.tvSecurytiFee;

                txt_from_date = false;

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);

                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        DeliveryAdapter.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(calendar);
                dpd.show(((AppCompatActivity) context).getFragmentManager(), "Datepickerdialog");

                //new_mycart_data.add()

                itemList.set(position,new MyCartData(itemList.get(position).my_cart_id,itemList.get(position).product_id, itemList.get(position).product_name, itemList.get(position).image_url, itemList.get(position).rental_price, itemList.get(position).security_price,itemList.get(position).category_name,DeliveryAdapter.this.textViewDuration.getText().toString(),DeliveryAdapter.this.textViewFrom.getText().toString(),DeliveryAdapter.this.textViewTo.getText().toString(),DeliveryAdapter.this.tvtotal.getText().toString()));

                notifyDataSetChanged();



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
                        Date date2 = myFormat.parse(DeliveryAdapter.this.textViewTo.getText().toString());
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

                        mydelivery.tv_total.setText(String.valueOf(payable_amount));

                  /*  int new_total = Integer.valueOf(myCart.tv_total.getText().toString()) - pre_tvtotal+ total_amount;
                     myCart.tv_total.setText(String.valueOf(new_total));
                  */

                        all_text_charge = Double.valueOf(mydelivery.tv_total.getText().toString()) * 12 / 100 + Double.valueOf(mydelivery.tv_total.getText().toString()) * 2 / 100 + Double.valueOf(mydelivery.tvSerciceCharge.getText().toString());


                        mydelivery.tvOthertax.setText(String.valueOf(Double.valueOf(mydelivery.tv_total.getText().toString()) * 2 / 100));

                        mydelivery.tvService_tax.setText(String.valueOf(Double.valueOf(mydelivery.tv_total.getText().toString()) * 12 / 100));

                        System.out.println("all_txt_===============" + all_text_charge);

                        total_payable_amount = Double.valueOf(mydelivery.tv_total.getText().toString()) + all_text_charge;

                        mydelivery.tv_total.setText(String.valueOf(total_payable_amount));




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

                        mydelivery.tv_total.setText(String.valueOf(payable_amount));

                        all_text_charge = Double.valueOf(mydelivery.tv_total.getText().toString()) * 12 / 100 + Double.valueOf(mydelivery.tv_total.getText().toString()) * 2 / 100 + Double.valueOf(mydelivery.tvSerciceCharge.getText().toString());

                        mydelivery.tvOthertax.setText(String.valueOf(Double.valueOf(mydelivery.tv_total.getText().toString()) * 2 / 100));

                        mydelivery.tvService_tax.setText(String.valueOf(Double.valueOf(mydelivery.tv_total.getText().toString()) * 12 / 100));


                        System.out.println("all_txt_===============" + all_text_charge);

                        total_payable_amount = Double.valueOf(mydelivery.tv_total.getText().toString()) + all_text_charge;

                        mydelivery.tv_total.setText(String.valueOf(total_payable_amount));

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