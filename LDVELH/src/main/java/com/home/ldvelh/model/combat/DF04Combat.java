package com.home.ldvelh.model.combat;

import android.widget.ImageButton;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.DF04Character;
import com.home.ldvelh.model.combat.CombatRow.Team;

public class DF04Combat extends DFCombat {

    @Override
    public <T extends EditableFighter> T createEditableFighter() {
        if (((DF04Character) Property.CHARACTER.get()).isInSpaceFight()) {
            //noinspection unchecked
            return (T) new DF04ShipFighter();
        } else {
            //noinspection unchecked
            return (T) new DFEditableFighter();
        }
    }

    @Override
    public boolean canAssault() {
        if (((DF04Character) Property.CHARACTER.get()).isInSpaceFight()) {
            if (Property.FIGHTER_GRID.get().size() == 1) {
                CombatRow row = Property.FIGHTER_GRID.get().get(0);
                if (row.hasExactlyOneMember(Team.LEFT) && row.hasExactlyOneMember(Team.RIGHT)) {
                    return true;
                }
            }
            return false;
        } else {
            return super.canAssault();
        }
    }

    @SuppressWarnings("unused")
    public boolean canLeavePlanet() {
        return !((DF04Character) Property.CHARACTER.get()).isInSpaceFight() && CombatCore.getAllRightFighters().size() == 0;
    }

    @SuppressWarnings("unused")
    public void leavePlanet(ImageButton button) {
        ((DF04Character) Property.CHARACTER.get()).leavePlanet();
        CombatCore.reset();
    }

    @SuppressWarnings("unused")
    public boolean canPhaser() {
        return !((DF04Character) Property.CHARACTER.get()).isInSpaceFight();
    }

    @SuppressWarnings("unused")
    public void phaser(ImageButton button) {
        final DF04Character character = (DF04Character) Property.CHARACTER.get();
        character.togglePhaserState();
        button.setImageResource(character.getPhaserState().getResId());
    }
}
