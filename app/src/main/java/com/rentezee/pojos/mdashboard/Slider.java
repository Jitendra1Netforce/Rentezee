package com.rentezee.pojos.mdashboard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JitendraSingh on 10/17/2016.
 */

public class Slider implements Parcelable {
    private int categoryId;
    private String categoryName;
    private String imageUrl;
    private int productId;

    protected Slider(Parcel in) {
        categoryId = in.readInt();
        categoryName = in.readString();
        imageUrl = in.readString();
        productId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryId);
        dest.writeString(categoryName);
        dest.writeString(imageUrl);
        dest.writeInt(productId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Slider> CREATOR = new Creator<Slider>() {
        @Override
        public Slider createFromParcel(Parcel in) {
            return new Slider(in);
        }

        @Override
        public Slider[] newArray(int size) {
            return new Slider[size];
        }
    };

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "Slider{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", productId=" + productId +
                '}';
    }
}
