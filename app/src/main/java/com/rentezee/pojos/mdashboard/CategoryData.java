package com.rentezee.pojos.mdashboard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JitendraSingh on 10/15/2016.
 */

public class CategoryData implements Parcelable {
    private int id;
    private String name;
    private String image;

    protected CategoryData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryData> CREATOR = new Creator<CategoryData>() {
        @Override
        public CategoryData createFromParcel(Parcel in) {
            return new CategoryData(in);
        }

        @Override
        public CategoryData[] newArray(int size) {
            return new CategoryData[size];
        }
    };

    public int getCategoryId() {
        return id;
    }

    public String getCategoryName() {
        return name;
    }

    public String getImageUrl() {
        return image;
    }

    @Override
    public String toString() {
        return "CategoryData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
