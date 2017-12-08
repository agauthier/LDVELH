package com.home.ldvelh.model.item;

import java.util.List;

import static com.home.ldvelh.commons.Constants.QTY_SEPARATOR;

public class Spell extends Item {
    private static final long serialVersionUID = 2839669026799276167L;

    private int nbCharges = 0;
    private boolean doubleCastAllowed = false;

    private Spell() { super(); }

    @SuppressWarnings("unused")
    private Spell(String name, List<Effect> effects, Object data) {
        super(name, effects, data);
    }

    @Override
    public <T extends Item> T copy() {
        Spell spell = new Spell();
        populate(spell);
        //noinspection unchecked
        return (T) spell;
    }

    @Override
    public String getName() {
        return super.getName() + QTY_SEPARATOR + nbCharges;
    }

    public void addCharges(int nbCharges) {
        this.nbCharges += nbCharges;
    }

    public void removeCharges(int nbCharges) {
        this.nbCharges -= nbCharges;
    }

    public boolean hasNoChargesLeft() {
        return nbCharges <= 0;
    }

    public void allowDoubleCast() {
        this.doubleCastAllowed = true;
    }

    public boolean canCastTwice() {
        return doubleCastAllowed && nbCharges >= 2;
    }

    @Override
    protected void populate(Item item) {
        Spell spell = (Spell) item;
        super.populate(spell);
        spell.nbCharges = nbCharges;
        spell.doubleCastAllowed = doubleCastAllowed;
    }
}
