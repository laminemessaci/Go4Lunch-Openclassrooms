package com.lamine.go4lunch.Models.NearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Google {

    @SerializedName("results")
    @Expose
    private List<NearbyResult> results = null;

    public List<NearbyResult> getResults() {
        return results;
    }

}