package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.example.android.coinz.MainActivity.wallet;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class GridViewFragment extends Fragment {
    private Context context;
    private GridListAdapter adapter;
    private ArrayList<HashMap<String, BitmapDrawable>> coins;
    private Button selectButton;
    private String tag = "GridView";

    public GridViewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grid_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectButton = view.findViewById(R.id.select_button);
        try {
            loadGridView(view);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onClickEvent(view);
    }

    @SuppressLint("DefaultLocale")
    private void loadGridView(View view) throws JSONException {
        GridView gridView = view.findViewById(R.id.grid_view);
        coins = new ArrayList<>();
        JSONObject wallet = MainActivity.wallet;
        JSONArray walletCoins;
        try {
            walletCoins = wallet.getJSONArray("coins");
        } catch (Exception e) {
            walletCoins = new JSONArray();
        }
        for (int i = 0; i < walletCoins.length(); i++) {
            BitmapDrawable icon = null;
            JSONObject obj = walletCoins.getJSONObject(i);
            JSONObject properties = obj.getJSONObject("properties");
            double value = properties.getDouble("value");
            String currency = properties.getString("currency");
            switch (currency) {
                case "QUID":
                    icon = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.quid, null);
                    break;
                case "PENY":
                    icon = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.penny, null);
                    break;
                case "SHIL":
                    icon = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.shil, null);
                    break;
                case "DOLR":
                    icon = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.dolr, null);
                    break;
            }
            HashMap<String, BitmapDrawable> coin = new HashMap<>();
            coin.put(currency + ": " + String.format("%.5f", value), icon);
            coins.add(coin);
        }
        adapter = new GridListAdapter(context, coins);
        gridView.setAdapter(adapter);
    }

    @SuppressLint("LogNotTimber")
    private void onClickEvent(View view) {
        view.findViewById(R.id.transfer_button).setOnClickListener(view1 -> {
            SparseBooleanArray selectedRows = adapter.getSelectedIds(); //Get the selected ids from adapter
            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists()) {
                        Log.d("GridView", "DocumentSnapshot data: " + document.getData());
                         String str = Objects.requireNonNull(document.get("coinsLeft")).toString();
                         int coinsLeft = Integer.parseInt(str);
                         if(coinsLeft >= selectedRows.size()) {
                             mDatabase.collection("users").document(currentUser.getUid())
                                     .update("coinsLeft", (coinsLeft - selectedRows.size()));

                             JSONObject rates;
                             double shilRate = 0;
                             double dolrRate = 0;
                             double quidRate = 0;
                             double penyRate = 0;
                             try {
                                 rates = new JSONObject(wallet.getString("rates"));
                                 shilRate = rates.getDouble("SHIL");
                                 dolrRate = rates.getDouble("DOLR");
                                 quidRate = rates.getDouble("QUID");
                                 penyRate = rates.getDouble("PENY");
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }
                             //Check if item is selected or not via size
                             if (selectedRows.size() > 0) {
                                 //Loop to all the selected rows array
                                 for (int i = 0; i < selectedRows.size(); i++) {

                                     //Check if selected rows have value i.e. checked item
                                     if (selectedRows.valueAt(i)) {

                                         //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                                         HashMap<String, BitmapDrawable> selectedRowLabel = coins.get(selectedRows.keyAt(i));
                                         for(Map.Entry<String, BitmapDrawable> entry : selectedRowLabel.entrySet()) {
                                             String key = entry.getKey();
                                             Pattern p = Pattern.compile("([A-Za-z]+)+: (\\d+.\\d+)");
                                             Matcher m = p.matcher(key);
                                             double value = 0;
                                             String currency = null;
                                             double currencyRate = 0;
                                             if (m.find()) {
                                                 currency = m.group(1);
                                                 value = Double.parseDouble(m.group(2));
                                             }
                                             assert currency != null;
                                             switch (currency) {
                                                 case "QUID":
                                                     currencyRate = quidRate;
                                                     break;
                                                 case "PENY":
                                                     currencyRate = penyRate;
                                                     break;
                                                 case "SHIL":
                                                     currencyRate = shilRate;
                                                     break;
                                                 case "DOLR":
                                                     currencyRate = dolrRate;
                                                     break;
                                             }
                                             double finalValue = value;
                                             double finalCurrencyRate = currencyRate;
                                             docRef.get().addOnCompleteListener(task_coins -> {
                                                 if (task_coins.isSuccessful()) {
                                                     DocumentSnapshot document_coins = task_coins.getResult();
                                                     if (Objects.requireNonNull(document_coins).exists()) {
                                                         Log.d("GridView", "DocumentSnapshot data: " + document_coins.getData());
                                                         double goldCoinsValue = Double.parseDouble(Objects.requireNonNull(document_coins.get("goldCoinsAmount")).toString());
                                                         mDatabase.collection("users").document(currentUser.getUid())
                                                                 .update("goldCoinsAmount", goldCoinsValue + finalValue * finalCurrencyRate);
                                                     } else {
                                                         Log.d("GridView", "No such document");
                                                     }
                                                 } else {
                                                     Log.d("GridView", "get failed with ", task_coins.getException());
                                                 }
                                             });
                                         }
                                     }
                                 }
                                 JSONObject wallet = MainActivity.wallet;
                                 JSONArray walletCoins;
                                 try {
                                     walletCoins = wallet.getJSONArray("coins");
                                 } catch (Exception e) {
                                     walletCoins = new JSONArray();
                                 }
                                 for (int i = (selectedRows.size() - 1); i >= 0; i--) {
                                     //Check if selected rows have value i.e. checked item
                                     if (selectedRows.valueAt(i)) {
                                         //remove the checked item
                                         coins.remove(selectedRows.keyAt(i));
                                         walletCoins.remove(selectedRows.keyAt(i));
                                     }
                                 }
                                 //notify the adapter and remove all checked selection
                                 adapter.removeSelection();
                                 try {
                                     wallet.remove("coins");
                                     JSONObject updateJson = new JSONObject(wallet.toString());
                                     updateJson.put("coins", walletCoins);
                                     updateFile(updateJson.toString());
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                             updateLayout();
                         } else {
                             Toast.makeText(getActivity(), "You exceeded your coins transfer limit!",
                                     Toast.LENGTH_SHORT).show();
                         }
                    } else {
                        Log.d("GridView", "No such document");
                    }
                } else {
                    Log.d("GridView", "get failed with ", task.getException());
                }
            });

        });
        selectButton.setOnClickListener(view13 -> {
            //Check the current text of Select Button
            if (selectButton.getText().toString().equals(getResources().getString(R.string.select_all))) {

                //If Text is Select All then loop to all array List items and check all of them
                for (int i = 0; i < coins.size(); i++)
                    adapter.checkCheckBox(i, true);

                //After checking all items change button text
                selectButton.setText(getResources().getString(R.string.deselect_all));
            } else {
                //If button text is Deselect All remove check from all items
                adapter.removeSelection();

                //After checking all items change button text
                selectButton.setText(getResources().getString(R.string.select_all));
            }
        });

    }

    private void updateFile(String result) {
        // Add the geojson text into a file in internal storage
        try {
            FileOutputStream file = getApplicationContext().openFileOutput("walletCoins.geojson", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(file);
            outputWriter.write(result);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LogNotTimber")
    private void updateLayout() {
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    Log.d(tag, "DocumentSnapshot data: " + document.getData());
                    TextView coinsNumber= Objects.requireNonNull(getActivity()).findViewById(R.id.coins_number);
                    coinsNumber.setText( Objects.requireNonNull(document.get("coinsLeft")).toString());
                } else {
                    Log.d(tag, "No such document");
                }
            } else {
                Log.d(tag, "get failed with ", task.getException());
            }
        });
    }

}
