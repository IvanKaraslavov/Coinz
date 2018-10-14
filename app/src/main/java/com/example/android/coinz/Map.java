package com.example.android.coinz;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private ArrayList<Coin> coins;
    private double shilRate;
    private double dolrRate;
    private double quidRate;
    private double penyRate;

    public Map(double shilRate, double dolrRate, double quidRate, double penyRate) {
        this.shilRate = shilRate;
        this.dolrRate = dolrRate;
        this.quidRate = quidRate;
        this.penyRate = penyRate;
        this.coins = new ArrayList<>();
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public double getShilRate() {
        return shilRate;
    }

    public double getDolrRate() {
        return dolrRate;
    }

    public double getQuidRate() {
        return quidRate;
    }

    public double getPenyRate() {
        return penyRate;
    }


}
