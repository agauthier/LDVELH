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
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class CustomList<T extends Item> extends ObservableLinearLayout implements Observer {

    private final String listPropertyName;
    private final String rowLayout;
    private final boolean newItemVisible;
    private final boolean newConsumableItemVisible;
    private final String newItemCaption;
    private final String newConsumableItemCaption;
    private final Class<? extends Store<T>> storeClass;
    private final List<CustomListItem<T>> customList = new ArrayList<>();

    private ListValueHolder<T> modelList;
    private RowArrayAdapter<CustomListItem<T>> arrayAdapter;

    public CustomList(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomList, 0, 0);
        this.listPropertyName = array.getString(R.styleable.CustomList_listPropertyName);
        this.rowLayout = array.getString(R.styleable.CustomList_rowLayout);
        this.newItemVisible = Boolean.valueOf(array.getString(R.styleable.CustomList_newItemVisible));
        this.newConsumableItemVisible = Boolean.valueOf(array.getString(R.styleable.CustomList_newConsumableItemVisible));
        this.newItemCaption = array.getString(R.styleable.CustomList_newItemCaption);
        this.newConsumableItemCaption = array.getString(R.styleable.CustomList_newConsumableItemCaption);
        this.storeClass = Utils.getClass(array.getString(R.styleable.CustomList_storeClass));
        array.recycle();
        LayoutInflater.from(context).inflate(R.layout.widget_list_custom_list, this, true);
    }

    public abstract void initRowView(View row, CustomListItem<T> customListItem);
    public abstract CustomListItem<T> createListItem(T item);

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ListValueHolder<T> listValueHolder = (ListValueHolder<T>) Property.getPropertyByName(listPropertyName).get();
        int rowLayoutResId = getResources().getIdentifier(rowLayout, "layout", getContext().getPackageName());
        initListAndAdapter(listValueHolder, rowLayoutResId);
        initStoreButton(getContext());
        initSimpleItemEditText();
        initConsumableItemEditText();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        modelList.addObserver(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        modelList.deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        arrayAdapter.clear();
        populateCustomList();
        arrayAdapter.addAll(customList);
        arrayAdapter.notifyDataSetChanged();
        View currentFocus = ((Activity) getContext()).getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        notifyObservers(data);
    }

    public void add(T sampleItem) {
        modelList.addNewItem(sampleItem);
    }

    public void remove(T item) {
        modelList.remove(item);
    }

    protected void touch() {
        modelList.touch();
    }

    private void initListAndAdapter(ListValueHolder<T> list, int rowLayoutResId) {
        modelList = list;
        populateCustomList();
        arrayAdapter = new RowArrayAdapter<>(getContext(), rowLayoutResId, customList);
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
                                        modelList.addNewItem(itemName);
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

    private void populateCustomList() {
        customList.clear();
        for (T item : modelList) {
            addToCustomList(item);
        }
    }

    private void addToCustomList(T item) {
        boolean itemAlreadyExists = false;
        for (CustomListItem<T> customListItem : customList) {
            if (customListItem.add(item)) {
                itemAlreadyExists = true;
                break;
            }
        }
        if (!itemAlreadyExists) {
            customList.add(createListItem(item));
        }
    }
}
