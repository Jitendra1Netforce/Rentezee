package com.rentezee.main;

/**
 * Created by John on 11/4/2016.
 */
public class SearchData {

    public String product_id,product_name,image_url,price,category_name;

    SearchData(String product_id, String product_name, String image_url,String price,String category_name)
    {

        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.price = price;
        this.category_name = category_name;

    }
}
