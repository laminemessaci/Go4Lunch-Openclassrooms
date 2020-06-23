package com.lamine.go4lunch.Controller;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.lamine.go4lunch.Models.Helper.User;
import com.lamine.go4lunch.Models.Helper.UserHelper;
import com.lamine.go4lunch.R;
import com.lamine.go4lunch.Utils.Prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LogInActivity extends BaseActivity {

    // - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    // FOR DESIGN
    @BindView(R.id.spinner) Spinner spinner;
    @BindView(R.id.mainActivity_facebook_login) Button buttonFaceBook;
    @BindView(R.id.mainActivity_google_login) Button buttonGoogle;
    @BindView(R.id.mainActivity_login) Button buttonEmail;
    @BindView(R.id.mainActivity_twitter_login) Button twitterLoginButton;
    // - Get Coordinator Layout
    @BindView(R.id.mainActivity_CoordinatorLayout) CoordinatorLayout coordinatorLayout;
    //FOR DATA
    private Prefs prefs;
    private String currentLanguage = "en", currentLang;
    private String TAG = "permission";
    private String message = "Permission granted";

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = Prefs.get(this);
        checkIfLanguageIsOk();

        requestLocationPermission();

        if(UserHelper.isCurrentUserLogged()) {
            this.startActivityIfLogged();
        }
    }

    // --------------------
    // ACTIONS
    // --------------------

    // Launch Google Sign-in
    @OnClick(R.id.mainActivity_google_login)
    public void onClickGoogleButton() {
        if(UserHelper.isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForGoogle();
        }
    }

    // Launch Facebook Sign-in
    @OnClick(R.id.mainActivity_facebook_login)
    public void onClickFacebookButton() {
        if(UserHelper.isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForFacebook();
        }
    }

    // Launch Login Sign-in
    @OnClick(R.id.mainActivity_login)
    public void onClickLoginButton() {
        if(UserHelper.isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForMailPassword();

        }
    }

    // Launch Login  Twitter Sign-in
    @OnClick(R.id.mainActivity_twitter_login)
    public void onClickTwitterButton() {
        if(UserHelper.isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForTwitter();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // --------------------
    // NAVIGATION
    // --------------------

    // - Launch Sign-In Activity for Google
    private void startSignInActivityForGoogle() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build())) //GOOGLE
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN);
    }

    // - Launch Sign-In Activity for Facebook
    private void startSignInActivityForFacebook() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build())) //GOOGLE
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN);
    }

    // - Launch Sign-In Activity for mail/password
    private void startSignInActivityForMailPassword() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, false)
                        .build(), RC_SIGN_IN);
    }

    // - Launch Sign-In Activity for Twitter
    private void startSignInActivityForTwitter() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.TwitterBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN);
    }

    // --------------------
    // UI
    // --------------------

    // - Show Snack Bar with a message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    // --------------------
    // UTILS
    // --------------------

    private void startActivityIfLogged() {
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
        finish();
    }

    private void createUserFireStore() {
        if(UserHelper.getCurrentUser() != null) {
            String urlPicture = (UserHelper.getCurrentUser().getPhotoUrl() != null) ? UserHelper.getCurrentUser().getPhotoUrl().toString() : null;
            String username = UserHelper.getCurrentUser().getDisplayName();
            String uid = UserHelper.getCurrentUser().getUid();

            UserHelper.getUser(UserHelper.getCurrentUser().getUid()).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    User currentUser = task.getResult().toObject(User.class);
                    if(currentUser != null && UserHelper.getCurrentUser().getUid().equals(currentUser.getUid())) {
                        UserHelper.updateUser(uid, username, urlPicture);
                        this.startActivityIfLogged();
                    } else {
                        UserHelper.createUser(uid, username, urlPicture).addOnCompleteListener(task1 -> {
                            this.startActivityIfLogged();
                        })
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_during_creating), Toast.LENGTH_SHORT).show());
                    }

                }
            });

        }

    }

    // - Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if(requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.coordinatorLayout, getResources().getString(R.string.connexion_success));
                this.createUserFireStore();

            } else { // ERRORS
                if(response == null) {
                    showSnackBar(this.coordinatorLayout, getResources().getString(R.string.authentication_canceled));
                } else if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.coordinatorLayout, getResources().getString(R.string.no_network));
                } else if(response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.coordinatorLayout, getResources().getString(R.string.error_restaurant));
                }
            }
        }
    }

    // -------------------
    // PERMISSIONS
    // -------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Log.i(TAG, message);
        } else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.location_permission), REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    // -------------------
    // PREFERENCES USER LANGUAGE
    // -------------------
    // Check language prefs and purpose choice if null
    private void checkIfLanguageIsOk() {
        String locale = prefs.getLanguage();
        if(locale != null && !locale.isEmpty()) {
            setLocale(locale);
            spinner.setVisibility(View.GONE);
        } else {
            configureSpinner();
        }
    }

    // Spinner configuration to select language
    private void configureSpinner() {
        spinner.setVisibility(View.VISIBLE);
        hideButtons();
        currentLanguage = getIntent().getStringExtra(currentLang);
        List<String> list = new ArrayList<>();

        list.add(getResources().getString(R.string.select));
        list.add(getResources().getString(R.string.en));
        list.add(getResources().getString(R.string.fr));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch(position) {
                    case 0:
                        break;
                    case 1:
                        setLocale("en");
                        prefs.storeLanguageChoice("en");
                        break;
                    case 2:
                        setLocale("fr");
                        prefs.storeLanguageChoice("fr");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    // Hide connexion buttons
    private void hideButtons() {
        buttonEmail.setVisibility(View.GONE);
        buttonFaceBook.setVisibility(View.GONE);
        buttonGoogle.setVisibility(View.GONE);
        twitterLoginButton.setVisibility(View.GONE);
    }


    // Set new language to apply
    public void setLocale(String localeName) {
        currentLanguage = getIntent().getStringExtra(currentLang);
        if(!localeName.equals(currentLanguage)) {
            Locale myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, LogInActivity.class);
            refresh.putExtra(currentLang, localeName);
            refresh.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(refresh);
        }
        showButtons();
    }

    // Show connexion buttons
    private void showButtons() {
        buttonEmail.setVisibility(View.VISIBLE);
        buttonFaceBook.setVisibility(View.VISIBLE);
        buttonGoogle.setVisibility(View.VISIBLE);
        twitterLoginButton.setVisibility(View.VISIBLE);
    }

}
