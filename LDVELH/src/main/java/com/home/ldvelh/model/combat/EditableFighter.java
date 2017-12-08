package com.home.ldvelh.model.combat;

import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.Namable;

public interface EditableFighter extends Namable {

    int getEditableValue1NameResId();

    IntValueHolder getEditableValue1();

    int getEditableValue2NameResId();

    IntValueHolder getEditableValue2();
}
