package com.home.ldvelh.ui.page;

import com.home.ldvelh.R;
import com.home.ldvelh.model.combat.CCCombat;

import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_HELP;
import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_KILL;
import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_SURRENDER;

public class CCCombatPage extends CombatPage {

    public static final CombatButtonData CC_HELP_BUTTON_DATA = new CombatButtonData(R.drawable.help, METHOD_COMBAT_BUTTON_HELP);
    public static final CombatButtonData CC_KILL_BUTTON_DATA = new CombatButtonData(R.drawable.kill, METHOD_COMBAT_BUTTON_KILL);
    public static final CombatButtonData CC_SURRENDER_BUTTON_DATA = new CombatButtonData(R.drawable.surrender, METHOD_COMBAT_BUTTON_SURRENDER);

    @Override
    void initCombat() {
        combat = new CCCombat();
        combat.init();
    }
}
