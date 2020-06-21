package com.lamine.go4lunch;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lamine.go4lunch.Controller.MainScreenActivity;
import com.lamine.go4lunch.Models.Helper.UserHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.contrib.NavigationViewActions.navigateTo;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.lamine.go4lunch.Utils.Constants.GET_JOINED_RESTAURANT;
import static com.lamine.go4lunch.Utils.Constants.GET_JOINED_RESTAURANT_ID;
import static com.lamine.go4lunch.Utils.Constants.GET_RESTAURANT_VICINITY;
import static com.lamine.go4lunch.Utils.Constants.GET_USERNAME;
import static com.lamine.go4lunch.Utils.Constants.GET_USER_ID;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainScreenInstrumentedTest {

    private String currentUserJoinedRestaurant;
    private String restaurantVicinity;
    private String currentUserJoinedRestaurantId;
    private List<String> userList = new ArrayList<>();

    @Rule
    public IntentsTestRule<MainScreenActivity> mActivityRule = new IntentsTestRule<>(
            MainScreenActivity.class);

    @Test
    public void testBottomNavigationViewItemClick() {
        onView(withId(R.id.map_view)).perform(click())
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.list_view)).perform(click())
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.workmates)).perform(click())
                .check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testNavigationMenuItemClick() {
        onView(withId(R.id.first_screen_drawerlayout))
                .perform(DrawerActions.open());
        onView(withId(R.id.first_screen_drawerlayout))
                .check(matches(isOpen()));
        onView(withId(R.id.first_screen_navigation_view))
                .perform(navigateTo(R.id.settings));

        onView(isRoot()).perform(pressBack());

        onView(withId(R.id.first_screen_drawerlayout))
                .perform(DrawerActions.open());
        onView(withId(R.id.first_screen_drawerlayout))
                .check(matches(isOpen()));
        onView(withId(R.id.first_screen_navigation_view))
                .perform(navigateTo(R.id.logout));
    }

    @Test
    public void testRecyclerViewScroll() throws InterruptedException {
        onView(withId(R.id.list_view)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.listview_recyclerview)).perform(RecyclerViewActions.scrollToPosition(10));
    }

    @Test
    public void testSearchButtonBar() {
        onView(withId(R.id.search_icon)).perform(click());
        onView(withId(R.id.autoCompleteTextView)).perform(click());
        onView(withId(R.id.autoCompleteTextView)).perform(typeText("pizzatel"));
    }

    @Test
    public void checkFirebaseCurrentUserResult() {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        Task<Void> delay = source.getTask();

        UserHelper.getBookingRestaurant("HfS0osYsMffrKtBa37dAweIRQ8x1").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                currentUserJoinedRestaurant = task.getResult().getString(GET_JOINED_RESTAURANT);
                currentUserJoinedRestaurantId = task.getResult().getString(GET_JOINED_RESTAURANT_ID);
                restaurantVicinity = task.getResult().getString(GET_RESTAURANT_VICINITY);
                source.setResult(null);
            } else {
                source.setResult(null);
            }
        });
        try {
            Tasks.await(delay);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assertNotNull(currentUserJoinedRestaurant);
        assertNotNull(currentUserJoinedRestaurantId);
        assertNotNull(restaurantVicinity);
    }

    @Test
    public void checkFirebaseCoworkersResult() {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        Task<Void> delayTask = taskCompletionSource.getTask();

        UserHelper.getAllUsernames().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = UserHelper.getCurrentUser().getUid();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String coworkersJoinedRestaurant = documentSnapshot.getString(GET_JOINED_RESTAURANT_ID);
                    if (coworkersJoinedRestaurant != null) {
                        if (coworkersJoinedRestaurant.equals(currentUserJoinedRestaurantId)) {
                            String userId = documentSnapshot.getString(GET_USER_ID);
                            if (userId != null && !userId.equals(uid)) {
                                String coworkersName = documentSnapshot.getString(GET_USERNAME);
                                userList.add(coworkersName);
                            }
                        }
                    }
                }
                taskCompletionSource.setResult(null);
            } else {
                taskCompletionSource.setResult(null);
            }
        });
        try {
            Tasks.await(delayTask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(userList);
    }
}
