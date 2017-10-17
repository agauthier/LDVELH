package com.home.ldvelh.model.combat;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.widget.CustomNumberPicker;
import com.home.ldvelh.ui.widget.CustomNumberPicker.WatchType;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class DFEditableFighter extends Fighter {
	private static final long serialVersionUID = 5612175650712298670L;

	private static final int DEFAULT_SKILL = 5;
	private static final int DEFAULT_STAMINA = 5;

	private final String name;
	private final IntValueHolder skill;
	private final IntValueHolder stamina;
	private final IntValueHolder bonus;

	private static int nbFighters = 0;

	DFEditableFighter(Team team) {
		super(team);
		this.name = "#" + (++nbFighters);
		this.skill = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_SKILL);
		this.stamina = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_STAMINA);
		this.bonus = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
	}

	@Override public String getName() {
		return name;
	}

	@Override public IntValueHolder getSkill() {
		return skill;
	}

	@Override public IntValueHolder getStamina() {
		return stamina;
	}

	@Override public IntValueHolder getBonus() {
		return bonus;
	}

	@Override public View createView(LayoutInflater inflater, ViewGroup root) {
		View newView = inflater.inflate(R.layout.list_item_combat_fighter, root, false);
		initView(newView);
		CustomNumberPicker skillPicker = newView.findViewById(R.id.numberPickerSkill);
		skillPicker.init(skill, WatchType.VALUE);
		CustomNumberPicker staminaPicker = newView.findViewById(R.id.numberPickerStamina);
		staminaPicker.init(stamina, WatchType.VALUE);
		return newView;
	}

	@Override public boolean isStaminaObservable() {
		return true;
	}

	@Override public boolean isEditable() {
		return true;
	}

	static void resetNbFighters() {
		nbFighters = 0;
	}
}
