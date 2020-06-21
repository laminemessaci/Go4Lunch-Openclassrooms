package com.lamine.go4lunch.Utils;

/**
 * Created by Lamine MESSACI on 20/06/2020.
 */
public class Constants {



    public static final String APIKey = "AIzaSyB6npGzQpiEdM7mSaqSu_XUhFb-gh9EOeA";


    //----------------------------
    // MainScreenActivity
    //----------------------------

    public static final int SIGN_OUT_TASK = 10;
    public static final String ID = "ID";
    public static final String GET_RESTAURANT_ID = "restaurantId";

    //----------------------------
    // RestaurantActivity
    //----------------------------
    public static final String GET_ID = "ID";
    public  static final String JOIN = "JOIN";
    public static final String NO_LONGER_JOIN = "DISJOINT";
    public static final String TEL = "tel";
    public static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&maxheight=150&key="+APIKey+"&photoreference=";


    //----------------------------
    // NOTIFICATIONS
    //----------------------------
    public static final String GET_JOINED_RESTAURANT = "joinedRestaurant";
    public static final String GET_JOINED_RESTAURANT_ID = "restaurantId";
    public static final String GET_RESTAURANT_VICINITY = "vicinity";
    public static final String GET_USERNAME = "username";
    public static final String GET_USER_ID = "uid";
}
