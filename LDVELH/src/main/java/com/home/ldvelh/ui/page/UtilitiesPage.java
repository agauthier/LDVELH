package com.home.ldvelh.ui.page;

import com.home.ldvelh.R;
import com.home.ldvelh.ui.widget.PageInfo.Tag;
import com.home.ldvelh.ui.widget.utility.UtilityView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UtilitiesPage extends Fragment {

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_page_utilities, container, false);
		initUtilities((ViewGroup) layout.findViewById(R.id.utilities));
		return layout;
	}

	private void initUtilities(ViewGroup parent) {
		removeIfNotInConfig(parent, Tag.UTIL_LAST_PARAGRAPH, R.id.lastParagraphUtility);
		removeIfNotInConfig(parent, Tag.UTIL_DICE, R.id.diceUtility);
		removeIfNotInConfig(parent, Tag.UTIL_LUCK_CHECK, R.id.luckCheckUtility);
		removeIfNotInConfig(parent, Tag.UTIL_ZEUS, R.id.zeusUtility);
	}

	private void removeIfNotInConfig(ViewGroup parent, Tag tag, int utilityResId) {
		View utilityView = parent.findViewById(utilityResId);
		if (PagesAdapter.pageHasTag(R.string.tab_title_utilities, tag)) {
			((UtilityView) utilityView).initLayout();
		} else {
			parent.removeView(utilityView);
		}
	}
}
