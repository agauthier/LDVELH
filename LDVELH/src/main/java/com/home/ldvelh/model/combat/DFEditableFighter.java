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

import static com.home.ldvelh.commons.Constants.BIG_POSITIVE;

public class DFEditableFighter extends DFFighter implements EditableFighter {
    private static final long serialVersionUID = 6958589658922976433L;

    private static final int DEFAULT_SKILL = 5;
    private static final int DEFAULT_STAMINA = 5;

    private final IntValueHolder skill;
    private final IntValueHolder stamina;
    private final IntValueHolder bonus;
    private String name = StringUtils.EMPTY;

    DFEditableFighter() {
        super();
        this.skill = new IntValueHolder(0, BIG_POSITIVE, DEFAULT_SKILL);
        this.stamina = new IntValueHolder(0, BIG_POSITIVE, DEFAULT_STAMINA);
        this.bonus = new IntValueHolder(Constants.BIG_NEGATIVE, BIG_POSITIVE, 0);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup root) {
        View newView = inflater.inflate(R.layout.list_item_combat_fighter, root, false);
        initView(newView, false);
        AnimatedNumber animSkill = newView.findViewById(R.id.animatedNumberSkill);
        animSkill.init(getSkill(), WatchType.VALUE);
        AnimatedNumber animStamina = newView.findViewById(R.id.animatedNumberStamina);
        animStamina.init(getStamina(), WatchType.VALUE);
        return newView;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getEditableValue1NameResId() {
        return R.string.skill;
    }

    @Override
    public IntValueHolder getEditableValue1() {
        return getSkill();
    }

    @Override
    public int getEditableValue2NameResId() {
        return R.string.stamina;
    }

    @Override
    public IntValueHolder getEditableValue2() {
        return getStamina();
    }
}
