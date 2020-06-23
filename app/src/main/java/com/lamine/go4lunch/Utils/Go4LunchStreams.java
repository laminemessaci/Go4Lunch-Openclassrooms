package com.lamine.go4lunch.Utils;

import com.lamine.go4lunch.Models.Details.Details;
import com.lamine.go4lunch.Models.NearbySearch.Google;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lamine MESSACI on 06/06/2020.
 */
public class Go4LunchStreams {

    private static Go4LunchStreams go4LunchStreams;
    private static Go4LunchService go4LunchService;

    private Go4LunchStreams() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        go4LunchService = retrofit.create(Go4LunchService.class);
    }

    // Google Places streams

    public static Go4LunchStreams getInstance() {
        if(go4LunchStreams == null) {
            synchronized(Go4LunchStreams.class) {
                if(go4LunchStreams == null)
                    go4LunchStreams = new Go4LunchStreams();
            }
        }
        return go4LunchStreams;
    }

    public Observable<Google> streamFetchGooglePlaces(String location, int radius, String type) {
        return go4LunchService.getGoogleRestaurant(location, radius, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(500, TimeUnit.SECONDS);
    }

    public Observable<Details> streamFetchGoogleDetailsInfo(String placeId) {
        return go4LunchService.getGoogleDetailsInfo(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(500, TimeUnit.SECONDS);
    }
}
