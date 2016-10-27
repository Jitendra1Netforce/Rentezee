package com.rentezee.pojos;

/**
 * Created by JitendraSingh on 10/13/2016.
 */

public class LoginResponse {
    private int success;
    private String message;
    private User data;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isSuccess() {
        return success==1;
    }

    public String getMessage() {
        return message;
    }

    public User getData() {
        return data;
    }
}
