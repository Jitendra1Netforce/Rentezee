package com.rentezee.pojos.mcategory;

/**
 * Created by JitendraSingh on 10/26/2016.
 */

public class Response {
    private int success;
    private String message;
    private ProductListData data;

    public boolean isSuccess() {
        return success==1;
    }

    public String getMessage() {
        return message;
    }

    public ProductListData getData() {
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
