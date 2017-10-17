package com.home.ldvelh.ui.widget.list;

import java.lang.reflect.Constructor;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.item.ListItem;
import com.home.ldvelh.model.value.ListValueHolder;
import com.home.ldvelh.ui.dialog.ConsumableEditor;
import com.home.ldvelh.ui.dialog.Store;
import com.home.ldvelh.ui.inflater.RowArrayAdapter;
import com.home.ldvelh.ui.widget.ObservableLinearLayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public abstract class CustomList<T extends ListItem> extends ObservableLinearLayout implements Observer {

	private final String listPropertyName;
	private final String rowLayout;
	private final boolean newItemVisible;
	private final boolean newConsumableItemVisible;
	private final String newItemCaption;
	private final String newConsumableItemCaption;
	private final Class<? extends Store<T>> storeClass;

	private ListValueHolder<T> list;
	private RowArrayAdapter<T> arrayAdapter;

	public CustomList(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomListOptions, 0, 0);
		this.listPropertyName = array.getString(R.styleable.CustomListOptions_listPropertyName);
		this.rowLayout = array.getString(R.styleable.CustomListOptions_rowLayout);
		this.newItemVisible = Boolean.valueOf(array.getString(R.styleable.CustomListOptions_newItemVisible));
		this.newConsumableItemVisible = Boolean.valueOf(array.getString(R.styleable.CustomListOptions_newConsumableItemVisible));
		this.newItemCaption = array.getString(R.styleable.CustomListOptions_newItemCaption);
		this.newConsumableItemCaption = array.getString(R.styleable.CustomListOptions_newConsumableItemCaption);
		this.storeClass = Utils.getClass(array.getString(R.styleable.CustomListOptions_storeClass));
		array.recycle();
		LayoutInflater.from(context).inflate(R.layout.widget_list_custom_list, this, true);
	}

	@Override protected void onFinishInflate() {
		super.onFinishInflate();
		@SuppressWarnings("unchecked") ListValueHolder<T> listValueHolder = (ListValueHolder<T>) Property.getPropertyByName(listPropertyName).get();
		int rowLayoutResId = getResources().getIdentifier(rowLayout, "layout", getContext().getPackageName());
		initListAndAdapter(listValueHolder, rowLayoutResId);
		initStoreButton(getContext());
		initSimpleItemEditText();
		initConsumableItemEditText();
	}

	@Override protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		list.addObserver(this);
	}

	@Override protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		list.deleteObserver(this);
	}

	@Override public void update(Observable observable, Object data) {
		arrayAdapter.clear();
		arrayAdapter.addAll(list.getValue());
		arrayAdapter.notifyDataSetChanged();
		View currentFocus = ((Activity) getContext()).getCurrentFocus();
		if (currentFocus != null) {
			currentFocus.clearFocus();
		}
		notifyObservers(data);
	}

	private void initListAndAdapter(ListValueHolder<T> list, int rowLayoutResId) {
		this.list = list;
		arrayAdapter = new RowArrayAdapter<>(getContext(), rowLayoutResId, list);
		ListView itemList = findViewById(R.id.list);
		itemList.setAdapter(arrayAdapter);
	}

	private void initStoreButton(final Context context) {
		Button storeButton = findViewById(R.id.storeButton);
		if (storeButton != null && storeClass == null) {
			((ViewGroup) storeButton.getParent()).removeView(storeButton);
		} else {
			storeButton.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
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

	private void initSimpleItemEditText() {
		final EditText editText = findViewById(R.id.newItem);
		if (editText != null && !newItemVisible) {
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
				@Override public boolean onKey(View view, int keyCode, KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:
							String itemName = editText.getText().toString();
							if (!itemName.isEmpty()) {
								editText.setText("");
								list.add(new ItemAndQuantity(itemName));
							}
							hideKeyboard(editText);
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

	private void initConsumableItemEditText() {
		final EditText editText = findViewById(R.id.newConsumableItem);
		if (editText != null && !newConsumableItemVisible) {
			((ViewGroup) editText.getParent()).removeView(editText);
		} else {
			if (StringUtils.isNotEmpty(newConsumableItemCaption)) {
				editText.setHint(newConsumableItemCaption);
			}
			editText.setOnKeyListener(new OnKeyListener() {
				@Override public boolean onKey(View view, int keyCode, KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:
							String itemName = editText.getText().toString();
							if (!itemName.isEmpty()) {
								editText.setText("");
								promptForEffects(itemName);
							}
							hideKeyboard(editText);
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

	private void hideKeyboard(final EditText editText) {
		InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		editText.clearFocus();
	}

	private void promptForEffects(String itemName) {
		ConsumableEditor consumableEditor = new ConsumableEditor(getContext(), itemName);
		consumableEditor.show();
	}
}
