package com.home.ldvelh.model.combat;

import java.io.Serializable;

import com.home.ldvelh.R;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.value.IntValueHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class Fighter implements Serializable {
	private static final long serialVersionUID = -2622572057143007364L;

	private final Team team;
	private boolean selected;

	private transient View refView;

	Fighter(Team team) {
		this.team = team;
	}
	
	public abstract String getName();

	public abstract IntValueHolder getSkill();

	public abstract IntValueHolder getStamina();

	public abstract IntValueHolder getBonus();

	public abstract View createView(LayoutInflater inflater, ViewGroup root);

	public boolean isStaminaObservable() {
		return false;
	}

	public Team getTeam() {
		return team;
	}
	
	public boolean isEditable() {
		return false;
	}
	
	public void kill() {}

	void deselect() {
		changeSelection(refView, false);
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	void initView(final View refView) {
		this.refView = refView;
		changeSelection(refView, selected);
		final TextView fighterNameView = refView.findViewById(R.id.fighterName);
		fighterNameView.setText(getName());
		fighterNameView.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				if (selected) {
					CombatCore.deselectAll();
				} else {
					CombatCore.deselectAll();
					changeSelection(refView, true);
				}
				CombatCore.touch();
			}
		});
	}

	private void changeSelection(View refView, boolean selected) {
		this.selected = selected;
		if (refView != null) {
			refView.setSelected(selected);
		}
	}
}
