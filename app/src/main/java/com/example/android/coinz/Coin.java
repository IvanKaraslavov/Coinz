package com.example.android.coinz;

public class Coin {
    private String id;
    private double value;
    private String currency;
    private String markerSymbol;

    private double lat;
    private double lng;

    public Coin(String id, double value, String currency, String markerSymbol, double lat, double lng) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.markerSymbol = markerSymbol;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMarkerSymbol() {
        return markerSymbol;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
