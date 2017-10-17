package com.home.ldvelh.ui.widget;

import com.home.ldvelh.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class CCWounds extends LinearLayout {

	public CCWounds(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.widget_cc_wounds, this);
	}
}
