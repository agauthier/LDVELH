package com.home.ldvelh.model.value;

import java.util.Observable;

public class DF04CommanderValueHolder extends DF04AssetValueHolder {
	private static final long serialVersionUID = -9075291814408781454L;

	public DF04CommanderValueHolder(String name) {
		super(name);
	}

	@Override public void update(Observable observable, Object data) {}

	@Override public void updateStaminaObserver() {}

	@Override public boolean canBeDragged() {
		return getLocation().is(Location.ONSHIPCANLEAVE);
	}

	@Override public boolean isCommander() {
		return true;
	}

	@Override public boolean isShip() {
		return false;
	}

	@Override public boolean isOnPlanet() {
		return getLocation().is(Location.ONPLANET);
	}

	@Override public boolean isInSpaceFight() {
		return false;
	}

	@Override public void leavePlanet() {
		getLocation().setValue(Location.ONSHIPCANLEAVE);
	}

	@Override public void descendOnPlanet() {
		getLocation().setValue(Location.ONPLANET);
	}

	@Override public void startSpaceFight() {
		getLocation().setValue(Location.ONSHIPCANLEAVE);
	}

	@Override public void endSpaceFight() {
		getLocation().setValue(Location.ONPLANET);
	}

	@Override public void kill() {}
}
