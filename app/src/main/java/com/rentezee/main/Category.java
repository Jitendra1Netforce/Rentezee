package com.rentezee.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rentezee.adapters.ProductsAdapter;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Debugger;
import com.rentezee.helpers.JToast;
import com.rentezee.helpers.Util;
import com.rentezee.helpers.VolleyErrorHandler;
import com.rentezee.helpers.VolleyGsonRequest;
import com.rentezee.pojos.mcategory.Product;
import com.rentezee.pojos.mdashboard.CategoryData;

import org.json.JSONObject;

import java.util.ArrayList;

public class Category extends BaseActivity implements View.OnClickListener {

    private static  final String TAG=Category.class.getSimpleName();
    private Context context;
    private CoordinatorLayout coordinatorLayout;

    private ArrayList<CategoryData> fetchedCategoryDataList;

    private ArrayList<Product> productList=new ArrayList<>();
    private ProductsAdapter productsAdapter;
    private int categoryId, page, sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        ListView lvProducts=(ListView)findViewById(R.id.lvProducts);
        productsAdapter=new ProductsAdapter(context, productList);
        lvProducts.setAdapter(productsAdapter);

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, Detail.class);
                intent.putExtra(Constants.PRODUCT_ID, productList.get(position).getProductId());
                gotoActivity(intent);
            }
        });


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        findViewById(R.id.layoutSort).setOnClickListener(this);

        Intent intent=getIntent();
        categoryId=intent.getIntExtra(Constants.CATEGORY_ID, 0);
        fetchedCategoryDataList = (ArrayList<CategoryData>) intent.getSerializableExtra(Constants.CATEGORIES);
        int selectedTabPosition=intent.getIntExtra(Constants.SELECTED_TAB_POSITION, 0);
        setUpTabs(tabLayout, selectedTabPosition);

        if(categoryId==0){
            showSnackBar(coordinatorLayout, "Not able to find the category");
            return;
        }
        fetchData(true);
    }

    private void setUpTabs(final TabLayout tabLayout, final int selectedTabPosition){
        if(fetchedCategoryDataList !=null) {
            for (CategoryData s : fetchedCategoryDataList) {
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setText(s.getCategoryName());
                tabLayout.addTab(tab);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tabLayout.getTabAt(selectedTabPosition).select();
                }
            }, 300);


            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    CategoryData categoryData = fetchedCategoryDataList.get(tab.getPosition());
                    categoryId= categoryData.getCategoryId();
                    fetchData(true);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


        }

    }

    private void showSortPopup(){
        ArrayList<String> options=new ArrayList<>();
        options.add("Relevance");
        options.add("Price low to high");
        options.add("Price high to low");
        options.add("Popularity");
        options.add("Latest Additions");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.sort_popup, null);
        alertDialog.setView(convertView);
        ListView lv = (ListView) convertView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options);
        lv.setAdapter(adapter);
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutSort:
                showSortPopup();
                break;
        }
    }

    private void reset(){
        page=1;
        sortBy=0;
        productList.clear();
        productsAdapter.notifyDataSetChanged();
    }

    private void fetchData(final boolean reset){
        if(reset){
            reset();
        }

        //Post data to sever
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appTypeId", Constants.APP_TYPE_ID);
            jsonObject.put("versionName", Util.getVersionName(context));
            jsonObject.put("categoryId", categoryId);
            jsonObject.put("page", page);
            jsonObject.put("sortBy", sortBy);
            String url = Constants.API + "product/all"; //URL to hit
            showProgressBar(context);
            AppController.getInstance().cancelPendingRequest(TAG);
            VolleyGsonRequest<com.rentezee.pojos.mcategory.Response> gsonRequest = new VolleyGsonRequest<>(url,
                    jsonObject,
                    new Response.Listener<com.rentezee.pojos.mcategory.Response>() {
                        @Override
                        public void onResponse(com.rentezee.pojos.mcategory.Response response) {
                            dismissProgressBar();
                            Debugger.i(TAG, "Response " + response);
                            if (response != null) {
                                if (response.isSuccess()) {
                                    processCategoryResponse(response);
                                } else {
                                    showSnackBar(coordinatorLayout, response.getMessage());
                                }
                            } else {
                                showSnackBar(coordinatorLayout, getString(R.string.generic_error));
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgressBar();
                            error.printStackTrace();
                            showSnackBar(coordinatorLayout, VolleyErrorHandler.getMessage(context, error));
                        }
                    },
                    com.rentezee.pojos.mcategory.Response.class,
                    null
            );
            AppController.getInstance().addToRequestQueue(gsonRequest, TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processCategoryResponse(com.rentezee.pojos.mcategory.Response response){
        ArrayList<Product> list=response.getData().getProducts();
        if(list!=null){
            productList.addAll(list);
            productsAdapter.notifyDataSetChanged();
        }
    }
}
