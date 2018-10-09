package com.example.android.coinz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;


public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,
                "pk.eyJ1IjoiaXZhbmthcmFzbGF2b3YiLCJhIjoiY2puMjNnMTYwMXk3cjNwbzFvbGcycXF2ZyJ9.KbmMkvKu4RCcqVBRJaTf_Q");
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
    }
}
