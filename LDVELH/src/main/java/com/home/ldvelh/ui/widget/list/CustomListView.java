package com.home.ldvelh.ui.widget.list;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.Item;
import com.home.ldvelh.model.value.ListValueHolder;
import com.home.ldvelh.ui.dialog.ConsumableEditor;
import com.home.ldvelh.ui.dialog.Store;
import com.home.ldvelh.ui.inflater.RowArrayAdapter;
import com.home.ldvelh.ui.widget.ObservableLinearLayout;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public abstract class CustomListView<T extends Item> extends ObservableLinearLayout implements Observer {

    private final String listPropertyName;
    private final String rowLayout;
    private final boolean newItemVisible;
    private final boolean newConsumableItemVisible;
    private final String newItemCaption;
    private final String newConsumableItemCaption;
    private final Class<? extends Store<T>> storeClass;

    private ListValueHolder<T> list;
    private RowArrayAdapter<CustomListViewItem<T>> arrayAdapter;

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomListView, 0, 0);
        this.listPropertyName = array.getString(R.styleable.CustomListView_listPropertyName);
        this.rowLayout = array.getString(R.styleable.CustomListView_rowLayout);
        this.newItemVisible = Boolean.valueOf(array.getString(R.styleable.CustomListView_newItemVisible));
        this.newConsumableItemVisible = Boolean.valueOf(array.getString(R.styleable.CustomListView_newConsumableItemVisible));
        this.newItemCaption = array.getString(R.styleable.CustomListView_newItemCaption);
        this.newConsumableItemCaption = array.getString(R.styleable.CustomListView_newConsumableItemCaption);
        this.storeClass = Utils.getClass(array.getString(R.styleable.CustomListView_storeClass));
        array.recycle();
        LayoutInflater.from(context).inflate(R.layout.widget_list_custom_list, this, true);
    }

    public abstract void initRowView(View row, CustomListViewItem<T> customListViewItem);
    protected abstract T createItem(String name);
    protected abstract CustomListViewItem<T> createCustomListViewItem(T item);

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int rowLayoutResId = getResources().getIdentifier(rowLayout, "layout", getContext().getPackageName());
        initListAndAdapter(rowLayoutResId);
        initStoreButton(getContext());
        initSimpleItemEditText();
        initConsumableItemEditText();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        list.addObserver(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        list.deleteObserver(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void update(Observable observable, Object data) {
        refreshListView();
        View currentFocus = ((Activity) getContext()).getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        notifyObservers(data);
    }

    void increment(T item) {
        list.increment(item);
    }

    void decrement(T item) {
        list.decrement(item);
    }

    void touch() {
        list.touch();
    }

    void setItemName(View view, String text) {
        TextView textView = view.findViewById(R.id.itemName);
        textView.setText(text);
    }

    private void initListAndAdapter(int rowLayoutResId) {
        //noinspection unchecked,ConstantConditions
        list = (ListValueHolder<T>) Property.getPropertyByName(listPropertyName).get();
        arrayAdapter = new RowArrayAdapter<>(getContext(), rowLayoutResId, new ArrayList<CustomListViewItem<T>>());
        refreshListView();
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(arrayAdapter);
    }

    private void initStoreButton(final Context context) {
        Button storeButton = findViewById(R.id.storeButton);
        if (storeButton != null) {
            if (storeClass == null) {
                ((ViewGroup) storeButton.getParent()).removeView(storeButton);
            } else {
                storeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Constructor<? extends Store<T>> storeConstructor = storeClass.getConstructor(Context.class);
                            storeConstructor.newInstance(context).show();
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    }
                });
            }
        }
    }

    private void initSimpleItemEditText() {
        final EditText editText = findViewById(R.id.newItem);
        if (editText != null) {
            if (!newItemVisible) {
                ((ViewGroup) editText.getParent()).removeView(editText);
            } else {
                if (StringUtils.isNotEmpty(newItemCaption)) {
                    editText.setHint(newItemCaption);
                }
                if (!newConsumableItemVisible) {
                    android.view.ViewGroup.LayoutParams layoutParams = editText.getLayoutParams();
                    layoutParams.width = LayoutParams.MATCH_PARENT;
                    editText.setLayoutParams(layoutParams);
                }
                editText.setOnKeyListener(new OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_ENTER:
                                    String itemName = editText.getText().toString();
                                    if (!itemName.isEmpty()) {
                                        editText.setText("");
                                        list.add(createItem(itemName));
                                    }
                                    Utils.hideKeyboard(editText);
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    private void initConsumableItemEditText() {
        final EditText editText = findViewById(R.id.newConsumableItem);
        if (editText != null) {
            if (!newConsumableItemVisible) {
                ((ViewGroup) editText.getParent()).removeView(editText);
            } else {
                if (StringUtils.isNotEmpty(newConsumableItemCaption)) {
                    editText.setHint(newConsumableItemCaption);
                }
                editText.setOnKeyListener(new OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_ENTER:
                                    String itemName = editText.getText().toString();
                                    if (!itemName.isEmpty()) {
                                        editText.setText("");
                                        promptForEffects(itemName);
                                    }
                                    Utils.hideKeyboard(editText);
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    private void promptForEffects(String itemName) {
        ConsumableEditor consumableEditor = new ConsumableEditor(getContext(), itemName);
        consumableEditor.show();
    }

    private void refreshListView() {
        arrayAdapter.clear();
        for (T item : list) {
            arrayAdapter.add(createCustomListViewItem(item));
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
