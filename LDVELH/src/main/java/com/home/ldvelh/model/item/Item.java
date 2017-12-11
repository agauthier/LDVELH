package com.home.ldvelh.model.item;

import com.home.ldvelh.model.Namable;

import java.io.Serializable;

public abstract class Item implements Namable, Serializable {
    private static final long serialVersionUID = -1099569270000752599L;

    private String name;
    private int quantity;
    private Object data;

    protected Item(String name, Object data) {
        this(name, 1, data);
    }

    protected Item(String name, int quantity, Object data) {
        this.name = name;
        this.quantity = quantity;
        this.data = data;
    }

    public boolean isIdentical(Item item) {
        return name.equals(item.name) && (data == null || (data != null && data.equals(item.data)));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public Object getData() {
        return data;
    }
}
