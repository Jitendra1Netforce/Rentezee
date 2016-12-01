package com.rentezee.fragments.rent_it_out.productview;

/**
 * Created by John on 11/21/2016.
 */
public class ProductViewData {

    public String product_id,product_name,image_url,description, security_price, price,category_name;


    ProductViewData(String product_id, String product_name, String image_url, String description, String security_price, String price,String category_name)
    {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.description = description;
        this.security_price = security_price;
        this.price = price;
        this.category_name = category_name;
    }


}
