package com.home.ldvelh.ui.page;

import com.home.ldvelh.R;
import com.home.ldvelh.ui.dialog.SOIngredientPicker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SOSpellsPage extends Fragment {

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_page_so_spells, container, false);
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button button = getView().findViewById(R.id.ingredientsButton);
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				new SOIngredientPicker(getContext()).show();
			}
		});
	}
}
