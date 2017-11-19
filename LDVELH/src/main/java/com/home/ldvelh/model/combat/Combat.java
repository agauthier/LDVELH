package com.home.ldvelh.model.combat;

import android.widget.ImageButton;

import com.home.ldvelh.model.combat.CombatRow.Team;

public class Combat {

    public void init() {
        CombatCore.init();
    }

    public void reset() {
        CombatCore.reset();
    }

    public void addNewFighter(Fighter fighter, Team team) {
        CombatCore.addNewFighter(fighter, team);
    }

    @SuppressWarnings("unused")
    public boolean canAssault() {
        return CombatCore.canAssault();
    }

    @SuppressWarnings("unused")
    public void assault(ImageButton button) {
        CombatCore.assault();
    }

    @SuppressWarnings("unused")
    public boolean canEscape() {
        return CombatCore.canEscape();
    }

    @SuppressWarnings("unused")
    public void escape(ImageButton button) {
        CombatCore.escape();
    }

    public void addAllObservers() {
        CombatCore.addAllObservers();
    }

    public void removeAllObservers() {
        CombatCore.removeAllObservers();
    }
}
