package com.lamine.go4lunch.Models.Helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lamine MESSACI on 16/06/2020.
 */
public class User implements Parcelable {

    private String uid;
    private String username;
    private String urlPicture;
    private String joinedRestaurant;
    private String restaurantId;
    private String choice;

    public User(){}

    User(String uid, String username, String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }

    private User(Parcel in) {
        uid = in.readString();
        username = in.readString();
        urlPicture = in.readString();
        joinedRestaurant = in.readString();
        restaurantId = in.readString();
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

    // --- GETTERS ---

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getUrlPicture() {return urlPicture; }

    public String getJoinedRestaurant() {
        return joinedRestaurant;
    }

    public String getChoice(){return choice;}

    // --- SETTERS ---

    void setJoinedRestaurant(String joinedRestaurant) {
        this.joinedRestaurant = joinedRestaurant;
    }

    void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setChoice(String choise ){ this.choice = choise;}

    // ------------------------------------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(username);
        dest.writeString(urlPicture);
        dest.writeString(joinedRestaurant);
        dest.writeString(restaurantId);
    }
}