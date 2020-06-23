package com.lamine.go4lunch.Models.NearbySearch;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyResult {

    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    public Geometry getGeometry() {
        return geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getVicinity() {
        return vicinity;
    }

    public double getRating() {
        return rating;
    }
}
