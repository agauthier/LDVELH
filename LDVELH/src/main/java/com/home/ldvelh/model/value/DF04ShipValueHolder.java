package com.home.ldvelh.model.value;

import java.util.Observable;

public class DF04ShipValueHolder extends DF04AssetValueHolder {
	private static final long serialVersionUID = -1580381553724970369L;

	public DF04ShipValueHolder(String name, int skill, int stamina) {
		super(name, skill, stamina);
	}

	@Override public void update(Observable observable, Object data) {}

	@Override public void updateStaminaObserver() {}

	@Override public boolean canBeDragged() {
		return getLocation().is(Location.INSPACE);
	}

	@Override public boolean isCommander() {
		return false;
	}

	@Override public boolean isShip() {
		return true;
	}

	@Override public boolean isOnPlanet() {
		return false;
	}

	@Override public boolean isInSpaceFight() {
		return getLocation().is(Location.INSPACEFIGHT);
	}

	@Override public void leavePlanet() {}

	@Override public void descendOnPlanet() {}

	@Override public void startSpaceFight() {
		getLocation().setValue(Location.INSPACEFIGHT);
	}

	@Override public void endSpaceFight() {
		getLocation().setValue(Location.INSPACE);
	}

	@Override public void kill() {}
}
