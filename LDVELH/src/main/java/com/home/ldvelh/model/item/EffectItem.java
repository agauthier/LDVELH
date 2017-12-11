package com.home.ldvelh.model.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EffectItem extends Item {
    private static final long serialVersionUID = -216600216547931062L;

    private final List<Effect> effects = new ArrayList<>();

    public EffectItem(String name) {
        this(name, Collections.<Effect>emptyList());
    }

    public EffectItem(String name, List<Effect> effects) {
        super(name, null);
        this.effects.addAll(effects);
    }

    public EffectItem(String name, int quantity, List<Effect> effects) {
        super(name, quantity, null);
        this.effects.addAll(effects);
    }

    @Override
    public boolean isIdentical(Item item) {
        return super.isIdentical(item) && effects.equals(((EffectItem) item).effects);
    }

    public boolean hasEffects() {
        return effects.size() > 0;
    }

    public void applyEffects() {
        for (Effect effect : effects) {
            effect.apply();
        }
    }

    public void applyEffectsTwice() {
        for (Effect effect : effects) {
            effect.applyTwice();
        }
    }
}
