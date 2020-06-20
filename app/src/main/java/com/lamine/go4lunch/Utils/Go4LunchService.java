package com.lamine.go4lunch.Utils;

import com.lamine.go4lunch.Models.Details.Details;
import com.lamine.go4lunch.Models.NearbySearch.Google;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.lamine.go4lunch.Utils.Constants.APIKey;

/**
 * Created by Lamine MESSACI on 06/06/2020.
 */
public interface Go4LunchService {

    // Create a GET Request on a URL complement

    @GET("maps/api/place/nearbysearch/json?key="+APIKey)
    Observable<Google> getGoogleRestaurant(@Query("location") String location,
                                           @Query("radius") int radius,
                                           @Query("type") String type);

    @GET("maps/api/place/details/json?key="+APIKey)
    Observable<Details> getGoogleDetailsInfo(@Query("placeid") String placeId);
}
