package com.home.ldvelh.model.item;

import com.home.ldvelh.model.Namable;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Item implements Namable, Serializable {
    private static final long serialVersionUID = -2053819554209745912L;

    private String name;
    private final List<Effect> effects = new ArrayList<>();
    private Object data;

    protected Item() {}

    protected Item(String name, List<Effect> effects, Object data) {
        this.name = name;
        this.effects.addAll(effects);
        this.data = data;
    }

    public static <T extends Item> T create(Class<T> itemClass) {
        return create(itemClass, StringUtils.EMPTY, Collections.<Effect>emptyList(), null);
    }

    public static <T extends Item> T create(Class<T> itemClass, String name, List<Effect> effects, Object data) {
        try {
            Constructor<T> constructor = itemClass.getDeclaredConstructor(String.class, List.class, Object.class);
            constructor.setAccessible(true);
            return constructor.newInstance(name, effects, data);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    public abstract <T extends Item> T copy();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean hasName(String name) {
        return this.name.equals(name);
    }

    public boolean hasEffects() {
        return effects.size() > 0;
    }

    public void applyEffects() {
        for (Effect effect : effects) {
            effect.apply();
        }
    }

    public void applyEffectsTwice() {
        for (Effect effect : effects) {
            effect.applyTwice();
        }
    }

    public Object getData() {
        return data;
    }

    protected void populate(Item item) {
        item.name = name;
        item.effects.addAll(effects);
        item.data = data;
    }
}
