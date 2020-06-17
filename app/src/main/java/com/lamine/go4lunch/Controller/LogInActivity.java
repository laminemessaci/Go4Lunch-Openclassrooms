package com.lamine.go4lunch.Controller;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.lamine.go4lunch.Models.Helper.User;
import com.lamine.go4lunch.Models.Helper.UserHelper;
import com.lamine.go4lunch.R;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LogInActivity extends BaseActivity {

    //FOR DATA
    // - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    // FOR DESIGN
    // - Get Coordinator Layout
    @BindView(R.id.mainActivity_CoordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private String TAG = "permission";
    private String message = "Permission granted";

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestLocationPermission();

        if (UserHelper.isCurrentUserLogged()) {
            this.startActivityIfLogged();
        }
    }

    // --------------------
    // ACTIONS
    // --------------------

    // Launch Google Sign-in
    @OnClick(R.id.mainActivity_google_login)
    public void onClickGoogleButton() {
        if (UserHelper.isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForGoogle();
        }
    }

    // Launch Facebook Sign-in
    @OnClick(R.id.mainActivity_facebook_login)
    public void onClickFacebookButton() {
        if (UserHelper.isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForFacebook();
        }
    }

    // Launch Login Sign-in
    @OnClick(R.id.mainActivity_login)
    public void onClickLoginButton() {
        if (UserHelper.isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForMailPassword();
        }
    }

    // Launch Login  Twitter Sign-in
    @OnClick(R.id.mainActivity_twitter_login)
    public void onClickTwitterButton() {
        if (UserHelper.isCurrentUserLogged()) {
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
        if (UserHelper.getCurrentUser() != null) {
            String urlPicture = (UserHelper.getCurrentUser().getPhotoUrl() != null) ? UserHelper.getCurrentUser().getPhotoUrl().toString() : null;
            String username = UserHelper.getCurrentUser().getDisplayName();
            String uid = UserHelper.getCurrentUser().getUid();

            UserHelper.getUser(UserHelper.getCurrentUser().getUid()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User currentUser = task.getResult().toObject(User.class);
                    if (currentUser != null && UserHelper.getCurrentUser().getUid().equals(currentUser.getUid())) {
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

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.coordinatorLayout, getResources().getString(R.string.connexion_success));
                this.createUserFireStore();

            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, getResources().getString(R.string.authentication_canceled));
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.coordinatorLayout, getResources().getString(R.string.no_network));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
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
        if (EasyPermissions.hasPermissions(this, perms)) {
            Log.i(TAG, message);
        } else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.location_permission), REQUEST_LOCATION_PERMISSION, perms);
        }
    }
}
