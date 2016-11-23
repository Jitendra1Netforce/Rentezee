package com.rentezee.fragments.rent_it_out.productview;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.fragments.myorder.PastOrder.PastOrderAdapter;
import com.rentezee.fragments.myorder.PastOrder.PastOrderData;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

import java.util.ArrayList;


public class ProductViewFragment extends Fragment implements  View.OnClickListener
{

    RecyclerView recyclerviewPastOrder;
    Context context;
    ArrayList<ProductViewData> productViewDatas = new ArrayList<>();
    ProductViewAdatpter productViewAdatpter;
    BaseActivity baseActivity;
    String user_id;
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

        recyclerviewPastOrder=(RecyclerView) view.findViewById(R.id.lvProducts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewPastOrder.setLayoutManager(mLayoutManager);

        recyclerviewPastOrder.setNestedScrollingEnabled(false);

        user = (User) new AppPreferenceManager(getActivity()).getObject(PreferenceKeys.savedUser, User.class);

        if(user != null){

            userId = user.getUserId();

            user_id = Long.toString(userId);

            fetchData(true);

        }


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
        productViewDatas.clear();

        baseActivity.showProgressBar(context);
        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/products/product_list?user_id="+user_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null)
                        {

                            System.out.println("data=====" + result.toString());

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====" + result.toString());

                            if(productListArray.size() == 0)
                            {

                                baseActivity.dismissProgressBar();

                            }
                            else
                            {
                                for (int i = 0; i < productListArray.size(); i++) {
                                    JsonObject jsonObject = (JsonObject) productListArray.get(i);
                                    JsonObject product = jsonObject.getAsJsonObject("Product");
                                    String id = product.get("id").getAsString();
                                    String name = product.get("name").getAsString();
                                    String price = product.get("price").getAsString();
                                    String security_price = product.get("security_price").getAsString();
                                    String description = product.get("description").getAsString();
                                    String image = "http://netforce.biz/renteeze/webservice/files/products/" + product.get("images").getAsString();
                                    productViewDatas.add(new ProductViewData(id, name, image, description, security_price, price));

                                }
                                productViewAdatpter = new ProductViewAdatpter(context, productViewDatas);
                                recyclerviewPastOrder.setAdapter(productViewAdatpter);
                                productViewAdatpter.notifyDataSetChanged();
                                recyclerviewPastOrder.setVisibility(View.VISIBLE);
                                baseActivity.dismissProgressBar();
                            }


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
    public void setMenuVisibility(final boolean visible)
    {
        if (visible) {
            //Do your stuff here
            context = getActivity();
        }

        super.setMenuVisibility(visible);
    }


    @Override
    public void onClick(View view)
    {

    }


}