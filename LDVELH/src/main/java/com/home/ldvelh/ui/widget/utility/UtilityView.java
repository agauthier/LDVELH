package com.home.ldvelh.ui.widget.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.home.ldvelh.R;

public abstract class UtilityView extends RelativeLayout {

    public UtilityView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initLayout() {
        int padding = getResources().getDimensionPixelSize(R.dimen.default_padding);
        setPadding(padding, padding, padding, padding);
    }
}
