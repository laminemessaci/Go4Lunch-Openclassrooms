package com.lamine.go4lunch.Models.Details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours {
    @SerializedName("open_now")
    @Expose
    private boolean openNow;

    public boolean getOpenNow() {
        return openNow;
    }

}