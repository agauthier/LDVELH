package com.home.ldvelh.model.combat;

import com.home.ldvelh.model.value.IntValueHolder;

public interface EditableFighter {

    void setEditableName(String name);

    int getEditableValue1NameResId();

    IntValueHolder getEditableValue1();

    int getEditableValue2NameResId();

    IntValueHolder getEditableValue2();
}
