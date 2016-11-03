package com.rentezee.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by John on 11/3/2016.
 */
public class CategoriesData  implements Parcelable {


    public String category_id,category_name,image_url;

    CategoriesData(String category_id, String category_name, String image_url)
    {
        this.category_id = category_id;
        this.category_name = category_name;
        this.image_url = image_url;

    }


    protected CategoriesData(Parcel in) {
        category_id = in.readString();
        category_name = in.readString();
        image_url = in.readString();
    }


    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(category_id);
        dest.writeString(category_name);
        dest.writeString(image_url);
    }


    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CategoriesData> CREATOR = new Parcelable.Creator<CategoriesData>() {
        @Override
        public CategoriesData createFromParcel(Parcel in) {
            return new CategoriesData(in);
        }

        @Override
        public CategoriesData[] newArray(int size) {
            return new CategoriesData[size];
        }
    };

}
