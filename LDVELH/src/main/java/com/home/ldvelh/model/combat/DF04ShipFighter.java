package com.home.ldvelh.model.combat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.widget.AnimatedNumber;

import org.apache.commons.lang3.StringUtils;

class DF04ShipFighter extends Fighter {
    private static final long serialVersionUID = -7232644395791504982L;

    private static final int DEFAULT_SKILL = 5;
    private static final int DEFAULT_STAMINA = 5;

    private String name = StringUtils.EMPTY;
    private final IntValueHolder attackForce;
    private final IntValueHolder shields;
    private final IntValueHolder bonus;

    DF04ShipFighter() {
        super();
        this.name = StringUtils.EMPTY;
        this.attackForce = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_SKILL);
        this.shields = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_STAMINA);
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
        return attackForce;
    }

    @Override
    public IntValueHolder getStamina() {
        return shields;
    }

    @Override
    public IntValueHolder getBonus() {
        return bonus;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup root) {
        View newView = inflater.inflate(R.layout.list_item_combat_df04_ship_fighter, root, false);
        initView(newView, true);
        AnimatedNumber animAttackForce = newView.findViewById(R.id.animatedNumberAttackForce);
        animAttackForce.init(attackForce, IntValueHolder.WatchType.VALUE);
        AnimatedNumber animShields = newView.findViewById(R.id.animatedNumberShields);
        animShields.init(shields, IntValueHolder.WatchType.VALUE);
        return newView;
    }

    @Override
    public void kill() {}
}
