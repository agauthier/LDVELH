package com.home.ldvelh.model.combat;

import android.widget.ImageButton;

public class CCCombat extends DFCombat {

    @Override
    public <T extends EditableFighter> T createEditableFighter() {
        //noinspection unchecked
        return (T) new CCEditableFighter();
    }

    @SuppressWarnings("unused")
    public boolean canHelp() {
        return false;
    }

    @SuppressWarnings("unused")
    public void help(ImageButton button) {}

    @SuppressWarnings("unused")
    public boolean canKill() {
        return false;
    }

    @SuppressWarnings("unused")
    public void kill(ImageButton button) {}

    @SuppressWarnings("unused")
    public boolean canSurrender() {
        return false;
    }

    @SuppressWarnings("unused")
    public void surrender(ImageButton button) {}
}
