package com.home.ldvelh.model.value;

import android.support.annotation.NonNull;

import com.home.ldvelh.model.item.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.home.ldvelh.model.value.ListValueHolder.ItemMergeType.MERGE;

public class ListValueHolder<T extends Item> extends ValueHolder<List<T>> implements Iterable<T> {
    private static final long serialVersionUID = -7685844299342180845L;

    public enum ItemMergeType {MERGE, DONT_MERGE}

    private final ItemMergeType itemMergeType;

    public ListValueHolder(ItemMergeType itemMergeType) {
        super(new ArrayList<T>());
        this.itemMergeType = itemMergeType;
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

    public int getQuantityForName(String name) {
        T item = findByName(name);
        return (item == null) ? 0 : item.getQuantity();
    }

    public T findByName(String name) {
        for (T item : super.getValue()) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public T findByData(Object data) {
        for (T item : super.getValue()) {
            if (item.getData() == data) {
                return item;
            }
        }
        return null;
    }

    public void add(T item) {
        T identicalItem = findIdenticalItem(item);
        if (itemMergeType == MERGE && identicalItem != null) {
            identicalItem.addQuantity(item.getQuantity());
        } else {
            super.getValue().add(item);
        }
        notifyObservers();
    }

    public void remove(T item) {
        if (super.getValue().remove(item)) {
            notifyObservers();
        }
    }

    public void increment(T item) {
        item.addQuantity(1);
        notifyObservers();
    }

    public void decrement(T item) {
        item.addQuantity(-1);
        if (item.getQuantity() <= 0) {
            super.getValue().remove(item);
        }
        notifyObservers();
    }

    public T get(int location) {
        return super.getValue().get(location);
    }

    public void touch() {
        notifyObservers();
    }

    private T findIdenticalItem(T item) {
        for (T existingItem : super.getValue()) {
            if (existingItem.isIdentical(item)) {
                return existingItem;
            }
        }
        return null;
    }
}
