package com.lamine.go4lunch.Models.NearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    @SerializedName("weekday_text")
    @Expose
    private List<Object> weekday_text = null;

    public List<Object> getWeekdayText() {
        return weekday_text;
    }

    public void setWeekdayText(List<Object> weekdayText) {
        this.weekday_text = weekdayText;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

}