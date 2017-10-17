package com.home.ldvelh.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.home.ldvelh.R;

public class ThumbnailButton extends AppCompatImageButton {

	public ThumbnailButton(Context context) { super(context); }

	public ThumbnailButton(Context context, int imageResId, final Runnable action) {
		super(context);
		int defaultMargin = (int) getResources().getDimension(R.dimen.default_margin);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(defaultMargin, defaultMargin, defaultMargin, defaultMargin);
		setLayoutParams(layoutParams);
		setImageResource(imageResId);
		setBackgroundResource(R.drawable.small_button_selector);
		setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				action.run();
			}
		});
	}
}
