package com.home.ldvelh.commons;

import java.util.Observable;

public abstract class GameObservable extends Observable {

	private transient boolean updateSuspended = false;

	@Override public void notifyObservers() {
		notifyObservers(null);
	}

	@Override public void notifyObservers(Object data) {
		if (!updateSuspended) {
			setChanged();
			super.notifyObservers(data);
		}
	}

	public void suspendUpdates() {
		this.updateSuspended = true;
	}

	public void resumeUpdates() {
		this.updateSuspended = false;
	}
}
