package com.rentezee.navigation.myorder.PastOrder;

/**
 * Created by John on 11/21/2016.
 */
public class PastOrderData {

    public String product_id,order_date,price,product_status,order_number,delivery_date;
    PastOrderData(String product_id, String order_date, String price, String product_status , String order_number , String delivery_date)
    {

        this.product_id = product_id;
        this.order_date = order_date;
        this.price = price;
        this.product_status = product_status;
        this.order_number = order_number;
        this.delivery_date = delivery_date;
    }

}
