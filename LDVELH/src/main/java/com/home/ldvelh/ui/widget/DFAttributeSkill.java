package com.home.ldvelh.ui.widget;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;

import android.content.Context;
import android.util.AttributeSet;

public class DFAttributeSkill extends DFAttribute {

	public DFAttributeSkill(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttribute(getResources().getString(R.string.skill), Property.SKILL.get());
	}
}
