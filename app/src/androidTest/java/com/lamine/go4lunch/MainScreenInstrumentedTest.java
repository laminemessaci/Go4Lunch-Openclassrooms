package com.lamine.go4lunch;

import android.content.Context;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.lamine.go4lunch.Controller.MainScreenActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainScreenInstrumentedTest {

    private String currentUserJoinedRestaurant;
    private String restaurantVicinity;
    private String getCurrentUserJoinedRestaurantId;
    private List<String> userList = new ArrayList<>();

    @Rule
    public IntentsTestRule<MainScreenActivity>mActivityRule = new IntentsTestRule<>(MainScreenActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.lamine.go4lunch", appContext.getPackageName());
    }
}