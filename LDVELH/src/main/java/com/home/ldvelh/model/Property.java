package com.home.ldvelh.model;

import java.lang.reflect.Field;

import com.home.ldvelh.model.character.CCCharacter.Condition;
import com.home.ldvelh.model.character.Character;
import com.home.ldvelh.model.character.CharacterValues;
import com.home.ldvelh.model.combat.CombatRow;
import com.home.ldvelh.model.item.CCEquipment;
import com.home.ldvelh.model.item.CCGod;
import com.home.ldvelh.model.item.CCGod.God;
import com.home.ldvelh.model.item.Note;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.item.Spell;
import com.home.ldvelh.model.value.DF04AssetValueHolder;
import com.home.ldvelh.model.value.EnumValueHolder;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;
import com.home.ldvelh.model.value.MapValueHolder;
import com.home.ldvelh.model.value.StringValueHolder;
import com.home.ldvelh.model.value.ValueHolder;

public abstract class Property<T extends ValueHolder<?>> {

    // General properties
    public static final Property<Character> CHARACTER = new Property<Character>("CHARACTER") {};
    public static final Property<StringValueHolder> LAST_PARAGRAPH = new Property<StringValueHolder>("LAST_PARAGRAPH") {};
    public static final Property<MapValueHolder> MAP = new Property<MapValueHolder>("MAP") {};

    // Lists
    public static final Property<ListValueHolder<SimpleItem>> ITEM_LIST = new Property<ListValueHolder<SimpleItem>>("ITEM_LIST") {};
    public static final Property<ListValueHolder<SimpleItem>> MAGIC_ITEM_LIST = new Property<ListValueHolder<SimpleItem>>("MAGIC_ITEM_LIST") {};
    public static final Property<ListValueHolder<SimpleItem>> STONE_LIST = new Property<ListValueHolder<SimpleItem>>("STONE_LIST") {};
    public static final Property<ListValueHolder<SimpleItem>> INGREDIENT_LIST = new Property<ListValueHolder<SimpleItem>>("INGREDIENT_LIST") {};
    public static final Property<ListValueHolder<Note>> NOTE_LIST = new Property<ListValueHolder<Note>>("NOTE_LIST") {};
    public static final Property<ListValueHolder<Spell>> SPELL_LIST = new Property<ListValueHolder<Spell>>("SPELL_LIST") {};
    public static final Property<ListValueHolder<CombatRow>> FIGHTER_GRID = new Property<ListValueHolder<CombatRow>>("FIGHTER_GRID") {};
    public static final Property<ListValueHolder<CCEquipment>> CC_EQUIPMENT_LIST = new Property<ListValueHolder<CCEquipment>>("CC_EQUIPMENT_LIST") {};
    public static final Property<ListValueHolder<CCGod>> CC_GOD_LIST = new Property<ListValueHolder<CCGod>>("CC_GOD_LIST") {};

    // CC
    public static final Property<IntValueHolder> STRENGTH = new Property<IntValueHolder>("STRENGTH") {};
    public static final Property<IntValueHolder> PROTECTION = new Property<IntValueHolder>("PROTECTION") {};
    public static final Property<IntValueHolder> HONOR = new Property<IntValueHolder>("HONOR") {};
    public static final Property<IntValueHolder> SHAME = new Property<IntValueHolder>("SHAME") {};
    public static final Property<IntValueHolder> ENDURANCE = new Property<IntValueHolder>("ENDURANCE") {};
    public static final Property<IntValueHolder> INTELLIGENCE = new Property<IntValueHolder>("INTELLIGENCE") {};
    public static final Property<EnumValueHolder<Condition>> CONDITION = new Property<EnumValueHolder<Condition>>("CONDITION") {};
    public static final Property<EnumValueHolder<God>> TUTELARY_GOD = new Property<EnumValueHolder<God>>("TUTELARY_GOD") {};

    // DF
    public static final Property<IntValueHolder> SKILL = new Property<IntValueHolder>("SKILL") {};
    public static final Property<IntValueHolder> STAMINA = new Property<IntValueHolder>("STAMINA") {};
    public static final Property<IntValueHolder> LUCK = new Property<IntValueHolder>("LUCK") {};
    public static final Property<IntValueHolder> GOLD = new Property<IntValueHolder>("GOLD") {};
    public static final Property<IntValueHolder> ATTACK_BONUS = new Property<IntValueHolder>("ATTACK_BONUS") {};
    public static final Property<IntValueHolder> LUCK_PENALTY = new Property<IntValueHolder>("LUCK_PENALTY") {};

    // DF02
    public static final Property<IntValueHolder> MAGIC = new Property<IntValueHolder>("MAGIC") {};

    // DF04
    public static final Property<DF04AssetValueHolder> COMMANDER = new Property<DF04AssetValueHolder>("COMMANDER") {};
    public static final Property<DF04AssetValueHolder> SCIENCE_OFFICER = new Property<DF04AssetValueHolder>("SCIENCE_OFFICER") {};
    public static final Property<DF04AssetValueHolder> MEDICAL_OFFICER = new Property<DF04AssetValueHolder>("MEDICAL_OFFICER") {};
    public static final Property<DF04AssetValueHolder> ENGINEERING_OFFICER = new Property<DF04AssetValueHolder>("ENGINEERING_OFFICER") {};
    public static final Property<DF04AssetValueHolder> SHIP = new Property<DF04AssetValueHolder>("SHIP") {};
    public static final Property<DF04AssetValueHolder> SECURITY_OFFICER = new Property<DF04AssetValueHolder>("SECURITY_OFFICER") {};
    public static final Property<DF04AssetValueHolder> SECURITY_AGENT_1 = new Property<DF04AssetValueHolder>("SECURITY_AGENT_1") {};
    public static final Property<DF04AssetValueHolder> SECURITY_AGENT_2 = new Property<DF04AssetValueHolder>("SECURITY_AGENT_2") {};

    // DF10
    public static final Property<IntValueHolder> FEAR = new Property<IntValueHolder>("FEAR") {};

    // SO
    public static final Property<IntValueHolder> MEALS = new Property<IntValueHolder>("MEALS") {};

    private final String name;

    private Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public T get() {
        return CharacterValues.get(this);
    }

    public static <U extends ValueHolder<?>> Property<U> getPropertyByName(String name) {
        Field[] list = Property.class.getDeclaredFields();
        for (Field item : list) {
            if (item.getName().equals(name)) {
                try {
                    @SuppressWarnings("unchecked") Property<U> prop = (Property<U>) item.get(SKILL);
                    return prop;
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        return null;
    }
}
