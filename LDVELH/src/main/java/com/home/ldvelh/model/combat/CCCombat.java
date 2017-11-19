package com.home.ldvelh.model.combat;

import android.widget.ImageButton;

import com.home.ldvelh.model.combat.CombatRow.Team;

public class CCCombat extends Combat {

    public void addNewFighter(Fighter fighter, Team team) {}

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
