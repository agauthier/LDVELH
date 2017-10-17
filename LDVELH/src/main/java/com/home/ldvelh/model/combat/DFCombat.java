package com.home.ldvelh.model.combat;

import com.home.ldvelh.model.combat.CombatRow.Team;

public class DFCombat {
	
	public void init() {
		CombatCore.init();
	}

	public void reset() {
		CombatCore.reset();
	}

	public void addEditableFighter(Team team) {
		CombatCore.addEditableFighter(team);
	}

	public void moveUp() {
		CombatCore.moveUp();
	}

	public void moveDown() {
		CombatCore.moveDown();
	}
	
	public void escape() {
		CombatCore.escape();
	}

	public void assault() {
		CombatCore.assault();
	}

	public boolean canMoveUp() {
		return CombatCore.canMoveUp();
	}

	public boolean canMoveDown() {
		return CombatCore.canMoveDown();
	}

	public boolean canReset() {
		return CombatCore.canReset();
	}

	public boolean canEscape() {
		return CombatCore.canEscape();
	}

	public boolean canAssault() {
		return CombatCore.canAssault();
	}

	public void addAllObservers() {
		CombatCore.addAllObservers();
	}

	public void removeAllObservers() {
		CombatCore.removeAllObservers();
	}
}
