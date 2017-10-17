package com.home.ldvelh.ui.widget;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;

import android.content.Context;
import android.util.AttributeSet;

public class DFAttributeLuck extends DFAttribute {

	public DFAttributeLuck(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttribute(getResources().getString(R.string.luck), Property.LUCK.get());
	}
}
