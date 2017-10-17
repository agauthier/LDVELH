package com.home.ldvelh.ui.widget;

import com.home.ldvelh.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ChoiceButton extends LinearLayout {

	public ChoiceButton(Context context) { super(context); }

	public ChoiceButton(Context context, int textResId, final Runnable action) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.widget_choice_button, this);
		Button button = findViewById(R.id.button);
		button.setText(textResId);
		button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				action.run();
			}
		});
	}
}
