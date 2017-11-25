package com.home.ldvelh.model.combat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.GameObservable;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.value.IntValueHolder;

import static com.home.ldvelh.model.combat.CCCondition.DEAD;

public class CCCharacterFighter extends Fighter {
    private static final long serialVersionUID = 6119836001865605344L;

    public CCCharacterFighter() {
        super();
    }

    @Override
    public String getName() {
        return Utils.getString(R.string.cc_character);
    }

    @Override
    public GameObservable getLifeObservable() {
        return Property.CONDITION.get();
    }

    @Override
    public boolean isDead() {
        return Property.CONDITION.get().getValue() == DEAD;
    }

    @Override
    public void kill() {}

    @Override
    public View createView(LayoutInflater inflater, ViewGroup root) {
        View newView = inflater.inflate(R.layout.list_item_cc_combat_character, root, false);
        initView(newView, true);
        return newView;
    }

    public IntValueHolder getStrength() {
        return Property.STRENGTH.get();
    }

    public IntValueHolder getProtection() {
        return Property.PROTECTION.get();
    }
}
