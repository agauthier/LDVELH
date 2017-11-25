package com.home.ldvelh.model.combat;

import android.widget.ImageButton;

import com.home.ldvelh.model.combat.CombatRow.Team;

public interface Combat {

    void init();

    void reset();

    <T extends EditableFighter> T createEditableFighter();

    void addNewFighter(Fighter fighter, Team team);

    @SuppressWarnings("unused")
    boolean canAssault();

    @SuppressWarnings("unused")
    void assault(ImageButton button);

    @SuppressWarnings("unused")
    boolean canEscape();

    @SuppressWarnings("unused")
    void escape(ImageButton button);

    void addAllObservers();

    void removeAllObservers();
}
