package com.home.ldvelh.ui.page;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.DF04Character;

import static com.home.ldvelh.commons.Constants.METHOD_CHECK_PREFIX;
import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_ASSAULT;
import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_ESCAPE;
import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_HELP;
import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_KILL;
import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_LEAVE_PLANET;
import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_PHASER;
import static com.home.ldvelh.commons.Constants.METHOD_COMBAT_BUTTON_SURRENDER;

public enum CombatButtonData {

    ASSAULT_BUTTON_DATA(R.drawable.assault, METHOD_COMBAT_BUTTON_ASSAULT),
    ESCAPE_BUTTON_DATA(R.drawable.escape, METHOD_COMBAT_BUTTON_ESCAPE),
    CC_HELP_BUTTON_DATA(R.drawable.help, METHOD_COMBAT_BUTTON_HELP),
    CC_KILL_BUTTON_DATA(R.drawable.kill, METHOD_COMBAT_BUTTON_KILL),
    CC_SURRENDER_BUTTON_DATA(R.drawable.surrender, METHOD_COMBAT_BUTTON_SURRENDER),
    DF04_LEAVE_PLANET_BUTTON_DATA(R.drawable.leave_planet, METHOD_COMBAT_BUTTON_LEAVE_PLANET),
    DF04_PHASER_BUTTON_DATA(R.drawable.phaser_none, METHOD_COMBAT_BUTTON_PHASER) {
        @Override
        int getImageResId() {
            return ((DF04Character) Property.CHARACTER.get()).getPhaserState().getResId();
        }
    };

    private final int imageResId;
    private final String operationName;

    CombatButtonData(int imageResId, String operationName) {
        this.imageResId = imageResId;
        this.operationName = operationName;
    }

    int getImageResId() { return imageResId; }

    String getOperationName() { return operationName; }

    String getCheckOperationName() {
        return METHOD_CHECK_PREFIX + operationName.substring(0, 1).toUpperCase() + operationName.substring(1, operationName.length());
    }
}
