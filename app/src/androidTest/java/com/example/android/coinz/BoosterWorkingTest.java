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
public class BoosterWorkingTest {

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
        String testMap = "{\"type\":\"FeatureCollection\",\"date-generated\":\"Wed Dec 12 2018\",\"time-generated\":\"00:00\",\"approximate-time-remaining\":\"23:59\",\"rates\":{\"SHIL\":44.979436487169174,\"DOLR\":8.20347188277529,\"QUID\":49.06439350450738,\"PENY\":63.086742089435866},\"features\":[{\"type\":\"Feature\",\"properties\":{\"id\":\"eef2-7bb2-7872-5782-b24a-4d03\",\"value\":\"9.503901868210248\",\"currency\":\"DOLR\",\"marker-symbol\":\"9\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.186605242669754,55.943273761826816]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"cb15-0841-1e19-a219-29ce-a8e3\",\"value\":\"1.0399632229697509\",\"currency\":\"QUID\",\"marker-symbol\":\"1\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1912924376751173,55.94315037861326]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"5d21-728e-30aa-4c6b-1392-601f\",\"value\":\"1.44717229740162\",\"currency\":\"QUID\",\"marker-symbol\":\"1\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1922762720962155,55.943080491818115]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"0ecf-e2fb-b7be-177f-621e-d342\",\"value\":\"8.217260333025354\",\"currency\":\"QUID\",\"marker-symbol\":\"8\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.186547254880981,55.945347648649]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"1d44-0f72-cc68-09d4-34fa-065d\",\"value\":\"5.353914247902854\",\"currency\":\"DOLR\",\"marker-symbol\":\"5\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.186158939883976,55.9439929019247]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"3638-976b-d19b-fbda-e1d9-be63\",\"value\":\"0.009640295117823161\",\"currency\":\"DOLR\",\"marker-symbol\":\"0\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.192342873958182,55.942950629239526]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"b6fd-266f-0042-b4bb-66d3-b5b4\",\"value\":\"4.358212788227448\",\"currency\":\"QUID\",\"marker-symbol\":\"4\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1865928740866782,55.94616620949259]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"4468-bdcb-8f82-df83-1f6f-7493\",\"value\":\"3.421145403310779\",\"currency\":\"DOLR\",\"marker-symbol\":\"3\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1910068917408223,55.94342634464178]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"b095-10d6-6027-1beb-70a8-60d8\",\"value\":\"2.474909529126621\",\"currency\":\"SHIL\",\"marker-symbol\":\"2\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1882952742609274,55.94558017574499]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"8344-ce29-3bee-28bc-516c-c962\",\"value\":\"1.6254071540750104\",\"currency\":\"SHIL\",\"marker-symbol\":\"1\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1914378472429767,55.94345971642083]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"7e8a-b94c-c16b-d8f3-5afc-e58f\",\"value\":\"7.2449072510462535\",\"currency\":\"SHIL\",\"marker-symbol\":\"7\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.186204217634272,55.946216849454295]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"13f5-e15f-12e3-b35d-5fe3-eeef\",\"value\":\"0.08953277866518117\",\"currency\":\"QUID\",\"marker-symbol\":\"0\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.18992206716133,55.94418733394876]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"1144-7b7b-4381-8b09-3497-7fa4\",\"value\":\"4.414026636842281\",\"currency\":\"SHIL\",\"marker-symbol\":\"4\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.18837459949341,55.94306182252586]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"1cb3-0f28-cad4-f052-63aa-fa6c\",\"value\":\"3.228309419895389\",\"currency\":\"DOLR\",\"marker-symbol\":\"3\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1895745222391123,55.944200657263075]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"a426-81fa-b5b7-4ad6-92b2-4908\",\"value\":\"0.7679358091380595\",\"currency\":\"SHIL\",\"marker-symbol\":\"0\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.190090485742739,55.944983720917826]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"50b6-4257-0f98-065c-0e05-cc5c\",\"value\":\"2.0017282658201294\",\"currency\":\"SHIL\",\"marker-symbol\":\"2\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1892443146456824,55.94263748072145]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"34e1-95f1-af3a-3e47-3fe6-cbbe\",\"value\":\"4.196694451578557\",\"currency\":\"QUID\",\"marker-symbol\":\"4\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.189229911834665,55.945030249877405]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"58e6-2c68-e582-31bb-0f01-bfe6\",\"value\":\"7.949179400647967\",\"currency\":\"DOLR\",\"marker-symbol\":\"7\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.190267656159479,55.94282121757069]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"4f11-5bec-59b4-6bc9-8b79-73d5\",\"value\":\"1.6634964853500722\",\"currency\":\"SHIL\",\"marker-symbol\":\"1\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1897759533550567,55.943278776623416]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"5b05-a707-dadb-b874-b82e-a9b6\",\"value\":\"4.606654039716089\",\"currency\":\"QUID\",\"marker-symbol\":\"4\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1903124173641366,55.942708433319325]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"2326-6b3f-383b-5abe-9163-35a9\",\"value\":\"6.545372531568865\",\"currency\":\"PENY\",\"marker-symbol\":\"6\",\"marker-color\":\"#ff0000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1861502602817344,55.94302847616672]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"be3a-dbeb-d145-2cad-08b0-ab62\",\"value\":\"7.67430213631957\",\"currency\":\"QUID\",\"marker-symbol\":\"7\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.186422718847751,55.945212782659006]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"796e-eb51-9211-bd8a-25e9-c8b5\",\"value\":\"1.134626835192648\",\"currency\":\"QUID\",\"marker-symbol\":\"1\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1892194756628314,55.94300570129266]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"ff63-47d2-96d7-3fbe-d5ae-2025\",\"value\":\"9.437122618516398\",\"currency\":\"DOLR\",\"marker-symbol\":\"9\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1895979705954884,55.94548637879044]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"2ba9-602e-4ead-74a3-95f8-6272\",\"value\":\"7.079726962039845\",\"currency\":\"SHIL\",\"marker-symbol\":\"7\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1913536206189344,55.94477860943486]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"f959-00d5-2f5a-8a14-def6-c7f7\",\"value\":\"8.003742305914239\",\"currency\":\"SHIL\",\"marker-symbol\":\"8\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1891589371991422,55.94408590400856]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"87e1-573e-2ba5-6b20-4acf-894f\",\"value\":\"2.4981004996373946\",\"currency\":\"DOLR\",\"marker-symbol\":\"2\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1908082772835527,55.94391809376865]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"e371-9c9c-3b72-e410-771e-b3fa\",\"value\":\"1.0011699349180858\",\"currency\":\"QUID\",\"marker-symbol\":\"1\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1849010177620634,55.944981532388844]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"f6ff-ea7d-9ae4-d147-17b9-60f2\",\"value\":\"7.588303008781758\",\"currency\":\"DOLR\",\"marker-symbol\":\"7\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.187640101383171,55.94409914166299]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"2ed5-8286-699b-6459-7070-92aa\",\"value\":\"8.053913387274221\",\"currency\":\"SHIL\",\"marker-symbol\":\"8\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.18735604851022,55.944007779040724]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"f864-f766-9a4b-8b1b-c82c-298b\",\"value\":\"3.755394707591776\",\"currency\":\"DOLR\",\"marker-symbol\":\"3\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1898757327353753,55.94266801856751]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"51ce-c6e0-8eac-ef21-e29d-a9d6\",\"value\":\"5.786853182859262\",\"currency\":\"DOLR\",\"marker-symbol\":\"5\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1868876982984786,55.94364828423547]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"0b1f-d1a1-e716-7e9f-62ba-56c0\",\"value\":\"9.66109560178451\",\"currency\":\"SHIL\",\"marker-symbol\":\"9\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.188323013961239,55.94369767700556]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"50f5-1d80-07ae-943f-9d72-2dbd\",\"value\":\"9.973319260143054\",\"currency\":\"QUID\",\"marker-symbol\":\"9\",\"marker-color\":\"#ffdf00\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1857542454071277,55.943289680065874]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"0906-804c-d091-bd0d-bc40-3b4b\",\"value\":\"8.544156947439571\",\"currency\":\"SHIL\",\"marker-symbol\":\"8\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1911405458968227,55.94299365788565]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"0443-2ced-a958-a11a-f455-9ffd\",\"value\":\"8.394704857729867\",\"currency\":\"DOLR\",\"marker-symbol\":\"8\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.190778346243436,55.94318683109948]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"fb2f-f3ac-ea0c-de53-5d8c-cb85\",\"value\":\"9.146820361688743\",\"currency\":\"SHIL\",\"marker-symbol\":\"9\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.189579595153131,55.94299125306614]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"288f-1fc2-2f54-8b0a-beb1-060a\",\"value\":\"9.823717690552153\",\"currency\":\"SHIL\",\"marker-symbol\":\"9\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.185412698408174,55.944685175923475]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"eb4d-611c-157c-caf7-0caa-4027\",\"value\":\"2.536818321583161\",\"currency\":\"SHIL\",\"marker-symbol\":\"2\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.19078173417109,55.94397710170527]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"0cfc-17d9-277a-8fa3-d286-ee4d\",\"value\":\"3.9714931794372363\",\"currency\":\"SHIL\",\"marker-symbol\":\"3\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.187788083411718,55.9459013274\n" +
                "0599]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"7a06-9ea6-4fe8-ee3c-8169-5a2b\",\"value\":\"2.329728133127955\",\"currency\":\"DOLR\",\"marker-symbol\":\"2\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.192090390455271,55.94374205975749]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"2751-1381-dbc6-ba0e-f03e-e401\",\"value\":\"5.86142510065816\",\"currency\":\"PENY\",\"marker-symbol\":\"5\",\"marker-color\":\"#ff0000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1859000249991216,55.94295368857075]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"5eb9-38bd-00a5-3a9c-d23c-e502\",\"value\":\"9.90489023865268\",\"currency\":\"PENY\",\"marker-symbol\":\"9\",\"marker-color\":\"#ff0000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1851642094102557,55.94265433132791]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"a223-fb00-9874-2fc0-4607-79c2\",\"value\":\"6.176410871683789\",\"currency\":\"DOLR\",\"marker-symbol\":\"6\",\"marker-color\":\"#008000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1913338678344463,55.943214267801345]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"b70b-aff8-5cc7-8604-b874-5d29\",\"value\":\"0.08744562827100455\",\"currency\":\"PENY\",\"marker-symbol\":\"0\",\"marker-color\":\"#ff0000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.189685059791739,55.944373819275086]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"8bc8-8ef1-89e2-e76c-4f6b-9e62\",\"value\":\"0.7076157242124759\",\"currency\":\"SHIL\",\"marker-symbol\":\"0\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1923940181485633,55.94470684243036]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"b79b-6c43-8497-cefc-8b5d-8acc\",\"value\":\"5.244228838866673\",\"currency\":\"SHIL\",\"marker-symbol\":\"5\",\"marker-color\":\"#0000ff\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.189855726227529,55.942694523794295]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"6eda-e608-4211-b1ac-589a-6cfa\",\"value\":\"3.547036283270258\",\"currency\":\"PENY\",\"marker-symbol\":\"3\",\"marker-color\":\"#ff0000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1870531611861224,55.94491613430222]}},{\"type\":\"Feature\",\"properties\":{\"id\":\"87c2-4a77-2cbc-a520-4316-b770\",\"value\":\"6.162224512507636\",\"currency\":\"PENY\",\"marker-symbol\":\"6\",\"marker-color\":\"#ff0000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.1886897189862697,55.94270275088627]}}]}";
        String map = "coinzmap.geojson";
        updateFile(testMap, map);
        String testWallet = "{\"coins\":[{\"type\":\"Feature\",\"properties\":{\"id\":\"ba38-9034-ee55-1837-db94-6553\",\"value\":\"4.284585154319103\",\"currency\":\"PENY\",\"marker-symbol\":\"4\",\"marker-color\":\"#ff0000\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.187595958095121,55.9427383996925]}}],\"rates\":{\"SHIL\":44.979436487169174,\"DOLR\":8.20347188277529,\"QUID\":49.06439350450738,\"PENY\":63.086742089435866}}";
        String wallet = "walletCoins.geojson";
        updateFile(testWallet, wallet);
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        mDatabase.collection("users").document("IZZaY7F8kUdJ4EN3fNAI6dI8HZ52")
                .update("boosterBought", false);
        mDatabase.collection("users").document("IZZaY7F8kUdJ4EN3fNAI6dI8HZ52")
                .update("goldCoinsAmount", 3000.04);
        MainActivity.testing = true;
    }

    @Test
    public void boosterWorkingTest() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null || !currentUser.getUid().equals("IZZaY7F8kUdJ4EN3fNAI6dI8HZ52")) {
            // Added a sleep statement to match the app's execution delay.
            // The recommended way to handle such scenarios is to use Espresso idling resources:
            // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
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
            Thread.sleep(4050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(4050);
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

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        5),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(4050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.booster_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.exit_booster), withContentDescription("cross image"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(4050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction viewGroup = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.content),
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout.class),
                                        0)),
                        0),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

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
