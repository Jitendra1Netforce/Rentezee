package com.rentezee.fragments.my_cart;

/**
 * Created by John on 11/10/2016.
 */
public class MyCartData {

    public String product_id,product_name,image_url,rental_price,security_price;

    MyCartData(String product_id, String product_name, String image_url,String rental_price,String security_price)
    {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.rental_price = rental_price;
        this.security_price = security_price;

    }
}
