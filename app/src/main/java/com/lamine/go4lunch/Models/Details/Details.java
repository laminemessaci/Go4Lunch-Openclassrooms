package com.lamine.go4lunch.Models.Details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("result")
    @Expose
    private Result result = null;

    public Result getResult() {
        return result;
    }

}