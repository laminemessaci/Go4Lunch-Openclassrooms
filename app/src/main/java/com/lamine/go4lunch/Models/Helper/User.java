package com.lamine.go4lunch.Models.Helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lamine MESSACI on 16/06/2020.
 */
public class User implements Parcelable {

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
    private String uid;
    private String username;
    private String urlPicture;
    private String joinedRestaurant;
    private String restaurantId;
    private String choice;
    private String userEmail;


    public User() {
    }

    User(String uid, String username, String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }

    public User(String uid, String username, String urlPicture, String restaurantId, String userEmail) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.joinedRestaurant = joinedRestaurant;
        this.restaurantId = restaurantId;
        this.userEmail = userEmail;
        this.choice = choice;
    }

    private User(Parcel in) {
        uid = in.readString();
        username = in.readString();
        urlPicture = in.readString();
        joinedRestaurant = in.readString();
        restaurantId = in.readString();
    }

    // --- GETTERS ---

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public String getJoinedRestaurant() {
        return joinedRestaurant;
    }

    void setJoinedRestaurant(String joinedRestaurant) {
        this.joinedRestaurant = joinedRestaurant;
    }

    public String getChoice() {
        return choice;
    }


    // --- SETTERS ---

    public void setChoice(String choise) {
        this.choice = choise;
    }

    public String getIdRestaurant() {
        return restaurantId;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.restaurantId = idRestaurant;
    }

    void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

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