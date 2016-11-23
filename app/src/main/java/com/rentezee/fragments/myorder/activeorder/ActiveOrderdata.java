package com.rentezee.fragments.myorder.activeorder;

/**
 * Created by John on 11/11/2016.
 */
public class ActiveOrderdata {

    public String product_id,product_name,image_url,date,time;

    ActiveOrderdata(String product_id, String product_name, String image_url, String date, String time)
    {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.date = date;
        this.time = time;
    }

}
