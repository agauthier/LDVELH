package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;

public class NameEditor<T extends Namable> extends AdventureDialog {

    private final T namable;
    private final int layoutResId;
    private final int hintResId;

    public NameEditor(Context context, T namable) {
        this(context, namable, R.layout.dialog_name_editor, R.string.name);
    }

    NameEditor(Context context, T namable, int layoutResId, int hintResId) {
        super(context, null);
        this.namable = namable;
        this.layoutResId = layoutResId;
        this.hintResId = hintResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResId);
        initView();
    }

    @Override
    public void dismiss() {
        Utils.hideKeyboard(findViewById(R.id.name));
        super.dismiss();
    }

    @Override
    void initView() {
        super.initView();
        final EditText nameEditText = findViewById(R.id.name);
        nameEditText.setText(namable.getName());
        nameEditText.selectAll();
        nameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            finishEditing(nameEditText);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable editable) {
                findViewById(R.id.okButton).setEnabled(isOkButtonEnabled(editable.toString()));
            }
        });
        ((TextView) findViewById(R.id.name)).setHint(hintResId);
        Button okButton = findViewById(R.id.okButton);
        okButton.setEnabled(false);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditing(nameEditText);
            }
        });
    }

    T getNamable() {
        return namable;
    }

    boolean isOkButtonEnabled(String name) {
        return !name.isEmpty();
    }

    private void finishEditing(EditText nameEditText) {
        namable.setName(nameEditText.getText().toString());
        dismiss();
    }
}
