package com.home.ldvelh.model.character;

import java.util.Observable;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;
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

public class CCCharacter extends Character {
    private static final long serialVersionUID = -7699595433223555416L;

    public enum Condition {
        UNSCATHED {
            @Override public Condition worsen() {
                return WOUNDED;
            }
        }, WOUNDED {
            @Override public Condition worsen() {
                return BADLY_WOUNDED;
            }
        }, BADLY_WOUNDED {
            @Override public Condition worsen() {
                return DEAD;
            }
        }, DEAD {
            @Override public Condition worsen() {
                return DEAD;
            }
        };

        public abstract Condition worsen();
    }

    public enum ZeusInvocation {
        RESUSCITATE {
            @Override public boolean canPerform(CCCharacter character) {
                return !character.shameGreaterThanHonor();
            }

            @Override public void perform(CCCharacter character) {
                if (character.honor.getValue() == 0) {
                    recoverFromZeroHonor(character);
                } else {
                    character.shame.addWithFeedback(-character.shame.getValue());
                    character.honor.addWithFeedback(-character.honor.getValue() + 1);
                }
            }
        }, GAIN_RANDOM_HONOR {
            @Override public boolean canPerform(CCCharacter character) {
                return character.honor.getValue() > 0;
            }

            @Override public void perform(CCCharacter character) {
                character.honor.addWithFeedback(Die.SIX_FACES.roll());
            }
        }, REGAIN_HONOR_POINTS {
            @Override public boolean canPerform(CCCharacter character) {
                return character.honor.getValue() == 0;
            }

            @Override public void perform(CCCharacter character) {
                recoverFromZeroHonor(character);
            }
        }, ALL_GODS_NEUTRAL {
            @Override public boolean canPerform(CCCharacter character) {
                return true;
            }

            @Override public void perform(CCCharacter character) {
                ListValueHolder<CCGod> godsList = Property.CC_GOD_LIST.get();
                for (CCGod god : godsList) {
                    god.setAttitude(Attitude.NEUTRAL);
                }
                godsList.touch();
            }
        };

        public abstract boolean canPerform(CCCharacter character);

        public abstract void perform(CCCharacter character);

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
    private final EnumValueHolder<Condition> condition;
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
        condition = new EnumValueHolder<>(Condition.UNSCATHED);
        equipment = new ListValueHolder<>(CCEquipment.class);
        gods = new ListValueHolder<>(CCGod.class);
        tutelaryGod = new EnumValueHolder<>(God.NO_GOD);
        God.populateList(gods);
    }

    @Override public void update(Observable observable, Object data) {
        if (observable == honor && honor.getValue() == 0 && honor.getMax() != 0) {
            honor.addToMax(-honor.getMax());
        }
        super.update(observable, data);
    }

    @Override public void addLifeObserver(AdventureActivity activity) {
        honor.addObserver(this);
        shame.addObserver(this);
        condition.addObserver(this);
        addObserver(activity);
    }

    @Override public void deleteLifeObserver(AdventureActivity activity) {
        deleteObserver(activity);
        honor.deleteObserver(this);
        shame.deleteObserver(this);
        condition.deleteObserver(this);
    }

    @Override public void killWithoutUpdate() {
        honor.deleteObservers();
        shame.deleteObservers();
        condition.deleteObservers();
        honor.setValue(0);
        shame.setValue(1);
        condition.setValue(Condition.DEAD);
    }

    @Override public boolean isDead() {
        return condition.is(Condition.DEAD) || shameGreaterThanHonor();
    }

    @Override public CombatStrategy getCombatStrategy() {
        return null;
    }

    @Override public void initCharacterValues() {
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

    @Override public void startFromPreviousBook() {
        super.startFromPreviousBook();
        zeusInvoked.unset();
    }

    public void invokeZeus(ZeusInvocation invocation) {
        invocation.perform(this);
        zeusInvoked.set();
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
