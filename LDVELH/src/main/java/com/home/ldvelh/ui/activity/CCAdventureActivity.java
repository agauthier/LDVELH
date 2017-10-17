package com.home.ldvelh.ui.activity;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;

import android.os.Bundle;
import android.widget.TextView;

public class CCAdventureActivity extends AdventureActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((TextView) findViewById(R.id.tutelaryGod)).setText(Property.TUTELARY_GOD.get().getValue().getNameResId());
	}
}
