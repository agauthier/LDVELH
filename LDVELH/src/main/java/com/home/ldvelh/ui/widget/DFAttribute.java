package com.home.ldvelh.ui.widget;

import com.home.ldvelh.R;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

abstract class DFAttribute extends LinearLayout {

	public DFAttribute(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.widget_df_attribute, this);
	}

	final void initAttribute(String name, IntValueHolder value) {
		((TextView) findViewById(R.id.attributeName)).setText(name);
		CustomNumberPicker leftPicker = findViewById(R.id.numberPickerLeft);
		leftPicker.init(value, WatchType.VALUE);
		CustomNumberPicker rightPicker = findViewById(R.id.numberPickerRight);
		rightPicker.init(value, WatchType.MAX);
	}
}
