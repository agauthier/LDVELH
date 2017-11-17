package com.home.ldvelh.ui.inflater;

import android.view.ViewGroup;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.activity.AdventureActivity;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

public enum FreeAreaInflaterDF10 implements FreeAreaInflater {
	INSTANCE;

	@Override public void inflate(AdventureActivity activity, ViewGroup freeArea) {
		activity.getLayoutInflater().inflate(R.layout.widget_fear_score, freeArea);
		CustomNumberPicker fearPicker = activity.findViewById(R.id.numberPickerFear);
		fearPicker.init(Property.FEAR.get(), WatchType.VALUE);
		TextView maxFearTextView = freeArea.findViewById(R.id.maxFear);
		maxFearTextView.setText(String.format(Utils.getString(R.string.df10_maxFearEquals), Property.FEAR.get().getMax()));
	}

	@Override public void resume() {}

	@Override public void pause() {}
}
