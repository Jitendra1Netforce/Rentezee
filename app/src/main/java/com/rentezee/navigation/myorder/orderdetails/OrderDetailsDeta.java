package com.rentezee.navigation.myorder.orderdetails;

/**
 * Created by John on 12/12/2016.
 */

public class OrderDetailsDeta {


    public String product_name,product_type,image_url,categories_name,rent_price,security_price,rent_duration,duration_date,total_item_amount

            ;

    OrderDetailsDeta(String product_name, String product_type, String categories_name,String rent_price,String security_price,String total_item_amount,String rent_duration, String duration_date,String image_url)
    {
        this.product_name = product_name;
        this.product_type = product_type;
        this.categories_name = categories_name;
        this.rent_price = rent_price;
        this.security_price = security_price;
        this.total_item_amount = total_item_amount;
        this.rent_duration = rent_duration;
        this.duration_date = duration_date;
        this.image_url = image_url;


    }

}
