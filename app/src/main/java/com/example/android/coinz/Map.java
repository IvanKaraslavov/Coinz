package com.example.android.coinz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Map {
    private ArrayList<Coin> coins;
    private String date;
    private double shilRate;
    private double dolrRate;
    private double quidRate;
    private double penyRate;

    public Map(String date, double shilRate, double dolrRate, double quidRate, double penyRate) {
        this.date = date;
        this.shilRate = shilRate;
        this.dolrRate = dolrRate;
        this.quidRate = quidRate;
        this.penyRate = penyRate;
        this.coins = new ArrayList<>();
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public String getDate() {
        return date;
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

    public void getInformation(JSONObject jsonObject) throws JSONException {
        JSONArray features = jsonObject.getJSONArray("features");

        for (int i = 0; i < features.length(); i++) {
            JSONObject obj = features.getJSONObject(i);
            JSONObject properties = obj.getJSONObject("properties");
            String id = properties.getString("id");
            double value = properties.getDouble("value");
            String currency = properties.getString("currency");
            String markerSymbol = properties.getString("marker-symbol");
            JSONObject geometry = obj.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            double lat = coordinates.getDouble(1);
            double lng = coordinates.getDouble(0);
            Coin coin = new Coin(id,value,currency,markerSymbol,lat,lng);
            this.coins.add(coin);
        }
    }
}
