package com.home.ldvelh.ui.page;

import com.home.ldvelh.model.combat.CCCombat;

public class CCCombatPage extends CombatPage {

    @Override
    public void initCombat() {
        combat = new CCCombat();
        combat.init();
    }
}
