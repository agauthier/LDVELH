package com.home.ldvelh.model.combat.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.DF04Character;
import com.home.ldvelh.model.combat.CombatCore;
import com.home.ldvelh.model.combat.CombatRow;
import com.home.ldvelh.model.combat.Fighter;

public enum DF04CombatStrategy implements CombatStrategy {
	INSTANCE;

	private static final int REGULAR_WOUND = -2;
	private static final int GREAT_WOUND = -4;
	private static final int WORST_WOUND = -6;
	private static final int TWO_DICE_MAX_ROLL = 12;
	private static final Random random = new Random();

	@Override public void escape() {}

	@Override public void assault() {
		System.out.println("---- Assault ----");
		Map<Fighter, Integer> fighterDamage = new HashMap<>();
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

	private void spaceFight(Map<Fighter, Integer> fighterDamage) {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.canAssault()) {
				rowAssaultInSpace(row, fighterDamage);
			}
		}
	}
	
	private void rowAssaultInSpace(CombatRow row, Map<Fighter, Integer> fighterDamage) {
		Fighter goodShip = row.getGoodguys().get(0);
		shipFires(row, fighterDamage, goodShip);
		Fighter badShip = row.getMonsters().get(0);
		if (isAlive(badShip, fighterDamage)) {
			shipFires(row, fighterDamage, badShip);
		}
	}

	private void shipFires(CombatRow row, Map<Fighter, Integer> fighterDamage, Fighter attackingShip) {
		if (Die.SIX_FACES.roll(2) < attackingShip.getSkill().getValue() + attackingShip.getBonus().getValue()) {
			Fighter enemyShip = row.getFighters(attackingShip.getTeam().facingTeam()).get(0);
			int damageRoll = Die.SIX_FACES.roll(2);
			System.out.println(attackingShip.getName() + " VS " + enemyShip.getName() + " : " + damageRoll);
			if (damageRoll == TWO_DICE_MAX_ROLL) {
				addFighterDamage(fighterDamage, enemyShip, WORST_WOUND);
			} else if (damageRoll > enemyShip.getStamina().getValue()) {
				addFighterDamage(fighterDamage, enemyShip, GREAT_WOUND);
			} else {
				addFighterDamage(fighterDamage, enemyShip, REGULAR_WOUND);
			}
		}
	}

	private void combatNoPhaser(Map<Fighter, Integer> fighterDamage) {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.canAssault()) {
				rowAssaultNoPhaser(row, fighterDamage);
			}
		}
	}

	private void rowAssaultNoPhaser(CombatRow row, Map<Fighter, Integer> fighterDamage) {
		boolean firstGoodguy = true;
		for (Fighter goodguy : row.getGoodguys()) {
			int goodguyAttackForce = goodguy.getSkill().getValue() + Die.SIX_FACES.roll(2) + goodguy.getBonus().getValue();
			boolean firstMonster = true;
			for (Fighter monster : row.getMonsters()) {
				int monsterAttackForce = monster.getSkill().getValue() + Die.SIX_FACES.roll(2) + monster.getBonus().getValue();
				if (firstMonster && goodguyAttackForce > monsterAttackForce) {
					addFighterDamage(fighterDamage, monster, REGULAR_WOUND);
				} else if (firstGoodguy && monsterAttackForce > goodguyAttackForce) {
					addFighterDamage(fighterDamage, goodguy, REGULAR_WOUND);
				}
				firstMonster = false;
			}
			firstGoodguy = false;
		}
	}

	private void combatWithPhaser(Map<Fighter, Integer> fighterDamage) {
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.canAssault()) {
				goodguysRowAssault(row, fighterDamage);
			}
		}
		monstersAssault(fighterDamage);
	}

	private void goodguysRowAssault(CombatRow row, Map<Fighter, Integer> fighterDamage) {
		for (Fighter goodguy : row.getGoodguys()) {
			Fighter monster = row.getMonsters().get(0);
			if (isAlive(monster, fighterDamage)) {
				int roll = Die.SIX_FACES.roll(2);
				System.out.println(goodguy.getName() + " VS " + monster.getName() + " : " + roll);
				if (roll < goodguy.getSkill().getValue() + goodguy.getBonus().getValue()) {
					addFighterDamage(fighterDamage, monster, Constants.BIG_NEGATIVE);
				}
			}
		}
	}

	private void monstersAssault(Map<Fighter, Integer> fighterDamage) {
		for (Fighter monster : CombatCore.getAllMonsters()) {
			if (isAlive(monster, fighterDamage)) {
				List<Fighter> attackableGoodguys = getAttackableGoodguys(fighterDamage);
				if (!attackableGoodguys.isEmpty()) {
					Fighter randomGoodguy = attackableGoodguys.get(random.nextInt(attackableGoodguys.size()));
					int roll = Die.SIX_FACES.roll(2);
					System.out.println(monster.getName() + " VS " + randomGoodguy.getName() + " : " + roll);
					if (roll < monster.getSkill().getValue() + monster.getBonus().getValue()) {
						addFighterDamage(fighterDamage, randomGoodguy, Constants.BIG_NEGATIVE);
					}
				}
			}
		}
	}

	private int getFighterDamage(Map<Fighter, Integer> fighterDamage, Fighter fighter) {
		Integer damage = fighterDamage.get(fighter);
		return (damage == null) ? 0 : damage;
	}

	private void addFighterDamage(Map<Fighter, Integer> fighterDamage, Fighter fighter, int damage) {
		Integer totalDamage = fighterDamage.get(fighter);
		if (totalDamage == null) {
			fighterDamage.put(fighter, damage);
		} else {
			fighterDamage.put(fighter, totalDamage + damage);
		}
	}

	private boolean isAlive(Fighter fighter, Map<Fighter, Integer> fighterDamage) {
		return fighter.getStamina().getValue() + getFighterDamage(fighterDamage, fighter) > 0;
	}

	private List<Fighter> getAttackableGoodguys(Map<Fighter, Integer> fighterDamage) {
		List<Fighter> goodguys = new ArrayList<>();
		for (CombatRow row : Property.FIGHTER_GRID.get()) {
			if (row.canAssault()) {
				for (Fighter goodguy : row.getGoodguys()) {
					if (isAlive(goodguy, fighterDamage)) {
						goodguys.add(goodguy);
					}
				}
			}
		}
		return goodguys;
	}

	private void applyDamage(Map<Fighter, Integer> fighterDamage) {
		for (Fighter fighter : fighterDamage.keySet()) {
			fighter.getStamina().addWithFeedback(fighterDamage.get(fighter));
		}
	}
}
