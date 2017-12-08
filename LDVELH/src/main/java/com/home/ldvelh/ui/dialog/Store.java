package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.Item;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class Store<T extends Item> extends AdventureDialog implements Observer {

    private enum DisplayMode {
        SHOW, HIDE
    }

    private final int layoutResId;
    private final IntValueHolder srcCurrency;
    private final IntValueHolder currency;
    private final List<ShopPickerItem> shopItems = new ArrayList<>();
    private final boolean initCountsWithList;
    final ListValueHolder<T> list;

    Store(Context context, Object data, int layoutResId, IntValueHolder srcCurrency, ListValueHolder<T> list) {
        this(context, data, layoutResId, srcCurrency, list, false);
    }

    Store(Context context, Object data, int layoutResId, IntValueHolder srcCurrency, ListValueHolder<T> list, boolean initCountsWithList) {
        super(context, data);
        this.layoutResId = layoutResId;
        this.srcCurrency = srcCurrency;
        this.currency = new IntValueHolder(srcCurrency);
        this.initCountsWithList = initCountsWithList;
        this.list = list;
    }

    protected abstract void addToList(ShopPickerItem shopItem, ListValueHolder<T> list);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResId);
        setText(R.id.remainingCurrency, String.valueOf(currency.getValue()));
        setText(R.id.maxCurrency, String.valueOf(currency.getValue()));
        initPickers(currency.getValue());
        configurePickerObservers(DisplayMode.SHOW);
        Button okButton = findViewById(R.id.okButton);
        okButton.setEnabled(okButtonEnabled(currency));
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeAcquisition();
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        IntValueHolder shopItemValue = (IntValueHolder) observable;
        int pickerDelta = shopItemValue.getValueDiff();
        ShopPickerItem shopItem = findPickerByValue(shopItemValue);
        currency.add(-pickerDelta * shopItem.getPrice());
        setText(R.id.remainingCurrency, String.valueOf(currency.getValue()));
        recalculateMaxValues();
        findViewById(R.id.okButton).setEnabled(okButtonEnabled(currency));
    }

    void acquireItems() {
        for (ShopPickerItem shopItem : getAllShopItems()) {
            if (initCountsWithList) {
                shopItem.setQuantity(shopItem.getQuantity() - list.countByName(shopItem.getName()));
            }
            addToList(shopItem, list);
        }
    }

    boolean okButtonEnabled(IntValueHolder currency) {
        return true;
    }

    void completeAcquisition() {
        srcCurrency.addWithFeedback(currency.getValue() - srcCurrency.getValue());
        acquireItems();
        configurePickerObservers(DisplayMode.HIDE);
        fulfill();
        dismiss();
    }

    private void initPickers(int remainingCurrency) {
        for (ShopPickerItem shopItem : getAllShopItems()) {
            int initValue = initCountsWithList ? list.countByName(shopItem.getName()) : 0;
            shopItem.initialize(initValue, initValue + (remainingCurrency / shopItem.getPrice()));
        }
    }

    private void configurePickerObservers(DisplayMode displayMode) {
        for (ShopPickerItem shopItem : getAllShopItems()) {
            switch (displayMode) {
                case SHOW:
                    shopItem.addObserver(this);
                    break;
                case HIDE:
                    shopItem.deleteObserver(this);
                    break;
            }
        }
    }

    private void recalculateMaxValues() {
        for (ShopPickerItem shopItem : getAllShopItems()) {
            shopItem.setMax(shopItem.getQuantity() + (currency.getValue() / shopItem.getPrice()));
        }
    }

    private List<ShopPickerItem> getAllShopItems() {
        if (shopItems.isEmpty()) {
            //noinspection ConstantConditions
            shopItems.addAll(getShopItemsInView((ViewGroup) getWindow().getDecorView()));
        }
        return shopItems;
    }

    private List<ShopPickerItem> getShopItemsInView(ViewGroup viewGroup) {
        List<ShopPickerItem> shopItems = new ArrayList<>();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);
            try {
                ShopPickerItem shopItem = (ShopPickerItem) childView;
                shopItems.add(shopItem);
            } catch (Exception notShopPickerItem) {
                try {
                    ViewGroup childViewGroup = (ViewGroup) childView;
                    shopItems.addAll(getShopItemsInView(childViewGroup));
                } catch (Exception notViewGroup) { /* Swallow */ }
            }
        }
        return shopItems;
    }

    private ShopPickerItem findPickerByValue(IntValueHolder value) {
        for (ShopPickerItem shopItem : getAllShopItems()) {
            if (shopItem.hasValue(value)) {
                return shopItem;
            }
        }
        throw new IllegalStateException();
    }
}
