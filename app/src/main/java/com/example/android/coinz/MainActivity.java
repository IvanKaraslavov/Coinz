package com.example.android.coinz;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
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
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationEngineListener,
        PermissionsListener {

    private String tag = "MainActivity";
    private MapView mapView;
    private MapboxMap map;
    private Map todayMap;

    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;


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
    }

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
            try {
                getJSON();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
            for (int i = 0; i < todayMap.getCoins().size(); i++) {
                Coin currCoin = todayMap.getCoins().get(i);
                MarkerOptions marker = new MarkerOptions();
                marker.title(currCoin.getMarkerSymbol());
                marker.snippet(currCoin.getCurrency());
                marker.position(new LatLng(currCoin.getLat(), currCoin.getLng()));
                BitmapDrawable bitmapDraw = null;
                switch (currCoin.getCurrency()) {
                    case "QUID":
                        bitmapDraw = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.quid, null);
                        break;
                    case "PENY":
                        bitmapDraw = (BitmapDrawable)ResourcesCompat.getDrawable(getResources(), R.drawable.penny, null);
                        break;
                    case "SHIL":
                        bitmapDraw = (BitmapDrawable)ResourcesCompat.getDrawable(getResources(), R.drawable.shil, null);
                        break;
                    case "DOLR":
                        bitmapDraw = (BitmapDrawable)ResourcesCompat.getDrawable(getResources(), R.drawable.dolr, null);
                        break;
                }
                Bitmap bitmap = bitmapDraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                marker.icon(iconFactory.fromBitmap(smallMarker));
                mapboxMap.addMarker(marker);
            }
        }
    }

    private void getJSON() throws JSONException {
        String jsonString;
        try {
            InputStream is = getAssets().open("coinzmap.geojson");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonString);
            String date = jsonObject.getString("date-generated");
            JSONObject rates = new JSONObject(jsonObject.getString("rates"));

            double shilRate = rates.getDouble("SHIL");
            double dolrRate = rates.getDouble("DOLR");
            double quidRate = rates.getDouble("QUID");
            double penyRate = rates.getDouble("PENY");
            todayMap = new Map(date, shilRate, dolrRate, quidRate, penyRate);
            todayMap.getInformation(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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


    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        Log.d(tag, "[onConnected] requesting location updates");
        locationEngine.requestLocationUpdates();
    }


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

    @Override
    public void onExplanationNeeded(List<String>
                                            permissionsToExplain){
        Log.d(tag, "Permissions: " + permissionsToExplain.toString());
    }


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
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }
}
