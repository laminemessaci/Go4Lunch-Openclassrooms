package com.lamine.go4lunch.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.lamine.go4lunch.Fragments.ListViewFragment;
import com.lamine.go4lunch.Fragments.MapViewFragment;
import com.lamine.go4lunch.Fragments.WorkmatesFragment;
import com.lamine.go4lunch.Models.Helper.User;
import com.lamine.go4lunch.Models.Helper.UserHelper;
import com.lamine.go4lunch.Models.NearbySearch.NearbyResult;
import com.lamine.go4lunch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class MainScreenActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    // FOR DESIGN
    @BindView(R.id.first_screen_toolbar)
    Toolbar toolbar;
    @BindView(R.id.first_screen_drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.first_screen_bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.first_screen_navigation_view)
    NavigationView navigationView;
    private ImageView profileImageView;
    private TextView emailTextView;
    private TextView nameTextView;
    private LatLngBounds latLngBounds;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;

    // FOR DATA
    private static final int SIGN_OUT_TASK = 10;
    public static final String ID = "ID";
    private static final String GET_RESTAURANT_ID = "restaurantId";

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main_screen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.configureToolbar();
        this.configureNavigationView();
        this.configureBottomNavigationView();
        this.configureDrawerLayout();

        View view = navigationView.getHeaderView(0);
        profileImageView = view.findViewById(R.id.header_profile_picture);
        emailTextView = view.findViewById(R.id.header_email);
        nameTextView = view.findViewById(R.id.header_name);

        this.updateUIWhenCreating();

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        this.autoCompleteTextViewOnClick();

        displayFragments(MapViewFragment.newInstance());
    }

    // -------------------
    // CONFIGURATION
    // -------------------

    // Handle Navigation View Click
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.yourLunch:
                UserHelper.getBookingRestaurant(UserHelper.getCurrentUser().getUid()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String restaurantId = task.getResult().getString(GET_RESTAURANT_ID);
                        if (restaurantId != null) {
                            Intent intent = new Intent(this, RestaurantActivity.class);
                            intent.putExtra(ID, restaurantId);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.no_restaurant), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.logout:
                this.signOutUserFromFireBase();
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // Handle Bottom Navigation View Click
    private void configureBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            int id = menuItem.getItemId();

            // Set current location in the ViewPager to handle the position of the fragments
            switch (id) {
                case R.id.list_view:
                    displayFragments(ListViewFragment.newInstance());
                    cleanAutoCompleteTextView();
                    break;
                case R.id.workmates:
                    displayFragments(WorkmatesFragment.newInstance());
                    cleanAutoCompleteTextView();
                    break;
                default:
                    displayFragments(MapViewFragment.newInstance());
                    cleanAutoCompleteTextView();
                    break;
            }
            return true;
        });
    }

    private void cleanAutoCompleteTextView() {
        autoCompleteTextView.setText("");
    }

    // This method show and replace fragments after touching the bottom navigation view buttons
    private void displayFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    // Configure Toolbar
    private void configureToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.im_hungry));
    }

    // Configure DrawerLayout
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Update UI when activity is created
    private void updateUIWhenCreating() {

        if (UserHelper.getCurrentUser() != null) {

            // Get picture URL from FireBase
            if (UserHelper.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(UserHelper.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(profileImageView);
            }

            // Get email & username
            String email = TextUtils.isEmpty(UserHelper.getCurrentUser().getEmail()) ?
                    getResources().getString(R.string.no_email_found) : UserHelper.getCurrentUser().getEmail();
            this.emailTextView.setText(email);

            UserHelper.getUser(UserHelper.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
                User currentUser = documentSnapshot.toObject(User.class);
                String username = TextUtils.isEmpty(currentUser.getUsername()) ?
                        getResources().getString(R.string.no_username_found) : currentUser.getUsername();
                nameTextView.setText(username);
            });
        }
    }

    // -----------------
    // REST REQUEST
    // -----------------

    // Create http requests for SignOut

    private void signOutUserFromFireBase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRequestCompleted(SIGN_OUT_TASK));
    }

    // -------------
    // UI
    // -------------

    // Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRequestCompleted(final int origin) {
        return aVoid -> {
            if (origin == SIGN_OUT_TASK) {
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    // ----------------
    // GETTERS && SETTERS
    // ----------------

    // Get && set the latitude/longitude aligned rectangle.
    public LatLngBounds getLatLngBounds() {
        return latLngBounds;
    }

    public void setLatLngBounds(LatLngBounds latLngBounds) {
        this.latLngBounds = latLngBounds;
    }

    // ----------------
    // AUTOCOMPLETE
    // ----------------

    private void autoCompleteTextViewOnClick() {

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    configureAutoPredictions(s);
                } else {
                    // Create a Fragment and find the view with the ID
                    Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder);
                    // Check if the Fragment is an instance of ListViewFragment
                    if (fragmentById instanceof ListViewFragment) {
                        // Cast the Fragment with the ListViewFragment to call the displayAllRestaurants method
                        ((ListViewFragment) fragmentById).displayAllRestaurants();
                    } else if (fragmentById instanceof MapViewFragment) {
                        ((MapViewFragment) fragmentById).displayAllRestaurants();
                    }
                }

            }
        });

        // Handle the user click on the AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {

            String item = adapter.getItem(position);
            // Create an ArrayList to contain the filter result
            List<NearbyResult> nearbyResultListFilter = new ArrayList<>();
            // Create an ArrayList to contain the request result
            List<NearbyResult> nearbyResultList = new ArrayList<>();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder);
            if (fragmentById instanceof ListViewFragment) {
                // Fill the new List with the fragment list result
                nearbyResultList = ((ListViewFragment) fragmentById).nearbyResultList;
            } else if (fragmentById instanceof MapViewFragment) {
                nearbyResultList = ((MapViewFragment) fragmentById).nearbyResultList;
            }
            // For each NearbyResult into the ArrayList
            for (NearbyResult nearbyResult : nearbyResultList) {
                if (nearbyResult.getName().equals(item)) {
                    // Add the specific result of nearbyResult into nearbyResultList
                    nearbyResultListFilter.add(nearbyResult);
                }
            }
            if (fragmentById instanceof ListViewFragment) {
                ((ListViewFragment) fragmentById).refreshRestaurants(nearbyResultListFilter);
            } else if (fragmentById instanceof MapViewFragment) {
                ((MapViewFragment) fragmentById).updateGoogleUi(nearbyResultListFilter);
            }
        });
    }

    private void configureAutoPredictions(Editable s) {
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        LatLngBounds latLngBounds = this.getLatLngBounds();
        RectangularBounds bounds = RectangularBounds.newInstance(latLngBounds.southwest, latLngBounds.northeast);

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationRestriction(bounds)
                .setCountry("fr")
                .setQuery(s.toString())
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            List<String> restaurantList = new ArrayList<>();

            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                if (prediction.getPlaceTypes().contains(Place.Type.RESTAURANT)) {

                    restaurantList.add(prediction.getPrimaryText(null).toString());

                }
            }
            adapter = new ArrayAdapter<>(this, R.layout.custom_textview, restaurantList);

            autoCompleteTextView.setAdapter(adapter);
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(Menu.NONE, R.id.search_icon, Menu.NONE, R.string.search_a_restaurant);
        menuItem.setIcon(R.drawable.searchicon);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setOnMenuItemClickListener(item -> {
            if (autoCompleteTextView.getVisibility() == View.INVISIBLE) {
                autoCompleteTextView.setVisibility(View.VISIBLE);
            } else {
                autoCompleteTextView.setVisibility(View.INVISIBLE);
            }
            return true;
        });
        return super.onPrepareOptionsMenu(menu);
    }
}