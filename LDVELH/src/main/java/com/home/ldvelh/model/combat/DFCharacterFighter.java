package com.home.ldvelh.model.combat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.widget.CustomNumberPicker;
import com.home.ldvelh.ui.widget.CustomNumberPicker.WatchType;

public class DFCharacterFighter extends Fighter {
    private static final long serialVersionUID = 6689551278167471663L;

    public DFCharacterFighter() {
        super(Team.GOODGUYS);
    }

    @Override
    public String getName() {
        return Utils.getString(R.string.you);
    }

    @Override
    public IntValueHolder getSkill() {
        return Property.SKILL.get();
    }

    @Override
    public IntValueHolder getStamina() {
        return Property.STAMINA.get();
    }

    @Override
    public IntValueHolder getBonus() {
        return Property.ATTACK_BONUS.get();
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup root) {
        View newView = inflater.inflate(R.layout.list_item_combat_goodguy, root, false);
        initView(newView);
        CustomNumberPicker bonusPicker = newView.findViewById(R.id.numberPickerBonus);
        bonusPicker.init(Property.ATTACK_BONUS.get(), WatchType.VALUE);
        return newView;
    }
}
