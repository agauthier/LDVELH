package com.home.ldvelh.model.combat;

import android.widget.ImageButton;

import com.home.ldvelh.model.combat.CombatRow.Team;

public interface Combat {

    void init();

    void reset();

    <T extends EditableFighter> T createEditableFighter();

    void addNewFighter(Fighter fighter, Team team);

    boolean canAssault();

    void assault(ImageButton button);

    boolean canEscape();

    void escape(ImageButton button);

    void addAllObservers();

    void removeAllObservers();
}
