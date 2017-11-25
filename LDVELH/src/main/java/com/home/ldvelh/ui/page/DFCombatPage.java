package com.home.ldvelh.ui.page;

import com.home.ldvelh.model.combat.DFCombat;

public class DFCombatPage extends CombatPage {

    @Override
    public void initCombat() {
        combat = new DFCombat();
        combat.init();
    }
}
