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
        return "SignUpResponse{" +
                "userId=" + userId +
                ", success=" + success +
                ", message='" + message + '\'' +
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
