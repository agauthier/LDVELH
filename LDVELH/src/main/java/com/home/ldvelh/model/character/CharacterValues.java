package com.home.ldvelh.model.character;

import java.util.HashMap;
import java.util.Map;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.value.ValueHolder;

public class CharacterValues {

    private static final Map<Property<ValueHolder<?>>, ValueHolder<?>> propertyValues = new HashMap<>();

    @SuppressWarnings("unchecked") public static <T extends ValueHolder<?>> T get(Property<T> prop) {
        return (T) propertyValues.get(prop);
    }

    @SuppressWarnings("unchecked") public static <T extends ValueHolder<?>> void put(Property<T> prop, T obj) {
        propertyValues.put((Property<ValueHolder<?>>) prop, obj);
    }

    public static void clear() {
        for (ValueHolder<?> valueHolder : propertyValues.values()) {
            valueHolder.deleteObservers();
        }
        propertyValues.clear();
    }
}
