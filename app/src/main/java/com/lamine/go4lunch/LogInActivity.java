package com.lamine.go4lunch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LogInActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    @Override
    public int getFragmentLayout() {
        return 0;
    }
}