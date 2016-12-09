package com.rentezee.fragments.delivery;

/**
 * Created by John on 12/7/2016.
 */

public class DeliveryData {

    public String product_id,address_label,address_1,address_2,city,country,user_id,zip_code;

    DeliveryData(String product_id, String address_label, String address_1, String address_2,String city,String country,String user_id,String zip_code)
    {
        this.product_id = product_id;
        this.address_label = address_label;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.city= city;
        this.country= country;
        this.user_id = user_id;
        this.zip_code= zip_code;
    }

}
