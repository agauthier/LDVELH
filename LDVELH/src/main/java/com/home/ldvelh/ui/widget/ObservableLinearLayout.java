package com.home.ldvelh.ui.widget;

import java.util.Observer;

import com.home.ldvelh.commons.GameObservable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@SuppressWarnings("unused")
public abstract class ObservableLinearLayout extends LinearLayout {

	private final transient GameObservable observable = new GameObservable() {};

	public ObservableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void addObserver(Observer observer) {
		observable.addObserver(observer);
	}

	public void deleteObserver(Observer observer) {
		observable.deleteObserver(observer);
	}

	public void suspendUpdates() {
		observable.suspendUpdates();
	}

	public void resumeUpdates() {
		observable.resumeUpdates();
	}

	protected void notifyObservers(Object data) {
		observable.notifyObservers(data);
	}

	void notifyObservers() {
		observable.notifyObservers();
	}
}
