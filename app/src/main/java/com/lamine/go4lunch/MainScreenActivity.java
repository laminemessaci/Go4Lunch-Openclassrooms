package com.lamine.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import butterknife.BindView;

public class MainScreenActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    // FOR DESIGN
    @BindView(R.id.first_screen_toolbar) Toolbar toolbar;
    @BindView(R.id.first_screen_drawerlayout) DrawerLayout drawerLayout;
    @BindView(R.id.first_screen_bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.first_screen_navigation_view) NavigationView navigationView;
    private ImageView profileImageView;
    private TextView emailTextView;
    private TextView nameTextView;
    private AutoCompleteTextView autoCompleteTextView;

    // FOR DATA
    private static final int SIGN_OUT_TASK = 10;
    public static final String ID = "ID";
    private static final String GET_RESTAURANT_ID = "restaurantId";

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_login;
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

    }
    // Handle Bottom Navigation View Click
    private void configureBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            int id = menuItem.getItemId();

            // Set current location in the ViewPager to handle the position of the fragments
            switch (id) {
                case R.id.list_view:

                    cleanAutoCompleteTextView();
                    break;
                case R.id.workmates:

                    cleanAutoCompleteTextView();
                    break;
                default:

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}