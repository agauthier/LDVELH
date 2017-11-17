package com.home.ldvelh.model.combat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.widget.AnimatedNumber;

import org.apache.commons.lang3.StringUtils;

public class DFEditableFighter extends Fighter {
    private static final long serialVersionUID = 5612175650712298670L;

    private static final int DEFAULT_SKILL = 5;
    private static final int DEFAULT_STAMINA = 5;

    private String name = StringUtils.EMPTY;
    private final IntValueHolder skill;
    private final IntValueHolder stamina;
    private final IntValueHolder bonus;

    public DFEditableFighter() {
        super();
        this.skill = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_SKILL);
        this.stamina = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_STAMINA);
        this.bonus = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public IntValueHolder getSkill() {
        return skill;
    }

    @Override
    public IntValueHolder getStamina() {
        return stamina;
    }

    @Override
    public IntValueHolder getBonus() {
        return bonus;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup root) {
        View newView = inflater.inflate(R.layout.list_item_combat_fighter, root, false);
        initView(newView, false);
        AnimatedNumber animSkill = newView.findViewById(R.id.animatedNumberSkill);
        animSkill.init(skill, WatchType.VALUE);
        AnimatedNumber animStamina = newView.findViewById(R.id.animatedNumberStamina);
        animStamina.init(stamina, WatchType.VALUE);
        return newView;
    }

    @Override
    public void kill() {}
}
