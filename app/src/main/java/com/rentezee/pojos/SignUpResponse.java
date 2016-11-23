package com.rentezee.pojos;

/**
 * Created by JitendraSingh on 10/13/2016.
 */

public class SignUpResponse {
    private long userId;
    private int success;
    private String message;

    @Override
    public String toString() {
        return "{" +
                "success=" + success +
                ", message=" + message +
                ", userId='" + userId +
                '}';
    }

    public long getUserId() {
        return userId;
    }

    public boolean isSuccess() {
        return success==1;
    }

    public String getMessage() {
        return message;
    }
}
