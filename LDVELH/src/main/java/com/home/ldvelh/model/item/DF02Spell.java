package com.home.ldvelh.model.item;

import java.util.List;

public class DF02Spell extends EffectItem {
    private static final long serialVersionUID = -974144323950373450L;

    public DF02Spell(String name) {
        super(name);
    }

    public DF02Spell(String name, int quantity, List<Effect> effects) {
        super(name, quantity, effects);
    }

    public boolean canCastTwice() {
        return hasEffects() && getQuantity() >= 2;
    }
}
