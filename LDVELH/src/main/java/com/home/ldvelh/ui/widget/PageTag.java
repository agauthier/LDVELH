package com.home.ldvelh.ui.widget;

import com.home.ldvelh.ui.widget.utility.Dice;
import com.home.ldvelh.ui.widget.utility.LastParagraph;
import com.home.ldvelh.ui.widget.utility.Libra;
import com.home.ldvelh.ui.widget.utility.LuckCheck;
import com.home.ldvelh.ui.widget.utility.Zeus;

import static com.home.ldvelh.ui.page.CCCombatPage.CC_KILL_BUTTON_DATA;
import static com.home.ldvelh.ui.page.CCCombatPage.CC_SURRENDER_BUTTON_DATA;
import static com.home.ldvelh.ui.page.DF04CombatPage.LEAVE_PLANET_BUTTON_DATA;
import static com.home.ldvelh.ui.page.DF04CombatPage.PHASER_BUTTON_DATA;
import static com.home.ldvelh.ui.page.CombatPage.ASSAULT_BUTTON_DATA;
import static com.home.ldvelh.ui.page.CombatPage.ESCAPE_BUTTON_DATA;
import static com.home.ldvelh.ui.widget.PageTag.TagType.COMBAT_BUTTON;
import static com.home.ldvelh.ui.widget.PageTag.TagType.SIMPLE;
import static com.home.ldvelh.ui.widget.PageTag.TagType.UTILITY;

public enum PageTag {
    ALLOW_DROP(SIMPLE, null),
    COMBAT_BUTTON_ASSAULT(COMBAT_BUTTON, ASSAULT_BUTTON_DATA),
    COMBAT_BUTTON_ESCAPE(COMBAT_BUTTON, ESCAPE_BUTTON_DATA),
    COMBAT_BUTTON_CC_KILL(COMBAT_BUTTON, CC_KILL_BUTTON_DATA),
    COMBAT_BUTTON_CC_SURRENDER(COMBAT_BUTTON, CC_SURRENDER_BUTTON_DATA),
    COMBAT_BUTTON_DF04_LEAVE_PLANET(COMBAT_BUTTON, LEAVE_PLANET_BUTTON_DATA),
    COMBAT_BUTTON_DF04_PHASER(COMBAT_BUTTON, PHASER_BUTTON_DATA),
    UTILITY_LAST_PARAGRAPH(UTILITY, LastParagraph.class),
    UTILITY_DICE(UTILITY, Dice.class),
    UTILITY_LUCK_CHECK(UTILITY, LuckCheck.class),
    UTILITY_ZEUS(UTILITY, Zeus.class),
    UTILITY_LIBRA(UTILITY, Libra.class);

    public enum TagType {
        SIMPLE, UTILITY, COMBAT_BUTTON
    }

    private final TagType type;
    private final Object data;

    PageTag(TagType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public TagType getType() { return type; }

    public Object getData() { return data; }
}

