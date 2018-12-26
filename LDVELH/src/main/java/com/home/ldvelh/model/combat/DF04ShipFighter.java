package com.home.ldvelh.model.combat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.widget.AnimatedNumber;

import org.apache.commons.lang3.StringUtils;

class DF04ShipFighter extends DFFighter implements EditableFighter {
    private static final long serialVersionUID = 1484728379235320964L;

    private static final int DEFAULT_SKILL = 5;
    private static final int DEFAULT_STAMINA = 5;

    private final IntValueHolder attackForce;
    private final IntValueHolder shields;
    private final IntValueHolder bonus;
    private String name;

    DF04ShipFighter() {
        super();
        this.name = StringUtils.EMPTY;
        this.attackForce = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_SKILL);
        this.shields = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_STAMINA);
        this.bonus = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup root) {
        View newView = inflater.inflate(R.layout.list_item_combat_df04_ship_fighter, root, false);
        initView(newView, false);
        AnimatedNumber animAttackForce = newView.findViewById(R.id.animatedNumberAttackForce);
        animAttackForce.init(attackForce, IntValueHolder.WatchType.VALUE);
        AnimatedNumber animShields = newView.findViewById(R.id.animatedNumberShields);
        animShields.init(shields, IntValueHolder.WatchType.VALUE);
        return newView;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getEditableValue1NameResId() {
        return R.string.df04_shipAttackForce;
    }

    @Override
    public IntValueHolder getEditableValue1() {
        return getSkill();
    }

    @Override
    public int getEditableValue2NameResId() {
        return R.string.df04_shipShields;
    }

    @Override
    public IntValueHolder getEditableValue2() {
        return getStamina();
    }
}
