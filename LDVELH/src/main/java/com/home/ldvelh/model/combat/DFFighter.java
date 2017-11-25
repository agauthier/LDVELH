package com.home.ldvelh.model.combat;

import com.home.ldvelh.commons.GameObservable;
import com.home.ldvelh.model.value.IntValueHolder;

public abstract class DFFighter extends Fighter {
    private static final long serialVersionUID = 3555680423898897407L;

    @Override
    public GameObservable getLifeObservable() {
        return getStamina();
    }

    @Override
    public boolean isDead() {
        return getStamina().getValue() <= 0;
    }

    @Override
    public void kill() {}

    public abstract IntValueHolder getSkill();

    public abstract IntValueHolder getStamina();

    public abstract IntValueHolder getBonus();
}
