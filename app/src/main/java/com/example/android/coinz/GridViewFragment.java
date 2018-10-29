package com.example.android.coinz;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class GridViewFragment extends Fragment {
    private Context context;
    private GridListAdapter adapter;
    private ArrayList<HashMap<String, BitmapDrawable>> coins;
    private Button selectButton;

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
        loadGridView(view);
        onClickEvent(view);
    }

    private void loadGridView(View view) {
        GridView gridView = view.findViewById(R.id.grid_view);
        coins = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            BitmapDrawable shil = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.shil, null);
            HashMap<String, BitmapDrawable> coin = new HashMap<>();
            coin.put("100 SHIL VALUE", shil);
            coins.add(coin);
        }


        adapter = new GridListAdapter(context, coins);
        gridView.setAdapter(adapter);
    }

    private void onClickEvent(View view) {
        view.findViewById(R.id.show_button).setOnClickListener(view1 -> {
            SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
            //Check if item is selected or not via size
            if (selectedRows.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                //Loop to all the selected rows array
                for (int i = 0; i < selectedRows.size(); i++) {

                    //Check if selected rows have value i.e. checked item
                    if (selectedRows.valueAt(i)) {

                        //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                        HashMap<String, BitmapDrawable> selectedRowLabel = coins.get(selectedRows.keyAt(i));

                        //append the row label text
                        stringBuilder.append(selectedRowLabel).append("\n");
                    }
                }
                Toast.makeText(context, "Selected Rows\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            }

        });
        view.findViewById(R.id.delete_button).setOnClickListener(view12 -> {
            SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
            //Check if item is selected or not via size
            if (selectedRows.size() > 0) {
                //Loop to all the selected rows array
                for (int i = (selectedRows.size() - 1); i >= 0; i--) {

                    //Check if selected rows have value i.e. checked item
                    if (selectedRows.valueAt(i)) {

                        //remove the checked item
                        coins.remove(selectedRows.keyAt(i));
                    }
                }

                //notify the adapter and remove all checked selection
                adapter.removeSelection();
            }
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

}
