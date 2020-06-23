package com.lamine.go4lunch.Controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.lamine.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_setting) Toolbar toolbar;

    private Switch enableNotification; // Used to add a switch
    private LinearLayout settingsContainer; // LinearLayout that contain the switch
    private SharedPreferences sharedPreferences; // Used to get our shared preferences
    private SharedPreferences.Editor editor; // Used to get data from the preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        // Bind all the view
        enableNotification = findViewById(R.id.notification_switch);
        settingsContainer = findViewById(R.id.settings_activity_container);

        // Get our shared preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Update the state of the switch depending on the value get from the preferences
        if(getSwitchState()) enableNotification.setChecked(true);
        else enableNotification.setChecked(false);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
        });

        updateSwitchState();

    }

    // Method that save the state of the switch in a boolean and display a snackbar to warn the user
    private void updateSwitchState() {
        enableNotification.setOnCheckedChangeListener((compoundButton, switchState) -> {
            editor.putBoolean("switchState", switchState).commit();
            if(getSwitchState())
                Snackbar.make(settingsContainer, R.string.notification_is_enabled, Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(settingsContainer, R.string.notification_is_disabled, Snackbar.LENGTH_LONG).show();
        });
    }

    // Return true of false depending on the state of the switch
    private boolean getSwitchState() {
        return sharedPreferences.getBoolean("switchState", false);
    }
}
