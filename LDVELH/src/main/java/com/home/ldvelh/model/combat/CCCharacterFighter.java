package com.home.ldvelh.model.combat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.value.IntValueHolder;

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
    public void setName(String name) {}

    @Override
    public IntValueHolder getSkill() {
        return Property.STRENGTH.get();
    }

    @Override
    public IntValueHolder getStamina() {
        return Property.PROTECTION.get();
    }

    @Override
    public IntValueHolder getBonus() {
        return null;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup root) {
        View newView = inflater.inflate(R.layout.list_item_cc_combat_character, root, false);
        initView(newView, true);
        return newView;
    }

    @Override
    public void kill() {}
}
