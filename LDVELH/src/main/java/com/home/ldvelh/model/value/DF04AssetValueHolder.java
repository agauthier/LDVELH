package com.home.ldvelh.model.value;

import java.util.Observer;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Die;

public abstract class DF04AssetValueHolder extends ValueHolder<DF04AssetValueHolder>implements Observer {
	private static final long serialVersionUID = 1376933306272500052L;

	enum Location {
		ONSHIPCANLEAVE, ONSHIPDIEDONCE, ONSHIPSPACEFIGHT, ONPLANET, INSPACE, INSPACEFIGHT
	}

	private final String name;
	private final IntValueHolder skill;
	private final IntValueHolder stamina;
	private final IntValueHolder bonus;
	private final EnumValueHolder<Location> location;

	DF04AssetValueHolder(String name) {
		this(name, Location.ONPLANET, 0);
	}

	DF04AssetValueHolder(String name, Location location, int bonus) {
		this(name, location, bonus, 6 + Die.SIX_FACES.roll(), 12 + Die.SIX_FACES.roll(2));
	}

	DF04AssetValueHolder(String name, int skill, int stamina) {
		this(name, Location.INSPACE, 0, skill, stamina);
	}

	private DF04AssetValueHolder(String name, Location location, int bonus, int skill, int stamina) {
		super(null);
		this.name = name;
		this.skill = new IntValueHolder(0, skill, skill);
		this.stamina = new IntValueHolder(0, stamina, stamina);
		this.bonus = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, bonus);
		this.location = new EnumValueHolder<>(location);
	}

	public String getName() {
		return name;
	}

	public IntValueHolder getSkill() {
		return skill;
	}

	public IntValueHolder getStamina() {
		return stamina;
	}

	public IntValueHolder getBonus() {
		return bonus;
	}

	EnumValueHolder<Location> getLocation() {
		return location;
	}

	public void addLocationObserver(Observer observer) {
		location.addObserver(observer);
	}

	public void deleteLocationObserver(Observer observer) {
		location.deleteObserver(observer);
	}

	public abstract void updateStaminaObserver();

	public abstract boolean canBeDragged();

	public abstract boolean isCommander();

	public abstract boolean isShip();

	public abstract boolean isOnPlanet();

	public abstract boolean isInSpaceFight();

	public abstract void leavePlanet();

	public abstract void descendOnPlanet();

	public abstract void startSpaceFight();

	public abstract void endSpaceFight();

	public abstract void kill();
}
