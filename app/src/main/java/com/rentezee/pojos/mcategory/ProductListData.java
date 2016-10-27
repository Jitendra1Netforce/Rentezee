package com.rentezee.pojos.mcategory;

import java.util.ArrayList;

/**
 * Created by JitendraSingh on 10/26/2016.
 */

public class ProductListData {
    private ArrayList<Product> products;
    private int totalProductsCount, page, itemsPerPage;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public int getTotalProductsCount() {
        return totalProductsCount;
    }

    public int getPage() {
        return page;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    @Override
    public String toString() {
        return "ProductListData{" +
                "products=" + products +
                ", totalProductsCount=" + totalProductsCount +
                ", page=" + page +
                ", itemsPerPage=" + itemsPerPage +
                '}';
    }
}
