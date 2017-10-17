package com.home.ldvelh.model.value;

import java.util.Observable;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Die;

public class DF04CrewMemberValueHolder extends DF04AssetValueHolder {
	private static final long serialVersionUID = -7012397204752714695L;

	private static final int SKILL_LOSS = -2;

	public DF04CrewMemberValueHolder(String name) {
		this(name, 0);
	}

	public DF04CrewMemberValueHolder(String name, int bonus) {
		super(name, Location.ONSHIPCANLEAVE, bonus);
	}

	@Override public void update(Observable observable, Object data) {
		if (getStamina().getValue() <= 0) {
			getStamina().deleteObserver(this);
			kill();
		}
	}

	@Override public void updateStaminaObserver() {
		if (getLocation().is(Location.ONSHIPCANLEAVE)) {
			getStamina().addObserver(this);
		} else if (getLocation().is(Location.ONPLANET)) {
			getStamina().deleteObserver(this);
		}
	}

	@Override public boolean canBeDragged() {
		return getLocation().is(Location.ONSHIPCANLEAVE);
	}

	@Override public boolean isCommander() {
		return false;
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
		if (!getLocation().is(Location.ONSHIPDIEDONCE)) {
			getLocation().setValue(Location.ONSHIPCANLEAVE);
			getStamina().addObserver(this);
		}
	}

	@Override public void descendOnPlanet() {
		if (!getLocation().is(Location.ONSHIPDIEDONCE)) {
			getLocation().setValue(Location.ONPLANET);
			getStamina().deleteObserver(this);
		}
	}

	@Override public void startSpaceFight() {
		if (getLocation().is(Location.ONPLANET) || getLocation().is(Location.ONSHIPCANLEAVE)) {
			getLocation().setValue(Location.ONSHIPSPACEFIGHT);
		}
	}

	@Override public void endSpaceFight() {
		if (getLocation().is(Location.ONSHIPSPACEFIGHT)) {
			getLocation().setValue(Location.ONSHIPCANLEAVE);
		}
	}

	@Override public void kill() {
		getLocation().setValue(Location.ONSHIPDIEDONCE);
		getSkill().addWithFeedback(SKILL_LOSS);
		getSkill().addToMax(SKILL_LOSS);
		getStamina().addToMax(12 + Die.SIX_FACES.roll(2) - getStamina().getMax());
		getStamina().addWithFeedback(Constants.BIG_POSITIVE);
	}
}
