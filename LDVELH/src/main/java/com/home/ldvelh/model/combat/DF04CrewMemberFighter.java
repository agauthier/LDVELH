package com.home.ldvelh.model.combat;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.value.DF04AssetValueHolder;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.widget.CustomNumberPicker;
import com.home.ldvelh.ui.widget.CustomNumberPicker.WatchType;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DF04CrewMemberFighter extends Fighter {
	private static final long serialVersionUID = 8890901301578812379L;

	private final DF04AssetValueHolder crewMember;

	public DF04CrewMemberFighter(Property<DF04AssetValueHolder> property) {
		super(Team.GOODGUYS);
		this.crewMember = property.get();
	}

	@Override public String getName() {
		return crewMember.getName();
	}

	@Override public IntValueHolder getSkill() {
		return crewMember.getSkill();
	}

	@Override public IntValueHolder getStamina() {
		return crewMember.getStamina();
	}

	@Override public IntValueHolder getBonus() {
		return crewMember.getBonus();
	}

	@Override public View createView(LayoutInflater inflater, ViewGroup root) {
		View newView = inflater.inflate(R.layout.list_item_combat_goodguy, root, false);
		initView(newView);
		CustomNumberPicker bonusPicker = newView.findViewById(R.id.numberPickerBonus);
		bonusPicker.init(crewMember.getBonus(), WatchType.VALUE);
		return newView;
	}

	@Override public boolean isStaminaObservable() {
		return true;
	}

	@Override public void kill() {
		if (!crewMember.isCommander()) {
			crewMember.kill();
		}
	}
}