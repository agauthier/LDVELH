package com.home.ldvelh.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import com.home.ldvelh.R;

public abstract class AdventureDialog extends Dialog {

	private static AdventureDialog currentDialog;

	private final Object data;
	private boolean fulfilled = false;

	AdventureDialog(Context context, Object data) {
		super(context, android.R.style.Theme_Light_Panel);
		this.data = data;
		setOwnerActivity((Activity) context);
		final AdventureDialog adventureDialog = this;
		setOnShowListener(new OnShowListener() {
			@Override public void onShow(DialogInterface dialog) {
				currentDialog = adventureDialog;
			}
		});
	}

	@Override public void onBackPressed() {
		dismiss();
	}

	Object getData() {
		return data;
	}

	void setQuestionText(int textResId) {
		TextView textView = findViewById(R.id.question);
		if (textView != null) {
			textView.setText(textResId);
		}
	}

	TextView setText(int textViewResId, String text) {
		TextView textView = findViewById(textViewResId);
		if (textView != null) {
			textView.setText(text);
		}
		return textView;
	}

	void fulfill() {
		fulfilled = true;
	}

	public boolean isFulfilled() {
		return fulfilled;
	}

	public static void dismissCurrentDialog() {
		if (currentDialog != null && currentDialog.isShowing()) {
			currentDialog.setOnDismissListener(null);
			currentDialog.dismiss();
		}
	}
}
