package com.rentezee.fragments.my_cart;

/**
 * Created by John on 11/10/2016.
 */
public class MyCartData {

    public String my_cart_id, product_id,product_name,image_url,rental_price,security_price,category_name,total_per_item,rent_duration,from_date,to_date;

    MyCartData(String my_cart_id,String product_id, String product_name, String image_url,String rental_price,String security_price,String category_name,String rent_duration,String from_date,String to_date,String total_per_item)
    {

        this.my_cart_id = my_cart_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.rental_price = rental_price;
        this.security_price = security_price;
        this.category_name = category_name;
        this.rent_duration = rent_duration;
        this.from_date = from_date;
        this.to_date=to_date;
        this.total_per_item = total_per_item;
    }
}
