package com.home.ldvelh.ui.activity;

import java.util.Observer;

import com.home.ldvelh.commons.GameObservable;

import android.support.v4.app.FragmentActivity;

@SuppressWarnings("unused")
public abstract class ObservableFragmentActivity extends FragmentActivity {

	private final transient GameObservable observable = new GameObservable() {};

	public void addObserver(Observer observer) {
		observable.addObserver(observer);
	}

	public void deleteObserver(Observer observer) {
		observable.deleteObserver(observer);
	}

	void notifyObservers() {
		observable.notifyObservers();
	}

	public void notifyObservers(Object data) {
		observable.notifyObservers(data);
	}

	public void suspendUpdates() {
		observable.suspendUpdates();
	}

	public void resumeUpdates() {
		observable.resumeUpdates();
	}
}
