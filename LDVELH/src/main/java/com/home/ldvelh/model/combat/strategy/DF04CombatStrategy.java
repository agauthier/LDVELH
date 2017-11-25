package com.home.ldvelh.model.combat.strategy;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.DF04Character;
import com.home.ldvelh.model.combat.CombatCore;
import com.home.ldvelh.model.combat.CombatRow;
import com.home.ldvelh.model.combat.DFFighter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DF04CombatStrategy<T extends DFFighter> implements CombatStrategy {

    public static final DF04CombatStrategy INSTANCE = new DF04CombatStrategy();

    private static final int REGULAR_WOUND = -2;
    private static final int GREAT_WOUND = -4;
    private static final int WORST_WOUND = -6;
    private static final int TWO_DICE_MAX_ROLL = 12;
    private static final Random random = new Random();

    private DF04CombatStrategy() {}

    @Override
    public void escape() {}

    @Override
    public void assault() {
        Map<T, Integer> fighterDamage = new HashMap<>();
        if (((DF04Character) Property.CHARACTER.get()).isInSpaceFight()) {
            spaceFight(fighterDamage);
        } else {
            switch (((DF04Character) Property.CHARACTER.get()).getPhaserState()) {
                case NONE:
                    combatNoPhaser(fighterDamage);
                    break;
                case PARALYZE:
                case DEATH:
                    combatWithPhaser(fighterDamage);
                    break;
                default:
                    break;
            }
        }
        applyDamage(fighterDamage);
    }

    private void spaceFight(Map<T, Integer> fighterDamage) {
        for (@SuppressWarnings("unchecked") CombatRow<T> row : Property.FIGHTER_GRID.get()) {
            if (row.canAssault()) {
                rowAssaultInSpace(row, fighterDamage);
            }
        }
    }

    private void rowAssaultInSpace(CombatRow<T> row, Map<T, Integer> fighterDamage) {
        T leftShip = row.getTeamLeft().get(0);
        shipFires(row, fighterDamage, leftShip);
        T rightShip = row.getTeamRight().get(0);
        if (isAlive(rightShip, fighterDamage)) {
            shipFires(row, fighterDamage, rightShip);
        }
    }

    private void shipFires(CombatRow<T> row, Map<T, Integer> fighterDamage, T attackingShip) {
        if (Die.SIX_FACES.roll(2) < attackingShip.getSkill().getValue() + attackingShip.getBonus().getValue()) {
            T facingShip = row.getFighters(row.getTeam(attackingShip).facingTeam()).get(0);
            int damageRoll = Die.SIX_FACES.roll(2);
            if (damageRoll == TWO_DICE_MAX_ROLL) {
                addFighterDamage(fighterDamage, facingShip, WORST_WOUND);
            } else if (damageRoll > facingShip.getStamina().getValue()) {
                addFighterDamage(fighterDamage, facingShip, GREAT_WOUND);
            } else {
                addFighterDamage(fighterDamage, facingShip, REGULAR_WOUND);
            }
        }
    }

    private void combatNoPhaser(Map<T, Integer> fighterDamage) {
        for (@SuppressWarnings("unchecked") CombatRow<T> row : Property.FIGHTER_GRID.get()) {
            if (row.canAssault()) {
                rowAssaultNoPhaser(row, fighterDamage);
            }
        }
    }

    private void rowAssaultNoPhaser(CombatRow<T> row, Map<T, Integer> fighterDamage) {
        boolean firstLeftFighter = true;
        for (T leftFighter : row.getTeamLeft()) {
            int leftFighterAttackForce = leftFighter.getSkill().getValue() + Die.SIX_FACES.roll(2) + leftFighter.getBonus().getValue();
            boolean firstRightFighter = true;
            for (T rightFighter : row.getTeamRight()) {
                int rightFighterAttackForce = rightFighter.getSkill().getValue() + Die.SIX_FACES.roll(2) + rightFighter.getBonus().getValue();
                if (firstRightFighter && leftFighterAttackForce > rightFighterAttackForce) {
                    addFighterDamage(fighterDamage, rightFighter, REGULAR_WOUND);
                } else if (firstLeftFighter && rightFighterAttackForce > leftFighterAttackForce) {
                    addFighterDamage(fighterDamage, leftFighter, REGULAR_WOUND);
                }
                firstRightFighter = false;
            }
            firstLeftFighter = false;
        }
    }

    private void combatWithPhaser(Map<T, Integer> fighterDamage) {
        for (@SuppressWarnings("unchecked") CombatRow<T> row : Property.FIGHTER_GRID.get()) {
            if (row.canAssault()) {
                leftTeamAssault(row, fighterDamage);
            }
        }
        rightTeamAssault(fighterDamage);
    }

    private void leftTeamAssault(CombatRow<T> row, Map<T, Integer> fighterDamage) {
        for (T leftFighter : row.getTeamLeft()) {
            T rightFighter = row.getTeamRight().get(0);
            if (isAlive(rightFighter, fighterDamage)) {
                int roll = Die.SIX_FACES.roll(2);
                if (roll < leftFighter.getSkill().getValue() + leftFighter.getBonus().getValue()) {
                    addFighterDamage(fighterDamage, rightFighter, Constants.BIG_NEGATIVE);
                }
            }
        }
    }

    private void rightTeamAssault(Map<T, Integer> fighterDamage) {
        for (T rightFighter : CombatCore.<T>getAllRightFighters()) {
            if (isAlive(rightFighter, fighterDamage)) {
                List<T> attackableLeftFighters = getAttackableLeftFighters(fighterDamage);
                if (!attackableLeftFighters.isEmpty()) {
                    T randomLeftFighter = attackableLeftFighters.get(random.nextInt(attackableLeftFighters.size()));
                    int roll = Die.SIX_FACES.roll(2);
                    if (roll < rightFighter.getSkill().getValue() + rightFighter.getBonus().getValue()) {
                        addFighterDamage(fighterDamage, randomLeftFighter, Constants.BIG_NEGATIVE);
                    }
                }
            }
        }
    }

    private int getFighterDamage(Map<T, Integer> fighterDamage, T fighter) {
        Integer damage = fighterDamage.get(fighter);
        return (damage == null) ? 0 : damage;
    }

    private void addFighterDamage(Map<T, Integer> fighterDamage, T fighter, int damage) {
        Integer totalDamage = fighterDamage.get(fighter);
        if (totalDamage == null) {
            fighterDamage.put(fighter, damage);
        } else {
            fighterDamage.put(fighter, totalDamage + damage);
        }
    }

    private boolean isAlive(T fighter, Map<T, Integer> fighterDamage) {
        return fighter.getStamina().getValue() + getFighterDamage(fighterDamage, fighter) > 0;
    }

    private List<T> getAttackableLeftFighters(Map<T, Integer> fighterDamage) {
        List<T> leftFighters = new ArrayList<>();
        for (@SuppressWarnings("unchecked") CombatRow<T> row : Property.FIGHTER_GRID.get()) {
            if (row.canAssault()) {
                for (T leftFighter : row.getTeamLeft()) {
                    if (isAlive(leftFighter, fighterDamage)) {
                        leftFighters.add(leftFighter);
                    }
                }
            }
        }
        return leftFighters;
    }

    private void applyDamage(Map<T, Integer> fighterDamage) {
        for (T fighter : fighterDamage.keySet()) {
            fighter.getStamina().addWithFeedback(fighterDamage.get(fighter));
        }
    }
}
