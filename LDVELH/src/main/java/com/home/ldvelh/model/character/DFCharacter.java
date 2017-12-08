package com.home.ldvelh.model.character;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.DFCharacterFighter;
import com.home.ldvelh.model.combat.Fighter;
import com.home.ldvelh.model.combat.strategy.CombatStrategy;
import com.home.ldvelh.model.combat.strategy.DFCombatStrategy;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.EffectTarget;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.activity.AdventureActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DFCharacter extends Character {
    private static final long serialVersionUID = 3561520215039491885L;

    final IntValueHolder skill;
    final IntValueHolder stamina;
    final IntValueHolder luck;
    private final IntValueHolder gold;
    private final IntValueHolder attackBonus;
    private final IntValueHolder luckPenalty;

    DFCharacter() {
        int maxSkill = 6 + Die.SIX_FACES.roll();
        skill = new IntValueHolder(0, maxSkill, maxSkill);
        int maxStamina = 12 + Die.SIX_FACES.roll(2);
        stamina = new IntValueHolder(0, maxStamina, maxStamina);
        int maxLuck = 6 + Die.SIX_FACES.roll();
        luck = new IntValueHolder(0, maxLuck, maxLuck);
        gold = new IntValueHolder(0, Constants.BIG_POSITIVE, 0);
        attackBonus = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
        luckPenalty = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, -1);
    }

    @Override public void addLifeObserver(AdventureActivity activity) {
        stamina.addObserver(this);
        addObserver(activity);
    }

    @Override public void deleteLifeObserver(AdventureActivity activity) {
        deleteObserver(activity);
        stamina.deleteObserver(this);
    }

    @Override public void killWithoutUpdate() {
        stamina.deleteObservers();
        stamina.add(Constants.BIG_NEGATIVE);
    }

    @Override public boolean isDead() {
        return stamina.getValue() <= 0;
    }

    @Override public CombatStrategy getCombatStrategy() {
        return DFCombatStrategy.INSTANCE;
    }

    @Override
    public List<Fighter> getFighters() {
        List<Fighter> fighters = new ArrayList<>();
        fighters.add(new DFCharacterFighter());
        return fighters;
    }

    @SuppressWarnings("unused") public void setGold(Integer gold) {
        this.gold.add(gold - this.gold.getValue());
    }

    @SuppressWarnings("unused") public void addConsumableFood(String name, Integer quantity) {
        for (int i = 0; i < quantity; i++) {
            Property.ITEM_LIST.get().addNewItem(name, Collections.singletonList(new Effect(EffectTarget.STAMINA, 4)));
        }
    }

    @Override public void initCharacterValues() {
        super.initCharacterValues();
        CharacterValues.put(Property.SKILL, skill);
        CharacterValues.put(Property.STAMINA, stamina);
        CharacterValues.put(Property.LUCK, luck);
        CharacterValues.put(Property.GOLD, gold);
        CharacterValues.put(Property.ATTACK_BONUS, attackBonus);
        CharacterValues.put(Property.LUCK_PENALTY, luckPenalty);
    }
}
