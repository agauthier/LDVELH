package com.home.ldvelh.model.combat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.home.ldvelh.commons.OpMode;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.value.ListValueHolder;

public class CombatCore {

	static void init() {
		if (Property.FIGHTER_GRID.get().size() == 0) {
			reset();
		}
	}

	static void reset() {
		removeAllObservers();
		ListValueHolder<CombatRow> fighterGrid = Property.FIGHTER_GRID.get();
		fighterGrid.clear();
		DFEditableFighter.resetNbFighters();
		for (Fighter fighter : Property.CHARACTER.get().getFighters()) {
			addFighter(fighter);
		}
	}

	static void addEditableFighter(DFEditableFighter fighter) {
		addFighter(fighter);
	}

	static boolean canMoveUp() {
		return moveUp(OpMode.TEST);
	}

	static boolean canMoveDown() {
		return moveDown(OpMode.TEST);
	}

	static void moveUp() {
		moveUp(OpMode.PERFORM);
	}

	static void moveDown() {
		moveDown(OpMode.PERFORM);
	}

	static void escape() {
		Property.CHARACTER.get().getCombatStrategy().escape();
		reset();
	}

	static void assault() {
		Property.CHARACTER.get().getCombatStrategy().assault();
	}

	static boolean canMoveToRowAbove(Fighter fighter, CombatRow row) {
		return moveToRowAbove(fighter, row, OpMode.TEST);
	}

	static boolean canMoveToRowBelow(Fighter fighter, CombatRow row) {
		return moveToRowBelow(fighter, row, OpMode.TEST);
	}

	static boolean canReset() {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.canReset()) {
				return true;
			}
		}
		return false;
	}

	static boolean canEscape() {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.hasMembers(Team.MONSTERS)) {
				return true;
			}
		}
		return false;
	}

	static boolean canAssault() {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.canAssault()) {
				return true;
			}
		}
		return false;
	}

	static void addAllObservers() {
		updateAllObservers(false);
	}

	static void removeAllObservers() {
		updateAllObservers(true);
	}

	static void deselectAll() {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			row.deselectAll();
		}
	}

	public static List<Fighter> getAllMonsters() {
		List<Fighter> fighters = new ArrayList<>();
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			fighters.addAll(row.getMonsters());
		}
		return fighters;
	}

	static void kill(Fighter fighter) {
		fighter.kill();
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			Team team = row.kill(fighter);
			if (team != null) {
				if (!row.hasMembers(team) && row.hasOpponents(team)) {
					List<Fighter> newFighters = getNextOpponentlessTeam(team);
					for (Fighter newFighter : newFighters) {
						row.addMemberToEnd(newFighter);
					}
				}
				break;
			}
		}
		touch();
	}

	static boolean moveToRowAbove(Fighter fighter, CombatRow currentRow, OpMode opMode) {
		CombatRow rowAbove = Property.FIGHTER_GRID.get().getItemAbove(currentRow);
		if (rowAbove == null) {
			return false;
		} else if (rowAbove.canAdmitFighter(fighter)) {
			if (opMode == OpMode.PERFORM) {
				rowAbove.addMemberToEnd(fighter);
			}
			return true;
		} else {
			return moveToRowAbove(fighter, rowAbove, opMode);
		}
	}

	static boolean moveToRowBelow(Fighter fighter, CombatRow currentRow, OpMode opMode) {
		ListValueHolder<CombatRow> fighterGrid = Property.FIGHTER_GRID.get();
		CombatRow rowBelow = fighterGrid.getItemBelow(currentRow);
		if (rowBelow == null) {
			if (currentRow.hasMembers(fighter.getTeam().facingTeam())) {
				if (opMode == OpMode.PERFORM) {
					fighterGrid.suspendUpdates();
					CombatRow newRow = fighterGrid.add(new ItemAndQuantity(StringUtils.EMPTY, 1));
					newRow.addMemberToEnd(fighter);
					fighterGrid.resumeUpdates();
				}
				return true;
			} else {
				return false;
			}
		} else if (rowBelow.canAdmitFighter(fighter)) {
			if (opMode == OpMode.PERFORM) {
				rowBelow.addMemberToFront(fighter);
			}
			return true;
		} else {
			return moveToRowBelow(fighter, rowBelow, opMode);
		}
	}

	static void touch() {
		removeEmptyRows();
		Property.FIGHTER_GRID.get().touch();
	}

	private static void addFighter(Fighter fighter) {
		CombatRow combatRow = getNewCombatRow(fighter.getTeam());
		FighterObserver.add(fighter);
		combatRow.addMemberToEnd(fighter);
		touch();
	}

	private static CombatRow getNewCombatRow(Team team) {
		CombatRow combatRow = null;
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (!row.hasMembers(team)) {
				combatRow = row;
				break;
			}
		}
		if (combatRow == null) {
			combatRow = Property.FIGHTER_GRID.get().add(new ItemAndQuantity(StringUtils.EMPTY, 1));
		}
		return combatRow;
	}

	private static boolean moveUp(OpMode opMode) {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.moveUp(opMode)) {
				if (opMode == OpMode.PERFORM) {
					touch();
				}
				return true;
			}
		}
		return false;
	}

	private static boolean moveDown(OpMode opMode) {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.moveDown(opMode)) {
				if (opMode == OpMode.PERFORM) {
					touch();
				}
				return true;
			}
		}
		return false;
	}

	private static void updateAllObservers(boolean delete) {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			row.updateAllObservers(delete);
		}
	}

	private static void removeEmptyRows() {
		ListValueHolder<CombatRow> fighterGrid = Property.FIGHTER_GRID.get();
		for (int i = fighterGrid.size() - 1; i >= 0; i--) {
			CombatRow row = fighterGrid.get(i);
			if (row.isEmpty()) {
				fighterGrid.remove(row);
			}
		}
	}

	private static List<Fighter> getNextOpponentlessTeam(Team team) {
		List<Fighter> fighters = new ArrayList<>();
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.hasMembers(team) && !row.hasOpponents(team)) {
				fighters.addAll(row.getFighters(team));
				row.clear();
				break;
			}
		}
		return fighters;
	}
}
