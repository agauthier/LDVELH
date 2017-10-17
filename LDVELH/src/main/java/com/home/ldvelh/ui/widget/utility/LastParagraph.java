package com.home.ldvelh.ui.widget.utility;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LastParagraph extends LinearLayout implements UtilityView {

	public LastParagraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.widget_utility_last_paragraph, this);
	}

	@Override public void initLayout() {
		final EditText editText = findViewById(R.id.lastParagraph);
		editText.setText(Property.LAST_PARAGRAPH.get().getValue());
		editText.setSelectAllOnFocus(true);
		editText.setOnKeyListener(new OnKeyListener() {
			@Override public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						Property.LAST_PARAGRAPH.get().setValue(editText.getText().toString());
						hideKeyboard(editText);
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
	}

	private void hideKeyboard(final EditText editText) {
		InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		editText.clearFocus();
	}
}
