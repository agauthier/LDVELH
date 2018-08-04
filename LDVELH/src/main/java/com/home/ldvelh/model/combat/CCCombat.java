package com.home.ldvelh.model.combat;

import android.widget.ImageButton;

public class CCCombat implements Combat {

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
        return (T) new CCEditableFighter();
    }

    @Override
    public void addNewFighter(Fighter fighter, CombatRow.Team team) {
        CombatCore.addNewFighter(fighter, team);
    }

    @Override
    public boolean canAssault() {
        return CombatCore.canAssault();
    }

    @Override
    public void assault(ImageButton button) {

    }

    @Override
    public boolean canEscape() {
        return false;
    }

    @Override
    public void escape(ImageButton button) {

    }

    @Override
    public void addAllObservers() {

    }

    @Override
    public void removeAllObservers() {

    }

    @SuppressWarnings("unused")
    public boolean canHelp() {
        return false;
    }

    @SuppressWarnings("unused")
    public void help(ImageButton button) {}

    @SuppressWarnings("unused")
    public boolean canKill() {
        return false;
    }

    @SuppressWarnings("unused")
    public void kill(ImageButton button) {}

    @SuppressWarnings("unused")
    public boolean canSurrender() {
        return false;
    }

    @SuppressWarnings("unused")
    public void surrender(ImageButton button) {}
}
