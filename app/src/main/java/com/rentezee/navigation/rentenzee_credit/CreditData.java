package com.rentezee.navigation.rentenzee_credit;

/**
 * Created by John on 11/28/2016.
 */
public class CreditData {

    public String cart_via, amount,created,type;

    CreditData(String cart_via,String amount, String created, String type)
    {
        this.cart_via = cart_via;
        this.amount = amount;
        this.created = created;
        this.type = type;

    }
}
