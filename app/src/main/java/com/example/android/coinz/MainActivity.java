package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationEngineListener,
        PermissionsListener,NavigationView.OnNavigationItemSelectedListener {

    private String tag = "MainActivity";
    private String downloadDate = ""; // Format: YYYY/MM/DD
    private final String preferencesFile = "MyPrefsFile"; // for storing preferences
    private static boolean fileDownloaded;

    static boolean testing = false;

    private MapView mapView;
    static MapboxMap map;

    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;

    static List<MarkerOptions> markers;
    static JSONObject wallet;
    static private Icon dolrIcon;
    static private Icon penyIcon;
    static private Icon quidIcon;
    static private Icon shilIcon;

    public static Icon getDolrIcon() {
        return dolrIcon;
    }

    public static void setDolrIcon(Icon dolrIcon) {
        MainActivity.dolrIcon = dolrIcon;
    }

    public static Icon getPenyIcon() {
        return penyIcon;
    }

    public static void setPenyIcon(Icon penyIcon) {
        MainActivity.penyIcon = penyIcon;
    }

    public static Icon getQuidIcon() {
        return quidIcon;
    }

    public static void setQuidIcon(Icon quidIcon) {
        MainActivity.quidIcon = quidIcon;
    }

    public static Icon getShilIcon() {
        return shilIcon;
    }

    public static void setShilIcon(Icon shilIcon) {
        MainActivity.shilIcon = shilIcon;
    }

    public static boolean getFileDownloaded() {
        return fileDownloaded;
    }

    public static void setFileDownloaded(boolean fileDownloaded) {
        MainActivity.fileDownloaded = fileDownloaded;
    }

    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Mapbox.getInstance(this,
                "pk.eyJ1IjoiaXZhbmthcmFzbGF2b3YiLCJhIjoiY2puMjNnMTYwMXk3cjNwbzFvbGcycXF2ZyJ9.KbmMkvKu4RCcqVBRJaTf_Q");
        mapView = findViewById(R.id.mapboxMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadData();
        // Open the menu
        ImageButton mapButton = findViewById(R.id.menu_button);
        mapButton.setOnClickListener(view -> {
            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists()) {
                        Log.d(tag, "DocumentSnapshot data: " + document.getData());
                        boolean newNotifications = (boolean) document.get("newNotifications");
                        if(!newNotifications) {
                            navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setIcon(R.drawable.notifications_icon);
                        } else {
                            navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setIconTintMode(null).setIcon(R.drawable.notifications_icon_true);
                        }
                    } else {
                        Log.d(tag, "No such document");
                    }
                } else {
                    Log.d(tag, "get failed with ", task.getException());
                }
            });
            loadData();
            try {
                loadWallet();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            drawer.openDrawer(Gravity.START);
        });

    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        if (mapboxMap == null) {
            Log.d(tag, "[onMapReady] mapBox is null");
        } else {
            map = mapboxMap;
            // Set user interface options
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            // Make location information available
            enableLocation();
            IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);

            // Display the coins
            BitmapDrawable dolr = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.dolr, null);
            assert dolr != null;
            Bitmap bitmap = dolr.getBitmap();
            Bitmap smallDolr = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            setDolrIcon(iconFactory.fromBitmap(smallDolr));

            BitmapDrawable peny = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.penny, null);
            assert peny != null;
            bitmap = peny.getBitmap();
            Bitmap smallPeny = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            setPenyIcon(iconFactory.fromBitmap(smallPeny));

            BitmapDrawable quid = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.quid, null);
            assert quid != null;
            bitmap = quid.getBitmap();
            Bitmap smallQuid = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            setQuidIcon(iconFactory.fromBitmap(smallQuid));

            BitmapDrawable shil = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.shil, null);
            assert shil != null;
            bitmap = shil.getBitmap();
            Bitmap smallShil = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            setShilIcon(iconFactory.fromBitmap(smallShil));

        }

        // Update the date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtf.format(now);
        if (!todayDate.equals(downloadDate) || CreateProfilePage.firstTimeUser ) {
            //Download the GeoJSON file
            fileDownloaded = false;
            downloadDate = todayDate;
            setFileDownloaded(false);
            DownloadFileTask task = new DownloadFileTask();
            String url = "http://homepages.inf.ed.ac.uk/stg/coinz/" + todayDate + "/coinzmap.geojson";
            task.execute(url);
            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            assert currentUser != null;
            int COINS_LEFT = 25;
            mDatabase.collection("users").document(currentUser.getUid())
                    .update("coinsLeft", COINS_LEFT);
            mDatabase.collection("users").document(currentUser.getUid())
                    .update("boosterBought", false);
            mDatabase.collection("users").document(currentUser.getUid())
                    .update("currCoinValue", 0);
            mDatabase.collection("users").document(currentUser.getUid())
                    .update("steps", 0);
        } else {
            //Load map from the downloaded file
            String geoJsonString;
            setFileDownloaded(true);
            try {
                FileInputStream fis = openFileInput("coinzmap.geojson");
                geoJsonString = readStream(fis);
                DownloadCompleteRunner.downloadComplete(geoJsonString);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Light.ttf");
        TextView customFontTextView = findViewById(R.id.hello);
        customFontTextView.setTypeface(typeface);

        TextView username = findViewById(R.id.usernameChange);
        username.setTypeface(typefaceBold);

    }
    @SuppressLint({"LogNotTimber", "SetTextI18n"})
    private void loadData() {
        // Update the avatar in the menu
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    Log.d(tag, "DocumentSnapshot data: " + document.getData());
                    TextView username = findViewById(R.id.usernameChange);
                    username.setText( Objects.requireNonNull(document.get("username")).toString());

                    ImageView avatar = findViewById(R.id.avatarImage);
                    long avatarNumber = (long) document.get("currentAvatar");
                    switch ((int) avatarNumber) {
                        case 1: avatar.setImageResource(R.drawable.rabbit);
                        break;
                        case 2: avatar.setImageResource(R.drawable.dog);
                        break;
                        case 3: avatar.setImageResource(R.drawable.cat);
                        break;
                        case 4: avatar.setImageResource(R.drawable.koala);
                        break;
                        default:  avatar.setImageResource(R.drawable.rabbit);
                        break;
                    }
                } else {
                    Log.d(tag, "No such document");
                    TextView username = findViewById(R.id.usernameChange);
                    username.setText("THERE");
                }
            } else {
                Log.d(tag, "get failed with ", task.getException());
            }
        });
    }

    @NonNull
    private String readStream(InputStream stream)
            throws IOException {
        // Read input from stream, build result as a string
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(stream),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        stream.close();
        return sb.toString();
    }

    @SuppressLint("LogNotTimber")
    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            Log.d(tag, "Permissions are granted");
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
            Log.d(tag, "Permissions are not granted");
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this)
                .obtainBestLocationEngineAvailable();
        locationEngine.setInterval(5000); // preferably every 5 seconds
        locationEngine.setFastestInterval(1000); // at most every second
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();
        Location lastLocation;
        if(testing) {
            Location location = new Location("");
            location.setLatitude(55.9427);
            location.setLongitude(-3.18751);
            lastLocation = location;
            onLocationChanged(location);
        } else {
            lastLocation = locationEngine.getLastLocation();
        }
        if (lastLocation != null) {
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressLint("LogNotTimber")
    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer() {
        if (mapView == null) {
            Log.d(tag, "mapView is null");
        } else {
            if (map == null) {
                Log.d(tag, "map is null");
            } else {
                LocationLayerPlugin locationLayerPlugin = new LocationLayerPlugin(mapView,
                        map, locationEngine);
                locationLayerPlugin.setLocationLayerEnabled(true);
                locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
                locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
            }
        }
    }

    private void setCameraPosition(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    @SuppressLint("LogNotTimber")
    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        Log.d(tag, "[onConnected] requesting location updates");
        locationEngine.requestLocationUpdates();
    }


    @SuppressLint("LogNotTimber")
    @Override
    public void onLocationChanged(Location location) {
            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null) {
            DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists()) {
                        Log.d(tag, "DocumentSnapshot data: " + document.getData());
                        if (location == null) {
                            Log.d(tag, "[onLocationChanged] location is null");
                        } else {
                            Log.d(tag, "[onLocationChanged] location is not null");
                            long steps = (long) document.get("steps");
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("steps", steps + 1);
                            setCameraPosition(location);
                            try {
                                FileInputStream fis = openFileInput("coinzmap.geojson");
                                JSONObject jsonObject = new JSONObject(readStream(fis));
                                getCoins(jsonObject, location);
                                loadWallet();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Log.d(tag, "No such document");
                    }
                } else {
                    Log.d(tag, "get failed with ", task.getException());
                }
            });
            }
    }

    @SuppressLint("LogNotTimber")
    private void getCoins(JSONObject jsonObject, Location userLocation) {
        //Update the map and the wallet when a coin is taken
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    boolean boosterBought = (boolean) document.get("boosterBought");
                    JSONObject rates = null;
                    try {
                        rates = new JSONObject(jsonObject.getString("rates"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray features = null;
                    try {
                        features = jsonObject.getJSONArray("features");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    assert features != null;
                    for (int i = 0; i < features.length(); i++) {
                        JSONObject obj = null;
                        try {
                            obj = features.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject geometry = null;
                        try {
                            assert obj != null;
                            geometry = obj.getJSONObject("geometry");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray coordinates = null;
                        try {
                            assert geometry != null;
                            coordinates = geometry.getJSONArray("coordinates");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        double lat = 0;
                        try {
                            assert coordinates != null;
                            lat = coordinates.getDouble(1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        double lng = 0;
                        try {
                            lng = coordinates.getDouble(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int metresArea = 25;
                        if(boosterBought) {
                            metresArea = 75;
                        }
                        if(distFrom(lat, lng, userLocation.getLatitude(), userLocation.getLongitude()) <= metresArea) {
                            try {
                                String coin = features.get(i).toString();
                                Pattern p = Pattern.compile(".*\"value\":\"(\\d+\\.\\d+).*");
                                Matcher m = p.matcher(coin);
                                double value = 0;
                                if (m.find()) {
                                    value = Double.parseDouble(m.group(1));
                                }
                                mDatabase.collection("users").document(currentUser.getUid())
                                        .update("currCoinValue", value);
                                saveInDB();
                                Intent intent = new Intent(this,QuestionsActivity.class);
                                startActivity(intent);
                                updateWallet(features.get(i), rates);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            features.remove(i);
                            MainActivity.markers.remove(i);
                            Marker marker = MainActivity.map.getMarkers().get(i);
                            MainActivity.map.removeMarker(marker);
                            Toast.makeText(MainActivity.this, "Coin added to wallet!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    jsonObject.remove("features");
                    JSONObject updateJson = null;
                    try {
                        updateJson = new JSONObject(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert updateJson != null;
                        updateJson.put("features", features);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateFile(updateJson.toString(), "coinzmap.geojson");
                } else {
                    Log.d(tag, "No such document");
                }
            } else {
                Log.d(tag, "get failed with ", task.getException());
            }
        });
    }

    private void updateWallet(Object coin, JSONObject rates) throws IOException, JSONException {
        FileInputStream file = openFileInput("walletCoins.geojson");
        JSONObject jsonObject = new JSONObject(readStream(file));
        JSONArray coins = jsonObject.getJSONArray("coins");
        coins.put(coin);
        jsonObject.put("rates", rates);
        jsonObject.put("coins", coins);
        updateFile(jsonObject.toString(), "walletCoins.geojson");
    }

    private void loadWallet() throws IOException, JSONException {
        FileInputStream file = openFileInput("walletCoins.geojson");
        wallet = new JSONObject(readStream(file));
    }

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
    //Find the distance between the user and a coin in metres
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (earthRadius * c);
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onExplanationNeeded(List<String>
                                            permissionsToExplain){
        Log.d(tag, "Permissions: " + permissionsToExplain.toString());
    }


    @SuppressLint("LogNotTimber")
    @Override
    public void onPermissionResult(boolean granted) {
        Log.d(tag, "[onPermissionResult] granted == " + granted);
        if (granted) {
            enableLocation();
        } else {
            // Open a dialogue with the user
            Toast.makeText(MainActivity.this, "We will use your location only when you are playing the game in order to know if you are close enough to the coins!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("LogNotTimber")
    @SuppressWarnings( {"MissingPermission"})
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        if(locationEngine != null){
            try {
                locationEngine.requestLocationUpdates();
            } catch(SecurityException ignored) {}
            locationEngine.addLocationEngineListener(this);
        }
        SharedPreferences settings = getSharedPreferences(preferencesFile, Context.MODE_PRIVATE);
        // use ”” as the default value (this might be the first time the app is run)
        downloadDate = settings.getString("lastDownloadDate", "");
        Log.d(tag,"[onStart] Recalled lastDownloadDate is ’"  + downloadDate + "’");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @SuppressLint("LogNotTimber")
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if(locationEngine != null){
            locationEngine.removeLocationEngineListener(this);
            locationEngine.removeLocationUpdates();
        }
        Log.d(tag,"[onStop] Storing lastDownloadDate of " + downloadDate);
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(preferencesFile, Context.MODE_PRIVATE);
        // We need an Editor object to make preference changes.
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lastDownloadDate", downloadDate);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        // Apply the edits!
        editor.apply();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void saveInDB() {
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        FileInputStream walletFile = null;
        FileInputStream mapFile = null;
        try {
            walletFile = openFileInput("walletCoins.geojson");
            mapFile = openFileInput("coinzmap.geojson");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            JSONObject wallet = new JSONObject(readStream(walletFile));
            JSONObject map = new JSONObject(readStream(mapFile));
            mDatabase.collection("users").document(currentUser.getUid())
                    .update("wallet", wallet.toString());
            mDatabase.collection("users").document(currentUser.getUid())
                    .update("map", map.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("LogNotTimber")
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.bank_icon) {
            Intent intent = new Intent(this,BankActivity.class);
            startActivity(intent);
        } else if (id == R.id.wallet_icon) {
            Intent intent = new Intent(this,WalletActivity.class);
            startActivity(intent);
        } else if (id == R.id.shop_icon) {
            Intent intent = new Intent(this,AvatarsActivity.class);
            startActivity(intent);
        } else if (id == R.id.boosters_icon) {
            Intent intent = new Intent(this,BoostersActivity.class);
            startActivity(intent);
        } else if (id == R.id.notifications_icon) {
            Intent intent = new Intent(this,NotificationsActivity.class);
            startActivity(intent);
        } else if (id == R.id.log_out) {
            SharedPreferences settings = getSharedPreferences(preferencesFile, Context.MODE_PRIVATE);
            // We need an Editor object to make preference changes.
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = settings.edit();
            editor.putString("lastDownloadDate", downloadDate);
            FileInputStream walletFile = null;
            FileInputStream mapFile = null;
            try {
                walletFile = openFileInput("walletCoins.geojson");
                mapFile = openFileInput("coinzmap.geojson");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                JSONObject wallet = new JSONObject(readStream(walletFile));
                JSONObject map = new JSONObject(readStream(mapFile));
                assert currentUser != null;
                mDatabase.collection("users").document(currentUser.getUid())
                        .update("wallet", wallet.toString());
                mDatabase.collection("users").document(currentUser.getUid())
                        .update("map", map.toString());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            FirebaseAuth.getInstance().signOut();
            openLoginPage();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openLoginPage() {
        Intent intent = new Intent(this,LoginPage.class);
        startActivity(intent);
        finish();
    }
}
