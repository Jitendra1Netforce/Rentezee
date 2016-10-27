package com.rentezee.pojos.mcategory;

/**
 * Created by JitendraSingh on 10/26/2016.
 */

public class Product {
    private int productId;
    private String productName;
    private String imageUrl;
    private int price, offerPrice;

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", offerPrice=" + offerPrice +
                '}';
    }
}
