package com.lamine.go4lunch.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lamine.go4lunch.BuildConfig;
import com.lamine.go4lunch.Models.Details.Details;
import com.lamine.go4lunch.Models.Details.Result;
import com.lamine.go4lunch.Models.Helper.User;
import com.lamine.go4lunch.Models.Helper.UserHelper;
import com.lamine.go4lunch.R;
import com.lamine.go4lunch.Utils.Go4LunchStreams;
import com.lamine.go4lunch.Views.WorkmatesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.lamine.go4lunch.Utils.Constants.GET_ID;
import static com.lamine.go4lunch.Utils.Constants.GET_RESTAURANT_ID;
import static com.lamine.go4lunch.Utils.Constants.JOIN;
import static com.lamine.go4lunch.Utils.Constants.NO_LONGER_JOIN;
import static com.lamine.go4lunch.Utils.Constants.PICTURE_URL;
import static com.lamine.go4lunch.Utils.Constants.TEL;

public class RestaurantActivity extends BaseActivity {


    @BindView(R.id.activity_restaurant_restaurant_picture)
    ImageView restaurantImageView;
    @BindView(R.id.activity_restaurant_address)
    TextView restaurantAddress;
    @BindView(R.id.activity_restaurant_name)
    TextView restaurantName;
    @BindView(R.id.activity_restaurant_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.activity_restaurant_ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.activity_restaurant_button_like)
    Button likeBtn;
    @BindView(R.id.restaurant_activity_go_button)
    FloatingActionButton floatButton;
    @BindView(R.id.toolbar_restaurant)
    Toolbar toolbar;

    private Disposable disposable;
    private String placeId;
    private List<User> userList;
    private Result result;
    private WorkmatesAdapter workmatesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        placeId = getIntent().getStringExtra(GET_ID);

        this.configureRecyclerView();
        this.executeHttpRequestWithRetrofit();
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }
    //add toolbar and back home

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_restaurant;
    }

    // Configure the recyclerView and glue it with the adapter
    public void configureRecyclerView() {
        this.userList = new ArrayList<>();
        this.workmatesAdapter = new WorkmatesAdapter(userList);
        this.recyclerView.setAdapter(this.workmatesAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // Execute API request to get details of the restaurant
    private void executeHttpRequestWithRetrofit() {
        this.disposable = Go4LunchStreams.getInstance().streamFetchGoogleDetailsInfo(placeId).subscribeWith(new DisposableObserver<Details>() {
            @Override
            public void onNext(Details details) {
                result = details.getResult();
                UserHelper.getRestaurantInfo(result, userList -> {
                    RestaurantActivity.this.userList = userList;
                    workmatesAdapter.refreshAdapter(userList);
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                updateUI();
            }
        });
    }

    private void updateUI() {
        // Get the restaurant name
        restaurantName.setText(result.getName());

        // Get the restaurant vicinity
        String vicinity = result.getTypes().get(0) + " - " + result.getVicinity();
        restaurantAddress.setText(vicinity);

        // Get the restaurant rating
        if(result.getRating() != 0) {
            double googleRating = result.getRating();
            double rating = googleRating / 5 * 3;
            this.ratingBar.setRating((float) rating);
            this.ratingBar.setVisibility(View.VISIBLE);
        } else {
            this.ratingBar.setVisibility(View.GONE);
        }

        // Get the restaurant picture
        if(!(result.getPhotos() == null)) {
            if(!(result.getPhotos().isEmpty())) {
                Glide.with(this)
                        .load(PICTURE_URL + result.getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.MAPS_API_KEY)
                        .centerCrop()
                        .into(restaurantImageView);
            }
        } else {
            Glide.with(this)
                    .load(R.drawable.nopicture)
                    .centerCrop()
                    .into(restaurantImageView);
        }
        this.restoreLikeButton();
        this.restoreGoButton();
    }

    private void configureCustomTabs() {
        String url = result.getWebsite();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    // --------------
    // BUTTONS
    // --------------

    @OnClick(R.id.restaurant_activity_go_button)
    public void onClickFloatingButton(View v) {
        if(v.getId() == R.id.restaurant_activity_go_button) {
            if(JOIN.equals(floatButton.getTag())) {
                this.joinTheRestaurant();
            } else {
                this.disjointTheRestaurant();
            }
        }
    }

    public void joinTheRestaurant() {
        UserHelper.updateUserRestaurant(UserHelper.getCurrentUser().getUid(), result.getName(), result.getPlaceId(), result.getVicinity());
        floatButton.setImageDrawable(getResources().getDrawable(R.drawable.validate));
        floatButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        floatButton.setColorFilter(getResources().getColor(R.color.colorTransparent));
        Toast.makeText(this, getResources().getString(R.string.join), Toast.LENGTH_SHORT).show();
        floatButton.setTag(NO_LONGER_JOIN);
    }

    public void disjointTheRestaurant() {
        UserHelper.deleteUserRestaurant(UserHelper.getCurrentUser().getUid());
        floatButton.setImageDrawable(getResources().getDrawable(R.drawable.pic_logo_go4lunch_512x512));
        floatButton.setColorFilter(getResources().getColor(R.color.toolbar_darker));
        Toast.makeText(this, getResources().getString(R.string.no_longer_join), Toast.LENGTH_SHORT).show();
        floatButton.setTag(JOIN);
    }

    @OnClick(R.id.activity_restaurant_button_call)
    public void onClickCall() {
        if(result.getFormattedPhoneNumber() != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(TEL, result.getFormattedPhoneNumber(), null));
            startActivity(callIntent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.phone_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    // Display the restaurant website when the user click on the button
    @OnClick(R.id.activity_restaurant_button_website)
    public void onClickWeb() {
        if(result.getWebsite() != null) {
            this.configureCustomTabs();
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getWebsite()));
//            startActivity(browserIntent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.website_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.activity_restaurant_button_like)
    public void onClickLike(View v) {
        if(v.getId() == R.id.activity_restaurant_button_like) {
            if(getResources().getString(R.string.LIKE).equals(likeBtn.getText())) {
                this.likeTheRestaurant();
            } else {
                this.dislikeThisRestaurant();
            }
        }
    }

    private void likeTheRestaurant() {
        if(UserHelper.getCurrentUser() != null) {
            UserHelper.createLike(result.getPlaceId(), UserHelper.getCurrentUser().getUid()).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(this, getResources().getString(R.string.like_restaurant), Toast.LENGTH_SHORT).show();
                    likeBtn.setText(getResources().getString(R.string.UNLIKE));
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_restaurant), Toast.LENGTH_SHORT).show();
        }
    }

    private void dislikeThisRestaurant() {
        if(UserHelper.getCurrentUser() != null) {
            UserHelper.deleteLike(result.getPlaceId(), UserHelper.getCurrentUser().getUid());
            likeBtn.setText(getResources().getString(R.string.LIKE));
            Toast.makeText(this, getResources().getString(R.string.dislike_restaurant), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_restaurant), Toast.LENGTH_SHORT).show();
        }
    }

    private void restoreLikeButton() {
        UserHelper.restoreLike(UserHelper.getCurrentUser().getUid()).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if(task.getResult().isEmpty()) {
                    likeBtn.setText(getResources().getString(R.string.LIKE));
                } else {
                    for(DocumentSnapshot restaurant : task.getResult()) {
                        if(result.getPlaceId().equals(restaurant.getId())) {
                            likeBtn.setText(getResources().getString(R.string.UNLIKE));
                            break;
                        } else {
                            likeBtn.setText(getResources().getString(R.string.LIKE));
                        }
                    }
                }
            }
        });
    }

    private void restoreGoButton() {
        UserHelper.getBookingRestaurant(UserHelper.getCurrentUser().getUid()).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String restaurantId = task.getResult().getString(GET_RESTAURANT_ID);
                if(restaurantId != null && restaurantId.equals(result.getPlaceId())) {
                    floatButton.setImageDrawable(getResources().getDrawable(R.drawable.validate));
                    floatButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    floatButton.setColorFilter(getResources().getColor(R.color.colorTransparent));
                    floatButton.setTag(NO_LONGER_JOIN);
                } else {
                    floatButton.setImageDrawable(getResources().getDrawable(R.drawable.pic_logo_go4lunch_512x512));
                    floatButton.setColorFilter(getResources().getColor(R.color.toolbar_darker));
                    floatButton.setTag(JOIN);
                }
            }
        });

    }

    // Dispose subscription
    private void disposeWhenDestroy() {
        if(this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }
}