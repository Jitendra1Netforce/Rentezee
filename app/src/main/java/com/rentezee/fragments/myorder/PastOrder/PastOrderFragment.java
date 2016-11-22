package com.rentezee.fragments.myorder.PastOrder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class PastOrderFragment extends Fragment implements  View.OnClickListener
{

    RecyclerView recyclerviewPastOrder;
    Context context;
    ArrayList<PastOrderData> pastOrderDatas = new ArrayList<>();
    PastOrderAdapter pastOrderAdapter;
    BaseActivity baseActivity;
    String id,user_id;
    long  userId;
    User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_order, container, false);

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



        recyclerviewPastOrder=(RecyclerView) view.findViewById(R.id.lvProducts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewPastOrder.setLayoutManager(mLayoutManager);

        recyclerviewPastOrder.setNestedScrollingEnabled(false);
        fetchData(true);

        return view;
    }



    private void fetchData(boolean reset)
    {
        if(reset)
        {
            reset();
        }
    }


    private void reset()
    {
        recyclerviewPastOrder.setVisibility(View.INVISIBLE);
        pastOrderDatas.clear();

        baseActivity.showProgressBar(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id","2");

        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/Orders/orderlist")
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

                            for (int i = 0; i < productListArray.size(); i++) {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);

                                String id = jsonObject.get("product_id").getAsString();
                                String name = jsonObject.get("order_date").getAsString();
                                String price = jsonObject.get("total_amount").getAsString();
                                //String special_price = product.get("special_price").getAsString();

                                pastOrderDatas.add(new PastOrderData(id, name, price, price, price));

                            }
                            pastOrderAdapter = new PastOrderAdapter(context, pastOrderDatas);
                            recyclerviewPastOrder.setAdapter(pastOrderAdapter);
                            pastOrderAdapter.notifyDataSetChanged();
                            recyclerviewPastOrder.setVisibility(View.VISIBLE);
                            baseActivity.dismissProgressBar();


                        } else
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