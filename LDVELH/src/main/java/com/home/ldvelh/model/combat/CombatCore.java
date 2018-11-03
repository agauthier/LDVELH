package com.home.ldvelh.model.combat;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.value.ListValueHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.home.ldvelh.model.Property.FIGHTER_GRID;

public class CombatCore {

    public static <T extends Fighter> List<T> getAllRightFighters() {
        List<T> fighters = new ArrayList<>();
        for (CombatRow row : FIGHTER_GRID.get()) {
            //noinspection unchecked
            fighters.addAll((Collection<? extends T>) row.getTeamRight());
        }
        return fighters;
    }

    public static <T extends Fighter> T findFighterByName(String name) {
        T fighter = null;
        for (CombatRow row : FIGHTER_GRID.get()) {
            //noinspection unchecked
            fighter = (T) row.findFighterByName(name);
            if (fighter != null) {
                break;
            }
        }
        return fighter;
    }

    public static <T extends Fighter> void kill(T fighter) {
        fighter.kill();
        for (CombatRow row : FIGHTER_GRID.get()) {
            Team team = row.kill(fighter);
            if (team != null) {
                if (!row.hasMembers(team) && row.hasOpponents(team)) {
                    List<T> newFighters = getNextOpponentlessTeam(team);
                    for (T newFighter : newFighters) {
                        row.add(newFighter, team);
                    }
                }
                break;
            }
        }
        touch();
    }

    public static <T extends Fighter> void moveFighter(T fighter, CombatRow dstRow, Team dstTeam) {
        findRow(fighter).remove(fighter);
        dstRow.add(fighter, dstTeam);
        touch();
    }

    static void init() {
        if (FIGHTER_GRID.get().size() == 0) {
            reset();
        }
    }

    static void reset() {
        removeAllObservers();
        ListValueHolder<CombatRow> fighterGrid = FIGHTER_GRID.get();
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
        for (CombatRow row : FIGHTER_GRID.get()) {
            if (row.hasMembers(Team.LEFT) && row.hasMembers(Team.RIGHT)) {
                return true;
            }
        }
        return false;
    }

    static boolean canAssault() {
        for (CombatRow row : FIGHTER_GRID.get()) {
            if (row.canAssault()) {
                return true;
            }
        }
        return false;
    }

    static void addAllObservers() {
        for (CombatRow row : FIGHTER_GRID.get()) {
            row.addAllObservers();
        }
    }

    static void removeAllObservers() {
        for (CombatRow row : FIGHTER_GRID.get()) {
            row.removeAllObservers();
        }
    }

    static <T extends Fighter> void addNewFighter(T fighter, Team team) {
        CombatRow combatRow = getNewCombatRow(team);
        FighterObserver.add(fighter);
        combatRow.add(fighter, team);
        touch();
    }

    private static void touch() {
        removeEmptyRows();
        FIGHTER_GRID.get().touch();
    }

    private static <T extends Fighter> CombatRow findRow(T fighter) {
        CombatRow foundRow = null;
        for (CombatRow row : FIGHTER_GRID.get()) {
            if (row.hasFighter(fighter)) {
                foundRow = row;
            }
        }
        return foundRow;
    }

    private static CombatRow getNewCombatRow(Team team) {
        CombatRow combatRow = null;
        for (CombatRow row : FIGHTER_GRID.get()) {
            if (!row.hasMembers(team)) {
                combatRow = row;
                break;
            }
        }
        if (combatRow == null) {
            combatRow = new CombatRow();
            FIGHTER_GRID.get().add(combatRow);
        }
        return combatRow;
    }

    private static void removeEmptyRows() {
        ListValueHolder<CombatRow> fighterGrid = FIGHTER_GRID.get();
        for (int i = fighterGrid.size() - 1; i >= 0; i--) {
            CombatRow row = fighterGrid.get(i);
            if (row.isEmpty()) {
                fighterGrid.remove(row);
            }
        }
    }

    private static <T extends Fighter> List<T> getNextOpponentlessTeam(Team team) {
        List<T> fighters = new ArrayList<>();
        for (CombatRow row : FIGHTER_GRID.get()) {
            if (row.hasMembers(team) && !row.hasOpponents(team)) {
                //noinspection unchecked
                fighters.addAll((Collection<? extends T>) row.getFighters(team));
                row.clear();
                break;
            }
        }
        return fighters;
    }
}
