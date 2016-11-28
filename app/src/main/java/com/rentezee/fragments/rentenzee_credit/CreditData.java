package com.rentezee.fragments.rentenzee_credit;

/**
 * Created by John on 11/28/2016.
 */
public class CreditData {

    public String my_cart_id, product_id,product_name,image_url,rental_price,security_price,category_name;

    CreditData(String my_cart_id,String product_id, String product_name, String image_url,String rental_price,String security_price,String category_name)
    {

        this.my_cart_id = my_cart_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.rental_price = rental_price;
        this.security_price = security_price;
        this.category_name = category_name;

    }
}
