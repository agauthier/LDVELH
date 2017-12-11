package com.home.ldvelh.ui.inflater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.home.ldvelh.model.item.Item;
import com.home.ldvelh.ui.widget.list.CustomListViewItem;

import java.util.List;

public class RowArrayAdapter<T extends CustomListViewItem<? extends Item>> extends ArrayAdapter<T> {

    private final int layoutResId;

    public RowArrayAdapter(Context context, int layoutResId, List<T> list) {
        super(context, layoutResId, list);
        this.layoutResId = layoutResId;
    }

    @NonNull
    @Override
    public View getView(final int position, View row, @NonNull ViewGroup parent) {
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //noinspection ConstantConditions
            row = inflater.inflate(layoutResId, parent, false);
        }
        T item = getItem(position);
        if (item != null) {
             item.initView(row);
        }
        return row;
    }
}
