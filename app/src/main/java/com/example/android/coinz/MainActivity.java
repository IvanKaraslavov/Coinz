package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationEngineListener,
        PermissionsListener,NavigationView.OnNavigationItemSelectedListener {

    private String tag = "MainActivity";
    private String downloadDate = ""; // Format: YYYY/MM/DD
    private final String preferencesFile = "MyPrefsFile"; // for storing preferences
    private static boolean fileDownloaded;

    private MapView mapView;
    static MapboxMap map;

    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationLayerPlugin;
    private LocationEngine locationEngine;
    private Location originLocation;

    static List<MarkerOptions> markers;
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

        Button mapButton = findViewById(R.id.button);
        mapButton.setOnClickListener(view -> drawer.openDrawer(Gravity.START));

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        TextView customFontTextView = findViewById(R.id.hello);
        customFontTextView.setTypeface(typeface);
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

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtf.format(now);
        if (!todayDate.equals(downloadDate)) {
            //Download the GeoJSON file
            downloadDate = todayDate;
            setFileDownloaded(false);
            DownloadFileTask task = new DownloadFileTask();
            String url = "http://homepages.inf.ed.ac.uk/stg/coinz/" + todayDate + "/coinzmap.geojson";
            task.execute(url);
        }
        else {
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
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
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
                locationLayerPlugin = new LocationLayerPlugin(mapView,
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
        if (location == null) {
            Log.d(tag, "[onLocationChanged] location is null");
        } else {
            Log.d(tag, "[onLocationChanged] location is not null");
            originLocation = location;
            setCameraPosition(location);
        }
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
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStart();
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
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStart();
        }
        Log.d(tag,"[onStop] Storing lastDownloadDate of " + downloadDate);
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(preferencesFile, Context.MODE_PRIVATE);
        // We need an Editor object to make preference changes.
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lastDownloadDate", downloadDate);
        // Apply the edits!
        editor.apply();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
