package com.home.ldvelh.model.combat.strategy;

import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.CombatRow;
import com.home.ldvelh.model.combat.Fighter;

import java.util.HashMap;
import java.util.Map;

public enum DFCombatStrategy implements CombatStrategy {
    INSTANCE;

    private static final int REGULAR_WOUND = -2;

    @Override public void escape() {
        Property.STAMINA.get().addWithFeedback(REGULAR_WOUND);
    }

    @Override public void assault() {
        Map<Fighter, Integer> fighterDamage = new HashMap<>();
        for (CombatRow row : Property.FIGHTER_GRID.get()) {
            if (row.canAssault()) {
                rowAssault(row, fighterDamage);
            }
        }
        applyDamage(fighterDamage);
    }

    private void rowAssault(CombatRow row, Map<Fighter, Integer> fighterDamage) {
        for (int i = 0; i < row.getTeamLeft().size(); i++) {
            for (int j = 0; j < row.getTeamRight().size(); j++) {
                Fighter leftFighter = row.getTeamLeft().get(i);
                Fighter rightFighter = row.getTeamRight().get(j);
                int leftFighterAttackForce = leftFighter.getSkill().getValue() + Die.SIX_FACES.roll(2) + leftFighter.getBonus().getValue();
                int rightFighterAttackForce = rightFighter.getSkill().getValue() + Die.SIX_FACES.roll(2) + rightFighter.getBonus().getValue();
                if (leftFighterAttackForce > rightFighterAttackForce && j == 0) {
                    addFighterDamage(fighterDamage, rightFighter);
                } else if (rightFighterAttackForce > leftFighterAttackForce && i == 0) {
                    addFighterDamage(fighterDamage, leftFighter);
                }
            }
        }
    }

    private void addFighterDamage(Map<Fighter, Integer> fighterDamage, Fighter fighter) {
        Integer totalDamage = fighterDamage.get(fighter);
        if (totalDamage == null) {
            fighterDamage.put(fighter, REGULAR_WOUND);
        } else {
            fighterDamage.put(fighter, totalDamage + REGULAR_WOUND);
        }
    }

    private void applyDamage(Map<Fighter, Integer> fighterDamage) {
        for (Fighter fighter : fighterDamage.keySet()) {
            fighter.getStamina().addWithFeedback(fighterDamage.get(fighter));
        }
    }
}
