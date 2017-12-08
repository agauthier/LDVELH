package com.home.ldvelh.model.character;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.CCCharacterFighter;
import com.home.ldvelh.model.combat.CCCondition;
import com.home.ldvelh.model.combat.Fighter;
import com.home.ldvelh.model.combat.strategy.CombatStrategy;
import com.home.ldvelh.model.item.CCEquipment;
import com.home.ldvelh.model.item.CCGod;
import com.home.ldvelh.model.item.CCGod.Attitude;
import com.home.ldvelh.model.item.CCGod.God;
import com.home.ldvelh.model.value.BooleanValueHolder;
import com.home.ldvelh.model.value.EnumValueHolder;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;
import com.home.ldvelh.ui.activity.AdventureActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static com.home.ldvelh.model.combat.CCCondition.DEAD;
import static com.home.ldvelh.model.combat.CCCondition.UNSCATHED;

public class CCCharacter extends Character {
    private static final long serialVersionUID = -8802801187643769352L;

    public enum ZeusInvocation {
        RESUSCITATE {
            @Override
            public boolean canPerform(CCCharacter character) {
                return !character.shameGreaterThanHonor();
            }

            @Override
            public boolean perform(CCCharacter character) {
                if (character.honor.getValue() == 0) {
                    recoverFromZeroHonor(character);
                } else {
                    character.shame.addWithFeedback(-character.shame.getValue());
                    character.honor.addWithFeedback(-character.honor.getValue() + 1);
                }
                return true;
            }
        }, GAIN_RANDOM_HONOR {
            @Override
            public boolean canPerform(CCCharacter character) {
                return character.honor.getValue() > 0;
            }

            @Override
            public boolean perform(CCCharacter character) {
                character.honor.addWithFeedback(Die.SIX_FACES.roll());
                return true;
            }
        }, REGAIN_HONOR_POINTS {
            @Override
            public boolean canPerform(CCCharacter character) {
                return character.honor.getValue() == 0;
            }

            @Override
            public boolean perform(CCCharacter character) {
                recoverFromZeroHonor(character);
                return true;
            }
        }, ALL_GODS_NEUTRAL {
            @Override
            public boolean canPerform(CCCharacter character) {
                return true;
            }

            @Override
            public boolean perform(CCCharacter character) {
                ListValueHolder<CCGod> godsList = Property.CC_GOD_LIST.get();
                for (CCGod god : godsList) {
                    god.setAttitude(Attitude.NEUTRAL);
                }
                godsList.touch();
                return true;
            }
        }, CANCEL {
            @Override
            public boolean canPerform(CCCharacter character) {
                return true;
            }

            @Override
            public boolean perform(CCCharacter character) {
                return false;
            }
        };

        public abstract boolean canPerform(CCCharacter character);

        public abstract boolean perform(CCCharacter character);

        void recoverFromZeroHonor(CCCharacter character) {
            character.honor.deleteObserver(character);
            character.honor.addToMax(Constants.BIG_POSITIVE);
            character.honor.addWithFeedback(1);
            character.honor.addObserver(character);
        }
    }

    private final IntValueHolder strength;
    private final IntValueHolder protection;
    private final IntValueHolder honor;
    private final IntValueHolder shame;
    private final IntValueHolder endurance;
    private final IntValueHolder intelligence;
    private final EnumValueHolder<CCCondition> condition;
    private final ListValueHolder<CCEquipment> equipment;
    private final ListValueHolder<CCGod> gods;
    private final EnumValueHolder<God> tutelaryGod;
    private final BooleanValueHolder zeusInvoked = new BooleanValueHolder();

    public CCCharacter() {
        strength = new IntValueHolder(0, Constants.BIG_POSITIVE, 4);
        protection = new IntValueHolder(0, Constants.BIG_POSITIVE, 10);
        honor = new IntValueHolder(0, Constants.BIG_POSITIVE, 7);
        shame = new IntValueHolder(0, Constants.BIG_POSITIVE, 0);
        endurance = new IntValueHolder(0, Constants.BIG_POSITIVE, 0);
        intelligence = new IntValueHolder(0, Constants.BIG_POSITIVE, 0);
        condition = new EnumValueHolder<>(UNSCATHED);
        equipment = new ListValueHolder<>(CCEquipment.class);
        gods = new ListValueHolder<>(CCGod.class);
        tutelaryGod = new EnumValueHolder<>(God.NO_GOD);
        God.populateList(gods);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable == honor && honor.getValue() == 0 && honor.getMax() != 0) {
            honor.addToMax(-honor.getMax());
        }
        super.update(observable, data);
    }

    @Override
    public void addLifeObserver(AdventureActivity activity) {
        honor.addObserver(this);
        shame.addObserver(this);
        condition.addObserver(this);
        addObserver(activity);
    }

    @Override
    public void deleteLifeObserver(AdventureActivity activity) {
        deleteObserver(activity);
        honor.deleteObserver(this);
        shame.deleteObserver(this);
        condition.deleteObserver(this);
    }

    @Override
    public void killWithoutUpdate() {
        honor.deleteObservers();
        shame.deleteObservers();
        condition.deleteObservers();
        honor.setValue(0);
        shame.setValue(1);
        condition.setValue(DEAD);
    }

    @Override
    public boolean isDead() {
        return condition.is(DEAD) || shameGreaterThanHonor();
    }

    @Override
    public CombatStrategy getCombatStrategy() {
        return null;
    }

    @Override
    public List<Fighter> getFighters() {
        List<Fighter> fighters = new ArrayList<>();
        fighters.add(new CCCharacterFighter());
        return fighters;
    }

    @Override
    public void initCharacterValues() {
        super.initCharacterValues();
        CharacterValues.put(Property.STRENGTH, strength);
        CharacterValues.put(Property.PROTECTION, protection);
        CharacterValues.put(Property.HONOR, honor);
        CharacterValues.put(Property.SHAME, shame);
        CharacterValues.put(Property.ENDURANCE, endurance);
        CharacterValues.put(Property.INTELLIGENCE, intelligence);
        CharacterValues.put(Property.CONDITION, condition);
        CharacterValues.put(Property.CC_EQUIPMENT_LIST, equipment);
        CharacterValues.put(Property.CC_GOD_LIST, gods);
        CharacterValues.put(Property.TUTELARY_GOD, tutelaryGod);
    }

    @Override
    public void startFromPreviousBook() {
        super.startFromPreviousBook();
        zeusInvoked.unset();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean invokeZeus(ZeusInvocation invocation) {
        if (invocation.perform(this)) {
            zeusInvoked.set();
            return true;
        }
        return false;
    }

    @SuppressWarnings("unused")
    public void addEquipment(String name, Integer strength, Integer protection) {
        CCEquipment newEquipment = Property.CC_EQUIPMENT_LIST.get().addNewItem(name);
        newEquipment.setStrength(strength);
        newEquipment.setProtection(protection);
        newEquipment.setEquipped(true);
    }

    public boolean isZeusInvoked() {
        return zeusInvoked.isSet();
    }

    public int getEquipmentBonus(IntValueHolder value) {
        if (value == Property.STRENGTH.get()) {
            int bonus = 0;
            for (CCEquipment item : equipment) {
                if (item.isEquipped()) {
                    bonus += item.getStrength();
                }
            }
            return bonus;
        } else if (value == Property.PROTECTION.get()) {
            int bonus = 0;
            for (CCEquipment item : equipment) {
                if (item.isEquipped()) {
                    bonus += item.getProtection();
                }
            }
            return bonus;
        } else {
            throw new IllegalStateException();
        }
    }

    private boolean shameGreaterThanHonor() {
        return shame.getValue() > honor.getValue();
    }
}
