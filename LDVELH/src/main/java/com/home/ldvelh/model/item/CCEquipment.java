package com.home.ldvelh.model.item;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.value.IntValueHolder;

import org.apache.commons.lang3.StringUtils;

public class CCEquipment extends Item {
    private static final long serialVersionUID = 8460193493459850797L;

    private final IntValueHolder strength = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
    private final IntValueHolder protection = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
    private boolean equipped = false;

    public CCEquipment() {
        this(StringUtils.EMPTY);
    }

    public CCEquipment(String name) {
        super(name, null);
    }

    @Override
    public boolean isIdentical(Item item) {
        return super.isIdentical(item) && strength.getValue() == ((CCEquipment) item).strength.getValue() && protection.getValue() == ((CCEquipment) item).protection.getValue();
    }

    public IntValueHolder getStrength() {
        return strength;
    }

    public IntValueHolder getProtection() {
        return protection;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }
}
