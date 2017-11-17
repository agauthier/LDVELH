package com.home.ldvelh.model.value;

import android.support.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.item.ListItem;

public class ListValueHolder<T extends ListItem> extends ValueHolder<List<T>> implements Iterable<T> {
    private static final long serialVersionUID = -1378211383389336094L;

    private final T dummyItem;

    public ListValueHolder(Class<T> itemClass) {
        super(new ArrayList<T>());
        T dummyItem;
        try {
            @SuppressWarnings("unchecked") Constructor<T> constructor = (Constructor<T>) itemClass.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            dummyItem = constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        this.dummyItem = dummyItem;
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return super.getValue().iterator();
    }

    @Override
    public List<T> getValue() {
        return new ArrayList<>(super.getValue());
    }

    public int size() {
        return super.getValue().size();
    }

    public void clear() {
        super.getValue().clear();
    }

    public T find(String name) {
        for (T item : super.getValue()) {
            if (item.hasName(name)) {
                return item;
            }
        }
        return null;
    }

    public T find(Object data) {
        for (T item : super.getValue()) {
            if (item.getData() == data) {
                return item;
            }
        }
        return null;
    }

    public T add(String itemName) {
        return add(new ItemAndQuantity(itemName));
    }

    @SuppressWarnings("UnusedReturnValue")
    public T add(String itemName, Object data) {
        return add(new ItemAndQuantity(itemName), data);
    }

    public T add(ItemAndQuantity itemAndQty) {
        return add(itemAndQty, Collections.<Effect>emptyList(), null);
    }

    @SuppressWarnings("WeakerAccess")
    public T add(ItemAndQuantity itemAndQty, Object data) {
        return add(itemAndQty, Collections.<Effect>emptyList(), data);
    }

    public T add(ItemAndQuantity itemAndQty, List<Effect> effects) {
        return add(itemAndQty, effects, null);
    }

    @SuppressWarnings("WeakerAccess")
    public T add(ItemAndQuantity itemAndQty, List<Effect> effects, Object data) {
        T newItem = dummyItem.create(itemAndQty, effects, data, this);
        return add(newItem);
    }

    private T add(T item) {
        T addedItem = null;
        if (item.getQuantity() != 0) {
            T existingItem = find(item.getName());
            if (existingItem != null) {
                existingItem.add(item.getQuantity());
                if (existingItem.getQuantity() > 0) {
                    addedItem = existingItem;
                } else if (existingItem.getQuantity() == 0) {
                    super.getValue().remove(existingItem);
                } else if (existingItem.getQuantity() < 0) {
                    throw new IllegalArgumentException();
                }
            } else if (item.getQuantity() > 0) {
                super.getValue().add(item);
                addedItem = item;
            } else {
                throw new IllegalArgumentException();
            }
            notifyObservers();
        }
        return addedItem;
    }

    public void remove(T item) {
        if (super.getValue().remove(item)) {
            notifyObservers();
        }
    }

    public T get(int location) {
        return super.getValue().get(location);
    }

    public void touch() {
        notifyObservers();
    }
}
