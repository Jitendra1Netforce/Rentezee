package com.rentezee.pojos.mdashboard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JitendraSingh on 10/17/2016.
 */

public class Slider implements Parcelable {
    private int id;
    private String title;
    private String image;
    private int productId;

    protected Slider(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
        productId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image);
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
        return id;
    }

    public String getCategoryName() {
        return title;
    }

    public String getImageUrl() {
        return image;
    }

    public int getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "Slider{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
