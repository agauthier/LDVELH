package com.home.ldvelh.model.combat;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.DF04Character;
import com.home.ldvelh.model.combat.CombatRow.Team;

public class DF04Combat extends DFCombat {

    @Override
    public void addEditableFighter(Team team) {
        if (((DF04Character) Property.CHARACTER.get()).isInSpaceFight()) {
            CombatCore.addEditableFighter(new DF04ShipFighter(team));
        } else {
            super.addEditableFighter(team);
        }
    }

    @Override
    public boolean canAssault() {
        if (((DF04Character) Property.CHARACTER.get()).isInSpaceFight()) {
            if (Property.FIGHTER_GRID.get().size() == 1) {
                CombatRow row = Property.FIGHTER_GRID.get().get(0);
                if (row.hasExactlyOneMember(Team.GOODGUYS) && row.hasExactlyOneMember(Team.MONSTERS)) {
                    return true;
                }
            }
            return false;
        } else {
            return super.canAssault();
        }
    }

    public boolean canLeavePlanet() {
        return !((DF04Character) Property.CHARACTER.get()).isInSpaceFight() && CombatCore.getAllMonsters().size() == 0;
    }

    public void leavePlanet() {
        ((DF04Character) Property.CHARACTER.get()).leavePlanet();
        CombatCore.reset();
    }

    public boolean canUsePhaser() {
        return !((DF04Character) Property.CHARACTER.get()).isInSpaceFight();
    }
}
