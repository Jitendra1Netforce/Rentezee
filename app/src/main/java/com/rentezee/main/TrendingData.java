package com.rentezee.main;

/**
 * Created by John on 11/3/2016.
 */
public class TrendingData {

    public String product_id,product_name,image_url,price,special_price,category_name;

    TrendingData(String product_id, String product_name, String image_url,String price,String special_price,String category_name)
    {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.price = price;
        this.special_price = special_price;
        this.category_name = category_name;
    }

}
