package com.rentezee.fragments.wishlist;

/**
 * Created by John on 11/4/2016.
 */
public class WishListData {

    public String wishlist_id ,product_id,product_name,image_url,price,category_name;

    WishListData(String wishlist_id, String product_id, String product_name, String image_url,String price,String category_name)
    {

        this.wishlist_id = wishlist_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.image_url = image_url;
        this.price = price;
        this.category_name = category_name;

    }
}
