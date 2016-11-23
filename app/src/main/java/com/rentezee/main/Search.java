package com.rentezee.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.Debugger;

import java.util.ArrayList;

public class Search extends BaseActivity {

    private static final String TAG=Search.class.getSimpleName();
    private Context context;
    private SearchView searchView;
    ArrayList<SearchData> searchDatas = new ArrayList<>();
    SearchAdapter searchAdapter;
    ListView lvProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        lvProducts=(ListView)findViewById(R.id.lvProducts);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search, menu);
        // Associate searchable configuration with the SearchView
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Debugger.i(TAG,"onQueryTextSubmit "+ query);


                load_refresh();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Debugger.i(TAG,"onQueryTextChange "+ newText);
                return false;
            }
        });
        return true;
    }

    private void load_refresh()
    {
        // recyclerView.setVisibility(View.GONE);

        searchDatas.clear();

        showProgressBar(context);

        Ion.with(this)
                .load("http://netforce.biz/renteeze/webservice/products/product_list?cat_id=1")

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            JsonArray productListArray = result.getAsJsonArray("data");

                            System.out.println("data=====");

                            for (int i = 0; i < productListArray.size(); i++) {
                                JsonObject jsonObject = (JsonObject) productListArray.get(i);
                                JsonObject product = jsonObject.getAsJsonObject("Product");
                                String id = product.get("id").getAsString();
                                String name = product.get("name").getAsString();
                                String price = product.get("price").getAsString();
                                String category_name = product.get("special_price").getAsString();
                                String image = "http://netforce.biz/renteeze/webservice/files/products/" + product.get("images").getAsString();
                                searchDatas.add(new SearchData(id, name, image, price, category_name));

                            }
                            searchAdapter = new SearchAdapter(context, searchDatas);
                            lvProducts.setAdapter(searchAdapter);
                            searchAdapter.notifyDataSetChanged();

                            dismissProgressBar();

                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });

    }

}
