package com.lamine.go4lunch.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.lamine.go4lunch.Models.Details.Result;
import com.lamine.go4lunch.Models.Helper.User;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lamine MESSACI on 12/06/2020.
 */

public class Prefs {

    private static final String LANGUAGE_CHOICE = "language";
    private static final String MY_PREFS = "my_prefs";
    private static final String USER_PREFS = "user_prefs";
    private static final String RESTAURANTS = " Restaurants" ;

    //This class using SharedPreferences and the Gson library

    private static Prefs instance;
    private static SharedPreferences prefs;


    //Class Prefs constructor
    Prefs(Context context) {
        prefs = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    }
    //Prefs.get is called to create a new instance of Prefs
    public static Prefs get(Context context) {
        if (instance == null)
            instance = new Prefs(context);
        return instance;
    }

    //storeCategories change ArrayList into json strings and save it
    public void storeUserPrefs(User user) {
        //start writing (open the file)
        SharedPreferences.Editor editor = prefs.edit();
        //put the data
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(USER_PREFS, json);
        //close the file
        editor.apply();
    }
    public void storeChoicePrefs(Result restaurant) {
        //start writing (open the file)
        SharedPreferences.Editor editor = prefs.edit();
        //put the data
        Gson gson = new Gson();
        String json = gson.toJson(restaurant);
        editor.putString(RESTAURANTS, json);
        //close the file
        editor.apply();
    }

    //getCategories recovers json strings and return there in ArrayList
    public User getPrefsUser() {
        Gson gson = new Gson();
        String json = prefs.getString(USER_PREFS, "");
        return gson.fromJson(json, User.class);
    }

    public Result getChoice(){
        Gson gson = new Gson();
        String json = prefs.getString(RESTAURANTS, "");
        return gson.fromJson(json, Result.class);
    }

    public void storePicture(Uri uri, String userName){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(userName, uri.toString());
        editor.apply();
    }

    public Uri getPicture(String userName){
        String imageUriString = prefs.getString(userName, "");
        assert imageUriString != null;
        if(!imageUriString.isEmpty()){
            return Uri.parse(imageUriString);
        }else {
            return null;
        }
    }

    public void storeListResults(List<Result> resultDetailList){
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(resultDetailList);
        editor.putString("results", json);
        editor.apply();
    }

    public void storeLanguageChoice(String language_choice){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LANGUAGE_CHOICE, language_choice);
        editor.apply();
    }

    public String getLanguage(){
        return prefs.getString(LANGUAGE_CHOICE, "");
    }
}