package com.example.android.coinz;


import android.Manifest;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class CollectCoinsTest {

    @Rule
    public ActivityTestRule<LoginPage> mActivityTestRule = new ActivityTestRule<>(LoginPage.class);
    @Rule
    public GrantPermissionRule permissionsRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE);

    private void updateFile(String result, String fileName) {
        // Add the geojson text into a file in internal storage
        try {
            FileOutputStream file = getApplicationContext().openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(file);
            outputWriter.write(result);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Before
    public void beforeTest() {
        String testMap = "{\"type\":\"FeatureCollection\",\"date-generated\":\"Wed Dec 12 2018\",\"time-generated\":\"00:00\",\"approximate-time-remaining\":\"23:59\",\"rates\":{\"SHIL\":44.979436487169174,\"DOLR\":8.20347188277529,\"QUID\":49.06439350450738,\"PENY\":63.086742089435866},\"features\":[]}";

        String map = "coinzmap.geojson";
        updateFile(testMap, map);
        String resultWallet = "{\"coins\":[{\"type\":\"Feature\",\"properties\":{\"id\":\"ba38-9034-ee55-1837-db94-6553\",\"value\":\"4.284585154319103\",\"currency\":\"PENY\",\"marker-symbol\":\"4\",\"marker-color\":\"#ff0000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.187595958095121,55.9427383996925]}}],\"rates\":{\"SHIL\":44.979436487169174,\"DOLR\":8.20347188277529,\"QUID\":49.06439350450738,\"PENY\":63.086742089435866}}";
        String wallet = "walletCoins.geojson";
        updateFile(resultWallet, wallet);
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        mDatabase.collection("users").document("IZZaY7F8kUdJ4EN3fNAI6dI8HZ52")
                .update("goldCoinsAmount", 0.0000001);
        MainActivity.testing = true;
    }

    @Test
    public void collectCoinsTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null || !currentUser.getUid().equals("IZZaY7F8kUdJ4EN3fNAI6dI8HZ52")) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.emailField),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                            1),
                                    3),
                            isDisplayed()));
            appCompatEditText.perform(replaceText("test@gmail.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.emailField), withText("test@gmail.com"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                            1),
                                    3),
                            isDisplayed()));
            appCompatEditText2.perform(pressImeActionButton());

            ViewInteraction appCompatEditText3 = onView(
                    allOf(withId(R.id.passwordField),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                            1),
                                    2),
                            isDisplayed()));
            appCompatEditText3.perform(replaceText("123456"), closeSoftKeyboard());

            ViewInteraction appCompatEditText4 = onView(
                    allOf(withId(R.id.passwordField), withText("123456"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                            1),
                                    2),
                            isDisplayed()));
            appCompatEditText4.perform(pressImeActionButton());

            ViewInteraction appCompatImageButton = onView(
                    allOf(withId(R.id.loginButton), withContentDescription("loginButton"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                            1),
                                    5),
                            isDisplayed()));
            appCompatImageButton.perform(click());
        }

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.menu_button), withContentDescription("menu_icon"),
                        childAtPosition(
                                allOf(withId(R.id.mapboxMapView), withContentDescription("Showing a Map created with Mapbox. Scroll by dragging two fingers. Zoom by pinching two fingers."),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        3),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction frameLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.grid_view),
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                                        0)),
                        0),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
