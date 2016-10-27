package com.rentezee.pojos;

/**
 * Created by JitendraSingh on 10/14/2016.
 */

public class GenericResponse {
    private int success;
    private String message;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return success==1;
    }

    public String getMessage() {
        return message;
    }
}
