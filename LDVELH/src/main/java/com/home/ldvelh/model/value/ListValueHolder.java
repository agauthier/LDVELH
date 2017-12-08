package com.home.ldvelh.model.value;

import android.support.annotation.NonNull;

import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListValueHolder<T extends Item> extends ValueHolder<List<T>> implements Iterable<T> {
    private static final long serialVersionUID = -2086105548382419243L;

    private final Class<T> itemClass;

    public ListValueHolder(Class<T> itemClass) {
        super(new ArrayList<T>());
        this.itemClass = itemClass;
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

    public int countByName(String name) {
        int count = 0;
        for (T item : super.getValue()) {
            count += item.hasName(name) ? 1 : 0;
        }
        return count;
    }

    public T findByData(Object data) {
        for (T item : super.getValue()) {
            if (item.getData() == data) {
                return item;
            }
        }
        return null;
    }

    public T addNewItem(T sampleItem) {
        return add(sampleItem.<T>copy());
    }

    public T addNewItem(String name) {
        return add(Item.create(itemClass, name, Collections.<Effect>emptyList(), null));
    }

    public T addNewItem(String name, List<Effect> effects) {
        return add(Item.create(itemClass, name, effects, null));
    }

    public T addNewItem(String name, Object data) {
        return add(Item.create(itemClass, name, Collections.<Effect>emptyList(), data));
    }

    public T add(T item) {
        super.getValue().add(item);
        notifyObservers();
        return item;
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
