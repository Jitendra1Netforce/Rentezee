package com.rentezee.pojos.mdetail;

import com.rentezee.pojos.mdashboard.DashboardData;

import java.util.ArrayList;

/**
 * Created by JitendraSingh on 10/27/2016.
 */

public class ProductDetail {

    private ArrayList<String> images;
    private int productId, price, offerPrice, securityPrice;
    private String productName;
    private String model, description;

    public ArrayList<String> getImages() {
        return images;
    }

    public int getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public int getSecurityPrice() {
        return securityPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getModel() {
        return model;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
                "images=" + images +
                ", productId=" + productId +
                ", price=" + price +
                ", offerPrice=" + offerPrice +
                ", securityPrice=" + securityPrice +
                ", productName='" + productName + '\'' +
                ", model='" + model + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
