package com.rentezee.fragments.rent_it_out.productview;

/**
 * Created by John on 11/21/2016.
 */
public class ProductViewData {

    public String product_id,product_name,image_url,date,time;


    ProductViewData(String product_id, String product_name, String image_url, String date, String time)
    {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.date = date;
        this.time = time;
    }


}
