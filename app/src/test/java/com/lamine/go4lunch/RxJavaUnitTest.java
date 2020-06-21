package com.lamine.go4lunch;

import com.lamine.go4lunch.Models.Details.Details;
import com.lamine.go4lunch.Models.NearbySearch.Google;
import com.lamine.go4lunch.Utils.Go4LunchStreams;

import org.junit.BeforeClass;
import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RxJavaUnitTest {

    @BeforeClass
    public static void setupClass(){
        RxAndroidPlugins.setMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
    }

    @Test
    public void checkGooglePlacesHttpRequest() {
        Observable<Google> placesObservable = Go4LunchStreams.getInstance().streamFetchGooglePlaces("49.4938, 0.1077", 5000, "restaurant");
        TestObserver<Google> testObserver = new TestObserver<>();

        placesObservable.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();
        Google google = testObserver.values().get(0);
        assertTrue(google.getResults().size() > 0);
    }

    @Test
    public void checkGoogleDetailsHttpRequest(){

        Observable<Details> detailsObservable = Go4LunchStreams.getInstance().streamFetchGoogleDetailsInfo("ChIJ3520Oh8v4EcRih32gtlJsXc");
        TestObserver<Details> testObserver = new TestObserver<>();
        detailsObservable.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();
        Details details = testObserver.values().get(0);
        assertNotNull(details.getResult().getPlaceId());
    }


}