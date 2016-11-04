package com.rentezee.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.adapters.DashboardCategoriesAdapter;
import com.rentezee.adapters.ProductsAdapter;
import com.rentezee.adapters.TrendingAdapter;
import com.rentezee.adapters.ViewPagerAdapter;
import com.rentezee.fragments.DashboardSliderImage;
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

public class Category extends BaseActivity implements View.OnClickListener
{

     private static  final String TAG=Category.class.getSimpleName();
     private Context context;
     private CoordinatorLayout coordinatorLayout;
     private LinearLayout layoutBottom;
     ListView lvProducts;
     ArrayList<ProductListData> productListDatas = new ArrayList<>();
     ArrayList<CategoriesData> fetchedCategoryDataList;
     private ArrayList<Product> productList=new ArrayList<>();
     private ProductsAdapter productsAdapter;
     private int categoryId, page, sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        layoutBottom=(LinearLayout)findViewById(R.id.layoutBottom);
         lvProducts=(ListView)findViewById(R.id.lvProducts);


        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra(Constants.PRODUCT_ID, productListDatas.get(position).product_id);
                gotoActivity(intent);
            }
        });


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        findViewById(R.id.layoutSort).setOnClickListener(this);

        Intent intent=getIntent();

        String cat_id = intent.getStringExtra(Constants.CATEGORY_ID);

        categoryId = Integer.parseInt(cat_id);


        fetchedCategoryDataList = (ArrayList<CategoriesData>) intent.getSerializableExtra(Constants.CATEGORIES);
        int selectedTabPosition=intent.getIntExtra(Constants.SELECTED_TAB_POSITION, 0);

        System.out.println("selectedTabPosition============"+ selectedTabPosition);

        setUpTabs(tabLayout, selectedTabPosition);

       /* if(categoryId==0){
            showSnackBar(coordinatorLayout, "Not able to find the category");
            return;
        }*/

        if(selectedTabPosition == 0){
            fetchData(true);
        }



    }

    private void setUpTabs(final TabLayout tabLayout, final int selectedTabPosition){
        if(fetchedCategoryDataList !=null) {
            for (CategoriesData s : fetchedCategoryDataList) {
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setText(s.category_name);
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
                    CategoriesData categoryData = fetchedCategoryDataList.get(tab.getPosition());
                    String cat_data = categoryData.category_id;
                    int foo = Integer.parseInt(cat_data);
                    categoryId= foo;
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
        layoutBottom.setVisibility(View.GONE);
        page=1;
        sortBy=0;
        productListDatas.clear();
        // productsAdapter.notifyDataSetChanged();
    }

    private void fetchData(final boolean reset)
    {
        System.out.println("fetch data=====");
        if(reset){
            reset();
        }
       load_refresh();
       /* //Post data to sever
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
        }*/
    }


    private void processCategoryResponse(com.rentezee.pojos.mcategory.Response response)
    {
        ArrayList<Product> list=response.getData().getProducts();
        if(list!=null)
        {
            layoutBottom.setVisibility(View.VISIBLE);
            productList.addAll(list);
            productsAdapter.notifyDataSetChanged();
        }
    }



    private void load_refresh()
    {
        // recyclerView.setVisibility(View.GONE);

        // homeDatas.clear();

        showProgressBar(context);

        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/products/product_list?cat_id=1")

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null)
                        {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====");

                            for (int i = 0; i < productListArray.size(); i++)
                            {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);
                                JsonObject product = jsonObject.getAsJsonObject("Product");
                                String id = product.get("id").getAsString();
                                String name = product.get("name").getAsString();
                                String price = product.get("price").getAsString();
                                String special_price = product.get("special_price").getAsString();
                                String image = "http://netforce.biz/renteeze/webservice/files/products/"+product.get("images").getAsString();
                                productListDatas.add(new ProductListData(id, name, image,price,special_price));

                            }
                            productsAdapter=new ProductsAdapter(context, productListDatas);
                            lvProducts.setAdapter(productsAdapter);
                            productsAdapter.notifyDataSetChanged();

                            dismissProgressBar();
                            layoutBottom.setVisibility(View.VISIBLE);
                        }
                        else
                        {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });

    }




}
