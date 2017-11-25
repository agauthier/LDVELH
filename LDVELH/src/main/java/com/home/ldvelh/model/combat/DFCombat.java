package com.home.ldvelh.model.combat;

import android.widget.ImageButton;

import com.home.ldvelh.model.combat.CombatRow.Team;

public class DFCombat implements Combat {

    @Override
    public void init() {
        CombatCore.init();
    }

    @Override
    public void reset() {
        CombatCore.reset();
    }

    @Override
    public <T extends EditableFighter> T createEditableFighter() {
        //noinspection unchecked
        return (T) new DFEditableFighter();
    }

    @Override
    public void addNewFighter(Fighter fighter, Team team) {
        CombatCore.addNewFighter(fighter, team);
    }

    @Override
    public boolean canAssault() {
        return CombatCore.canAssault();
    }

    @Override
    public void assault(ImageButton button) {
        CombatCore.assault();
    }

    @Override
    public boolean canEscape() {
        return CombatCore.canEscape();
    }

    @Override
    public void escape(ImageButton button) {
        CombatCore.escape();
    }

    @Override
    public void addAllObservers() {
        CombatCore.addAllObservers();
    }

    @Override
    public void removeAllObservers() {
        CombatCore.removeAllObservers();
    }
}
