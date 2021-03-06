package com.home.ldvelh.model.combat.strategy;

import com.home.ldvelh.commons.Die;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.CombatRow;
import com.home.ldvelh.model.combat.DFFighter;

import java.util.HashMap;
import java.util.Map;

public class DFCombatStrategy<T extends DFFighter> implements CombatStrategy {

    public static final DFCombatStrategy INSTANCE = new DFCombatStrategy();
    private static final int REGULAR_WOUND = -2;

    private DFCombatStrategy() {}

    @Override public void escape() {
        Property.STAMINA.get().addWithFeedback(REGULAR_WOUND);
    }

    @Override public void assault() {
        Map<T, Integer> fighterDamage = new HashMap<>();
        for (CombatRow row : Property.FIGHTER_GRID.get()) {
            if (row.canAssault()) {
                rowAssault(row, fighterDamage);
            }
        }
        applyDamage(fighterDamage);
    }

    private void rowAssault(CombatRow row, Map<T, Integer> fighterDamage) {
        for (int i = 0; i < row.getTeamLeft().size(); i++) {
            for (int j = 0; j < row.getTeamRight().size(); j++) {
                @SuppressWarnings("unchecked") T leftFighter = (T) row.getTeamLeft().get(i);
                @SuppressWarnings("unchecked") T rightFighter = (T) row.getTeamRight().get(j);
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

    private void addFighterDamage(Map<T, Integer> fighterDamage, T fighter) {
        Integer totalDamage = fighterDamage.get(fighter);
        if (totalDamage == null) {
            fighterDamage.put(fighter, REGULAR_WOUND);
        } else {
            fighterDamage.put(fighter, totalDamage + REGULAR_WOUND);
        }
    }

    private void applyDamage(Map<T, Integer> fighterDamage) {
        for (T fighter : fighterDamage.keySet()) {
            fighter.getStamina().addWithFeedback(fighterDamage.get(fighter));
        }
    }
}
