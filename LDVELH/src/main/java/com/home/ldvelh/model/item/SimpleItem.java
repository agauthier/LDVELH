package com.home.ldvelh.model.item;

public class SimpleItem extends Item {
    private static final long serialVersionUID = -5247844386266471568L;

    public SimpleItem(String name) {
        super(name, null);
    }

    public SimpleItem(String name, int quantity) {
        super(name, quantity, null);
    }
}
