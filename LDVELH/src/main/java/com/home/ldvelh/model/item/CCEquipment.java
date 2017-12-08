package com.home.ldvelh.model.item;

import java.util.List;

public class CCEquipment extends Item {
    private static final long serialVersionUID = -1563773358827185825L;

    private int strength = 0;
    private int protection = 0;
    private boolean equipped = false;

    private CCEquipment() { super(); }

    @SuppressWarnings("unused")
    private CCEquipment(String name, List<Effect> effects, Object data) {
        super(name, effects, data);
    }

    @Override
    public <T extends Item> T copy() {
        CCEquipment equipment = new CCEquipment();
        populate(equipment);
        //noinspection unchecked
        return (T) equipment;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getProtection() {
        return protection;
    }

    public void setProtection(int protection) {
        this.protection = protection;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    @Override
    protected void populate(Item item) {
        CCEquipment equipment = (CCEquipment) item;
        super.populate(equipment);
        equipment.strength = strength;
        equipment.protection = protection;
    }
}
