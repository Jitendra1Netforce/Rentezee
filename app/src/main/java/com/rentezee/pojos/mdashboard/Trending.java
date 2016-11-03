package com.rentezee.pojos.mdashboard;

/**
 * Created by JitendraSingh on 10/17/2016.
 */

public class Trending {
    private int category_id;
    private String name;
    private String images;
    private int id;

    private int price;

    public int getCategoryId() {
        return category_id;
    }

    public String getCategoryName() {
        return name;
    }

    public String getImageUrl() {
        return images;
    }

    public int getProductId() {
        return id;
    }

    public String getProductName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Trending{" +
                "category_id=" + category_id +
                ", name='" + name + '\'' +
                ", images='" + images + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
