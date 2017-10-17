package com.home.ldvelh.ui.inflater;

import com.home.ldvelh.model.item.ListItem;
import com.home.ldvelh.model.value.ListValueHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class RowArrayAdapter<T extends ListItem> extends ArrayAdapter<T> {

	private final int layoutResId;

	public RowArrayAdapter(Context context, int layoutResId, ListValueHolder<T> list) {
		super(context, layoutResId, list.getValue());
		this.layoutResId = layoutResId;
	}

	@Override public View getView(final int position, View row, ViewGroup parent) {
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResId, parent, false);
		}
		T item = getItem(position);
		item.initView(row);
		return row;
	}
}
