package com.rentezee.my;

/**
 * Created by John on 11/3/2016.
 */
public class ProductList
{

    public String product_id,product_name,image_url, price,offer_price;

    ProductList(String product_id, String product_name, String image_url,String price,String offer_price)
    {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.price = price;
        this.offer_price = offer_price;
    }


}
