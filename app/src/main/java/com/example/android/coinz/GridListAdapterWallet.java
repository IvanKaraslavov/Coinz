package com.example.android.coinz;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GridListAdapterWallet extends BaseAdapter {
    private ArrayList<HashMap<String, BitmapDrawable>> coins;
    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;

    GridListAdapterWallet(Context context, ArrayList<HashMap<String, BitmapDrawable>> arrayList) {
        this.coins = arrayList;
        inflater = LayoutInflater.from(context);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return coins.size();
    }

    @Override
    public Object getItem(int i) {
        return coins.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(R.layout.grid_custom_row_layout_wallet, viewGroup, false);

            viewHolder.image = view.findViewById(R.id.label);
            viewHolder.description =  view.findViewById(R.id.description);
            viewHolder.checkBox = view.findViewById(R.id.checkbox);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        HashMap<String, BitmapDrawable> instance = coins.get(i);
        for (Map.Entry<String, BitmapDrawable> entry  : instance.entrySet()) {
            viewHolder.image.setImageDrawable(entry.getValue());
            viewHolder.checkBox.setChecked(mSelectedItemsIds.get(i));
            viewHolder.description.setText(entry.getKey());
        }

        viewHolder.checkBox.setOnClickListener(v -> checkCheckBox(i, !mSelectedItemsIds.get(i)));

        viewHolder.image.setOnClickListener(v -> checkCheckBox(i, !mSelectedItemsIds.get(i)));

        viewHolder.description.setOnClickListener(v -> checkCheckBox(i, !mSelectedItemsIds.get(i)));

        return view;
    }

    private class ViewHolder {
        private ImageView image;
        private TextView description;
        private CheckBox checkBox;
    }


    /**
     * Remove all checkbox Selection
     **/
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}
