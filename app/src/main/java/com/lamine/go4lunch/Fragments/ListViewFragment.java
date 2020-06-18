package com.lamine.go4lunch.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lamine.go4lunch.Controller.RestaurantActivity;
import com.lamine.go4lunch.Models.NearbySearch.Google;
import com.lamine.go4lunch.Models.NearbySearch.NearbyResult;
import com.lamine.go4lunch.R;
import com.lamine.go4lunch.Utils.GPSTracker;
import com.lamine.go4lunch.Utils.Go4LunchStreams;
import com.lamine.go4lunch.Utils.ItemClickSupport;
import com.lamine.go4lunch.Views.RestaurantsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.observers.DisposableObserver;


public class ListViewFragment extends BaseFragment {

    public List<NearbyResult> nearbyResultList;
    @BindView(R.id.listview_recyclerview) RecyclerView recyclerView;
    private String position;
    private RestaurantsAdapter listViewAdapter;

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        // LOCATION
        GPSTracker gpsTracker = new GPSTracker(getContext());
        position = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();

        this.configureRecyclerView();
        this.configureOnClickRecyclerView();
        this.executeHttpRequestWithRetrofit();
        setHasOptionsMenu(true);


        return view;
    }

    private void configureRecyclerView() {
        this.nearbyResultList = new ArrayList<>();
        this.listViewAdapter = new RestaurantsAdapter(getContext(), nearbyResultList, position);
        this.recyclerView.setAdapter(this.listViewAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    String placeId = listViewAdapter.getmResultList().get(position).getPlaceId();
                    Intent restaurantActivity = new Intent(getContext(), RestaurantActivity.class);
                    restaurantActivity.putExtra(ID, placeId);
                    startActivity(restaurantActivity);
                });
    }

    private void executeHttpRequestWithRetrofit() {
        disposable = Go4LunchStreams.getInstance().streamFetchGooglePlaces(position, 7000, RESTAURANT).subscribeWith(new DisposableObserver<Google>() {
            @Override
            public void onNext(Google google) {
                nearbyResultList.addAll(google.getResults());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                listViewAdapter.notifyItemRangeChanged(0, nearbyResultList.size());

            }
        });
    }

    public void displayAllRestaurants() {
        listViewAdapter.refreshAdapter(nearbyResultList);
    }

    public void refreshRestaurants(List<NearbyResult> nearbyResultListFilter) {
        listViewAdapter.refreshAdapter(nearbyResultListFilter);
    }
}
