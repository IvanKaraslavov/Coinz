package com.example.android.coinz;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class DownloadCompleteRunner {
    private static String result;

    public static void downloadComplete(String result) throws JSONException {
        DownloadCompleteRunner.result = result;
        JSONObject jsonObject = new JSONObject(getResult());
        getInformation(jsonObject);

        if(!MainActivity.getFileDownloaded()) {
            writeFile();
        }
    }

    public static void writeFile() {
        // Add the geojson text into a file in internal storage
        try {
            FileOutputStream file = getApplicationContext().openFileOutput("coinzmap.geojson", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(file);
            outputWriter.write(result);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getInformation(JSONObject jsonObject) throws JSONException {
        JSONObject rates = new JSONObject(jsonObject.getString("rates"));
        double shilRate = rates.getDouble("SHIL");
        double dolrRate = rates.getDouble("DOLR");
        double quidRate = rates.getDouble("QUID");
        double penyRate = rates.getDouble("PENY");

        JSONArray features = jsonObject.getJSONArray("features");
        MainActivity.markers = new ArrayList<>();
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

            Icon icon = null;
            switch (currency) {
                case "QUID":
                    icon = MainActivity.getQuidIcon();
                    break;
                case "PENY":
                    icon = MainActivity.getPenyIcon();
                    break;
                case "SHIL":
                    icon = MainActivity.getShilIcon();
                    break;
                case "DOLR":
                    icon = MainActivity.getDolrIcon();
                    break;
            }

            MarkerOptions marker = new MarkerOptions();
            marker.title(markerSymbol).snippet(currency).icon(icon).position(new LatLng(lat, lng));
            MainActivity.markers.add(marker);
            MainActivity.map.addMarker(marker);
        }
    }

    public static String getResult() {
        return result;
    }
}
