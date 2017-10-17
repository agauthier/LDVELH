package com.home.ldvelh.model.value;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import com.home.ldvelh.commons.GameObservable;

public abstract class ValueHolder<T> extends GameObservable implements Serializable, Observer {
    private static final long serialVersionUID = -8389775948894872273L;

    private T value;

    protected ValueHolder(T value) {
        this.value = value;
    }

    @Override public void update(Observable observable, Object data) {
        notifyObservers(this);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        setValue(value, null);
    }

    void setValue(T value, Object data) {
        if (this.value != value) {
            this.value = value;
            notifyObservers(data);
        }
    }
}
