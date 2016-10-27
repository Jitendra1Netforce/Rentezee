package com.rentezee.pojos.mdetail;

import com.rentezee.pojos.mdashboard.DashboardData;

/**
 * Created by JitendraSingh on 10/27/2016.
 */

public class Response {
    private int success;
    private String message;
    private ProductDetail data;

    public boolean isSuccess() {
        return success==1;
    }

    public String getMessage() {
        return message;
    }

    public ProductDetail getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
