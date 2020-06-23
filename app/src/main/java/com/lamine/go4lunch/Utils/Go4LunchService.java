package com.lamine.go4lunch.Utils;

import com.lamine.go4lunch.BuildConfig;
import com.lamine.go4lunch.Models.Details.Details;
import com.lamine.go4lunch.Models.NearbySearch.Google;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Lamine MESSACI on 06/06/2020.
 */
public interface Go4LunchService {

    // Create a GET Request on a URL complement

    @GET("maps/api/place/nearbysearch/json?key=" + BuildConfig.MAPS_API_KEY)
    Observable<Google> getGoogleRestaurant(@Query("location") String location,
                                           @Query("radius") int radius,
                                           @Query("type") String type);

    @GET("maps/api/place/details/json?key=" + BuildConfig.MAPS_API_KEY)
    Observable<Details> getGoogleDetailsInfo(@Query("placeid") String placeId);
}
