package com.rentezee.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JitendraSingh on 10/13/2016.
 */

public class User implements Parcelable{
    private long userId;
    private String name;
    private String email;
    private String mobile;
    private String imageUrl;
    private int status;

    public User(long userId,String name, String email, String mobile,String imageUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.imageUrl = imageUrl;
    }

    protected User(Parcel in) {
        userId = in.readLong();
        name = in.readString();
        email = in.readString();
        mobile = in.readString();
        imageUrl = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(imageUrl);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", status=" + status +
                '}';
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStatus() {
        return status;
    }
}
