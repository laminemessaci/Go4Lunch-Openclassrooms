package com.lamine.go4lunch.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lamine MESSACI on 12/06/2020.
 */

public class Prefs {

    private static final String LANGUAGE_CHOICE = "language";
    private static final String MY_PREFS = "my_prefs";
    private static final String USER_PREFS = "user_prefs";
    private static final String RESTAURANTS = " Restaurants";

    //This class using SharedPreferences and the Gson library

    private static Prefs instance;
    private static SharedPreferences prefs;


    //Class Prefs constructor
    Prefs(Context context) {
        prefs = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    }

    //Prefs.get is called to create a new instance of Prefs
    public static Prefs get(Context context) {
        if(instance == null)
            instance = new Prefs(context);
        return instance;
    }

    public void storeLanguageChoice(String language_choice) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LANGUAGE_CHOICE, language_choice);
        editor.apply();
    }

    public String getLanguage() {
        return prefs.getString(LANGUAGE_CHOICE, "");
    }
}