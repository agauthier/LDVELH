package com.home.ldvelh.model.combat;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.value.ListValueHolder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CombatCore {

    public static List<Fighter> getAllRightFighters() {
        List<Fighter> fighters = new ArrayList<>();
        for (CombatRow row : Property.FIGHTER_GRID.get()) {
            fighters.addAll(row.getTeamRight());
        }
        return fighters;
    }

    public static Fighter findFighterByName(String name) {
        Fighter fighter = null;
        for (CombatRow row : Property.FIGHTER_GRID.get()) {
            fighter = row.findFighterByName(name);
            if (fighter != null) {
                break;
            }
        }
        return fighter;
    }

    public static void kill(Fighter fighter) {
        fighter.kill();
        for (CombatRow row : Property.FIGHTER_GRID.get()) {
            Team team = row.kill(fighter);
            if (team != null) {
                if (!row.hasMembers(team) && row.hasOpponents(team)) {
                    List<Fighter> newFighters = getNextOpponentlessTeam(team);
                    for (Fighter newFighter : newFighters) {
                        row.add(newFighter, team);
                    }
                }
                break;
            }
        }
        touch();
    }

    static void init() {
        if (Property.FIGHTER_GRID.get().size() == 0) {
            reset();
        }
    }

    static void reset() {
        removeAllObservers();
        ListValueHolder<CombatRow> fighterGrid = Property.FIGHTER_GRID.get();
        fighterGrid.clear();
        for (Fighter fighter : Property.CHARACTER.get().getFighters()) {
            addNewFighter(fighter, Team.LEFT);
        }
    }

    static void escape() {
        Property.CHARACTER.get().getCombatStrategy().escape();
        reset();
    }

    static void assault() {
        Property.CHARACTER.get().getCombatStrategy().assault();
    }

    static boolean canEscape() {
        for (CombatRow row : Property.FIGHTER_GRID.get()) {
            if (row.hasMembers(Team.LEFT) && row.hasMembers(Team.RIGHT)) {
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

    static void addNewFighter(Fighter fighter, Team team) {
        CombatRow combatRow = getNewCombatRow(team);
        FighterObserver.add(fighter);
        combatRow.add(fighter, team);
        touch();
    }

    static void moveFighter(Fighter fighter, CombatRow dstRow, Team dstTeam) {
        findRow(fighter).remove(fighter);
        dstRow.add(fighter, dstTeam);
        touch();
    }

    private static void touch() {
        removeEmptyRows();
        Property.FIGHTER_GRID.get().touch();
    }

    private static CombatRow findRow(Fighter fighter) {
        CombatRow foundRow = null;
        for (CombatRow row : Property.FIGHTER_GRID.get()) {
            if (row.hasFighter(fighter)) {
                foundRow = row;
            }
        }
        return foundRow;
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
