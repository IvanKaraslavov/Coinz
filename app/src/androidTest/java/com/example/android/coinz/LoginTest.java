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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public GrantPermissionRule permissionsRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE);
    @Rule
    public ActivityTestRule<LoginPage> mActivityTestRule = new ActivityTestRule<>(LoginPage.class);



    @Test
    public void loginTest() {
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


        ViewInteraction textView = onView(
                allOf(withText("Test"),
                        isDisplayed()));
        textView.check(matches(withText("Test")));

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
