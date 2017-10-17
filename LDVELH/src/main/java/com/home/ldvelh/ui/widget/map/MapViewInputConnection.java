package com.home.ldvelh.ui.widget.map;

import org.apache.commons.lang3.StringUtils;

import android.os.SystemClock;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;

class MapViewInputConnection extends BaseInputConnection {

	private final Editable editable = Editable.Factory.getInstance().newEditable(StringUtils.EMPTY);
	private final MapView mapView;

	private int swipeLength = 0;

	public MapViewInputConnection(View targetView) {
		super(targetView, true);
		mapView = (MapView) targetView;
	}

	@Override public Editable getEditable() {
		return editable;
	}

	@Override public boolean deleteSurroundingText(int beforeLength, int afterLength) {
		if (editable.length() > 0) {
			editable.delete(editable.length() - beforeLength, editable.length());
			mapView.setText(editable.toString());
		}
		return false;
	}

	@Override public boolean commitText(CharSequence text, int newCursorPosition) {
		if (swipeLength > 0) {
			if (text.length() == 0) {
				deleteSurroundingText(swipeLength, 0);
			}
			swipeLength = 0;
		} else {
			editable.append(text);
			mapView.setText(editable.toString());
		}
		return true;
	}

	@Override public boolean setComposingText(CharSequence text, int newCursorPosition) {
		String swipeText = text + " ";
		commitText(swipeText, newCursorPosition);
		swipeLength = swipeText.length();
		return true;
	}

	@Override public boolean sendKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9) {
				commitText(String.valueOf(event.getKeyCode() - KeyEvent.KEYCODE_0), 0);
				return true;
			} else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
				finishComposingText();
			}
		}
		return super.sendKeyEvent(event);
	}

	@Override public boolean finishComposingText() {
		if (editable != null) {
			editable.clear();
		}
		swipeLength = 0;
		mapView.hideKeyboard();
		MotionEvent dummyEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0);
		mapView.dispatchTouchEvent(dummyEvent);
		dummyEvent.recycle();
		mapView.finishComposingText();
		return super.finishComposingText();
	}
}
