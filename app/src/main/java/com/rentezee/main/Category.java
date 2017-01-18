package com.rentezee.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.rentezee.adapters.ProductAdapterNew;

import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Constants;

import com.rentezee.pojos.mcategory.Product;

import java.util.ArrayList;

public class Category extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener
{

     private static  final String TAG=Category.class.getSimpleName();
     private Context context;
     private CoordinatorLayout coordinatorLayout;
     private LinearLayout layoutBottom;
     RecyclerView lvProducts;
     ArrayList<ProductListData> productListDatas = new ArrayList<>();
     ArrayList<CategoriesData> fetchedCategoryDataList;
     private ArrayList<Product> productList=new ArrayList<>();
     ProductAdapterNew productsAdapter;
     private int categoryId, page, sortBy;
     DashboardContainer dashboardContainer;
     AlertDialog.Builder alertDialog;

     SwipeRefreshLayout mSwipyRefreshLayout;
     int product_count = 1;
     Boolean loadingMore= false;
     LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        layoutBottom = (LinearLayout) findViewById(R.id.layoutBottom);

        lvProducts = (RecyclerView) findViewById(R.id.lvProducts);

        mSwipyRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        // mSwipyRefreshLayout.setRefreshing(true);

        mSwipyRefreshLayout.setOnRefreshListener(this);


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        productsAdapter = new ProductAdapterNew(context, productListDatas);
        lvProducts.setAdapter(productsAdapter);
        lvProducts.setLayoutManager(mLayoutManager);


        lvProducts.setOnScrollListener(new RecyclerView.OnScrollListener()
        {

            public void onScrollStateChanged(RecyclerView view, int scrollState)
            {

                super.onScrollStateChanged(lvProducts, scrollState);

            }



            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = mLayoutManager.getItemCount();

                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                int lastVisibleItemCount = mLayoutManager.findLastVisibleItemPosition();

                if (totalItemCount > 0)
                {
                    if ((totalItemCount - 1) == lastVisibleItemCount)
                    {

                        product_count=   product_count + 1;

                        Log.e("product_count==",product_count+"");

                         load_new_data(product_count);

                        System.out.println("count===="+product_count);
                    }
                    else
                    {
                        //loadingProgress.setVisibility(View.GONE);
                    }

                }

            }


        });


       /* lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra(Constants.PRODUCT_ID, productListDatas.get(position).product_id);
                gotoActivity(intent);
            }
        });*/
   /*

        lvProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {

                 int lastItem = firstVisibleItem + visibleItemCount;

                System.out.println("totalitem===="+totalItemCount+"firstVisibleItem--------"+ firstVisibleItem+ "visibleItemCount==========="+visibleItemCount);

                if (totalItemCount > 0)
                {
                    if (lastItem == totalItemCount)
                    {
                        //loadMore();//your HTTP stuff goes in this method
                        //loadingProgress.setVisibility(View.VISIBLE);

                        System.out.println("lastItem===="+lastItem);

                        product_count=   product_count + 1;

                        Log.e("product_count==",product_count+"");


                       // load_new_data(product_count);

                        System.out.println("count===="+product_count);

                    }
                    else
                    {
                        //loadingProgress.setVisibility(View.GONE);
                    }

                }

            }
        });
*/

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


        if(selectedTabPosition == 0)
        {
            fetchData(true);
        }



    }

    private void load_new_data(int limit) {


        loadingMore =true;
        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/products/product_list?cat_id="+categoryId+"&limit="+limit)

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====" + result);

                            for (int i = 0; i < productListArray.size(); i++) {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);
                                JsonObject product = jsonObject.getAsJsonObject("Product");
                                JsonObject category = jsonObject.getAsJsonObject("Category");

                                try {

                                    String categories_name = category.get("name").getAsString();
                                    String id = product.get("id").getAsString();
                                    String name = product.get("name").getAsString();
                                    String price = product.get("price").getAsString();
                                    String security_price = product.get("security_price").getAsString();
                                    String image = "https://netforcesales.com/renteeze/webservice/files/products/" + product.get("images").getAsString();
                                    productListDatas.add(new ProductListData(id, name, image, price, categories_name, security_price));

                                } catch (Exception load_new) {

                                }

                            }

                            productsAdapter.notifyDataSetChanged();
                            loadingMore = false;
                            mSwipyRefreshLayout.setRefreshing(false);

                            layoutBottom.setVisibility(View.VISIBLE);
                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });
    }


    @Override
    public void onRefresh()
    {
               mSwipyRefreshLayout.setRefreshing(true);

                layoutBottom.setVisibility(View.GONE);

                productListDatas.clear();

                  product_count = 1;
                load_refresh_layout(1);
                dashboardContainer = new DashboardContainer();
                dashboardContainer.count_cart();

    }

    public void load_refresh_layout(int limit)
    {

        loadingMore = true;
        mSwipyRefreshLayout.setRefreshing(true);
        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/products/product_list?cat_id="+categoryId+"&limit="+limit+"&sort="+"")

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====" + result);

                            for (int i = 0; i < productListArray.size(); i++)
                            {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);
                                JsonObject product = jsonObject.getAsJsonObject("Product");
                                JsonObject category = jsonObject.getAsJsonObject("Category");

                                try
                                {

                                    String categories_name = category.get("name").getAsString();
                                    String id = product.get("id").getAsString();
                                    String name = product.get("name").getAsString();
                                    String price = product.get("price").getAsString();
                                    String security_price = product.get("security_price").getAsString();
                                    String image = "https://netforcesales.com/renteeze/webservice/files/products/" + product.get("images").getAsString();
                                    productListDatas.add(new ProductListData(id, name, image, price, categories_name,security_price));



                                }
                                catch (Exception load)
                                {


                                }




                            }

                            productsAdapter.notifyDataSetChanged();
                            loadingMore =false;
                            mSwipyRefreshLayout.setRefreshing(false);

                            layoutBottom.setVisibility(View.VISIBLE);
                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });
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

    private void showSortPopup()
    {

        ArrayList<String> options=new ArrayList<>();
        options.add("Relevance");
        options.add("Price low to high");
        options.add("Price high to low");
        options.add("Latest Additions");

        alertDialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.sort_popup, null);
        alertDialog.setView(convertView);
        final ListView lv = (ListView) convertView.findViewById(R.id.list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options);
        lv.setAdapter(adapter);
        final AlertDialog ad = alertDialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                if(adapter.getItem(i).toString().equals("Relevance"))
                {

                    load_sort("relevance");
                    ad.dismiss();
                }

                if (adapter.getItem(i).toString().equals("Price low to high"))
                {

                    load_sort("low_price");
                    ad.dismiss();
                }

                if (adapter.getItem(i).toString().equals("Price high to low"))
                {
                    load_sort("high_price");
                    ad.dismiss();

                }

                if (adapter.getItem(i).toString().equals("Latest Additions"))
                {

                     load_sort("latest");
                    ad.dismiss();

                }

                System.out.println("item=============="+adapter.getItem(i).toString());
               // load_sort(adapter.getItem(i).toString());


            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutSort:
                showSortPopup();
                break;
        }
    }

    private void reset()
    {
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
        product_count = 1;

       load_refresh(1);
        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();

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

    private void load_refresh(int limit)
    {
        // recyclerView.setVisibility(View.GONE);
        // homeDatas.clear();
        showProgressBar(context);
         loadingMore = true;
        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/products/product_list?cat_id="+categoryId+"&limit="+limit+"&sort="+"")

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====" + result);

                            for (int i = 0; i < productListArray.size(); i++) {

                                JsonObject jsonObject = (JsonObject) productListArray.get(i);
                                JsonObject product = jsonObject.getAsJsonObject("Product");
                                JsonObject category = jsonObject.getAsJsonObject("Category");

                                try {

                                    String categories_name = category.get("name").getAsString();
                                    String id = product.get("id").getAsString();
                                    String name = product.get("name").getAsString();
                                    String price = product.get("price").getAsString();
                                    String security_price = product.get("security_price").getAsString();
                                    String image = "https://netforcesales.com/renteeze/webservice/files/products/" + product.get("images").getAsString();
                                    productListDatas.add(new ProductListData(id, name, image, price, categories_name,security_price));


                                }catch (Exception r){


                                }




                            }

                            productsAdapter.notifyDataSetChanged();

                            loadingMore =false;

                            dismissProgressBar();
                            layoutBottom.setVisibility(View.VISIBLE);
                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });

    }





    private void load_sort(String sort)
    {
        layoutBottom.setVisibility(View.GONE);
        page=1;
        sortBy=0;
        productListDatas.clear();

        showProgressBar(context);

        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/products/product_list?cat_id="+categoryId+"&sort="+sort)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====" + result);

                            for (int i = 0; i < productListArray.size(); i++) {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);
                                JsonObject product = jsonObject.getAsJsonObject("Product");
                                JsonObject category = jsonObject.getAsJsonObject("Category");

                                try {

                                    String categories_name = category.get("name").getAsString();
                                    String id = product.get("id").getAsString();
                                    String name = product.get("name").getAsString();
                                    String price = product.get("price").getAsString();
                                    String security_price = product.get("security_price").getAsString();
                                    String image = "https://netforcesales.com/renteeze/webservice/files/products/" + product.get("images").getAsString();
                                    productListDatas.add(new ProductListData(id, name, image, price, categories_name, security_price));

                                }
                                catch (Exception a){


                                }


                                }

                                productsAdapter.notifyDataSetChanged();

                                dismissProgressBar();
                                layoutBottom.setVisibility(View.VISIBLE);
                            }else{

                                dismissProgressBar();
                                Log.e("error", e.toString());
                            }
                        }
                    }

                    );

                }


        @Override
    protected void onResume() {
        super.onResume();
        dashboardContainer = new DashboardContainer();

        try {
            invalidateOptionsMenu();

        } catch (Exception e) {

        }

        dashboardContainer.count_cart();

    }



}
