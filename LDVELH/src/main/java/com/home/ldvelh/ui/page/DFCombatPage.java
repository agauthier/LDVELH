package com.home.ldvelh.ui.page;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.combat.CombatCore;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.combat.DFCombat;
import com.home.ldvelh.model.combat.Fighter;
import com.home.ldvelh.ui.dialog.DFFighterEditor;
import com.home.ldvelh.ui.widget.PageTag;
import com.home.ldvelh.ui.widget.list.CombatRowList;

import java.util.Observable;
import java.util.Observer;

import static com.home.ldvelh.commons.Constants.METHOD_ASSAULT;
import static com.home.ldvelh.commons.Constants.METHOD_ASSAULT_ENABLED;
import static com.home.ldvelh.commons.Constants.METHOD_ESCAPE;
import static com.home.ldvelh.commons.Constants.METHOD_ESCAPE_ENABLED;
import static com.home.ldvelh.ui.widget.PageTag.TagType.COMBAT_BUTTON;

public class DFCombatPage extends Fragment implements Observer {

    static class CombatButtonData {

        private final int imageResId;
        private final String checkEnabledMethod;
        private final String onClickMethod;

        CombatButtonData(int imageResId, String checkEnabledMethod, String onClickMethod) {
            this.imageResId = imageResId;
            this.checkEnabledMethod = checkEnabledMethod;
            this.onClickMethod = onClickMethod;
        }

        int getImageResId() { return imageResId; }

        String getCheckEnabledMethod() { return checkEnabledMethod; }

        String getOnClickMethod() { return onClickMethod; }
    }

    public static final CombatButtonData ASSAULT_BUTTON_DATA = new CombatButtonData(R.drawable.assault, METHOD_ASSAULT_ENABLED, METHOD_ASSAULT);
    public static final CombatButtonData ESCAPE_BUTTON_DATA = new CombatButtonData(R.drawable.escape, METHOD_ESCAPE_ENABLED, METHOD_ESCAPE);

    DFCombat combat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_df_combat, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCombat();
        initFighterDropArea();
        initButtons();
    }

    @Override
    public void onResume() {
        super.onResume();
        combat.addAllObservers();
        //noinspection ConstantConditions
        ((CombatRowList) getActivity().findViewById(R.id.combatList)).addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        combat.removeAllObservers();
        //noinspection ConstantConditions
        ((CombatRowList) getActivity().findViewById(R.id.combatList)).deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        for (PageTag tag : PagesAdapter.getPageTags(R.string.tab_title_combat, COMBAT_BUTTON)) {
            CombatButtonData buttonData = (CombatButtonData) tag.getData();
            @SuppressWarnings("ConstantConditions") ImageButton button = getView().findViewWithTag(buttonData.getOnClickMethod());
            button.setEnabled((Boolean) call(buttonData.getCheckEnabledMethod()));
        }
    }

    void initCombat() {
        combat = new DFCombat();
        combat.init();
    }

    private void initFighterDropArea() {
        @SuppressWarnings("ConstantConditions") ImageView fighterDropArea = getView().findViewById(R.id.fighterDropArea);
        fighterDropArea.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (dropAreaAccepts(event)) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DROP:
                            Fighter fighter = CombatCore.findFighterByName(event.getClipData().getItemAt(0).getText().toString());
                            CombatCore.kill(fighter);
                            activateFighterDropArea(v, false);
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            activateFighterDropArea(v, true);
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            activateFighterDropArea(v, false);
                            return true;
                        case DragEvent.ACTION_DRAG_LOCATION:
                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
            }
        });
    }

    private void initButtons() {
        initAddFighterButton(R.id.addLeftFighterButton, Team.LEFT);
        initAddFighterButton(R.id.addRightFighterButton, Team.RIGHT);
        @SuppressWarnings("ConstantConditions") ViewGroup combatButtons = getView().findViewById(R.id.combatButtons);
        for (PageTag tag : PagesAdapter.getPageTags(R.string.tab_title_combat, COMBAT_BUTTON)) {
            combatButtons.addView(createButton((CombatButtonData) tag.getData()));
        }
    }

    private void initAddFighterButton(int buttonResId, final Team team) {
        //noinspection ConstantConditions
        getView().findViewById(buttonResId).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final DFFighterEditor editor = new DFFighterEditor(getContext());
                editor.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        combat.addNewFighter(editor.getFighter(), team);
                    }
                });
                editor.show();
            }
        });
    }

    private boolean dropAreaAccepts(DragEvent event) {
        return event.getClipDescription() != null && Constants.MIMETYPE_FIGHTER.equals(event.getClipDescription().getMimeType(0));
    }

    private void activateFighterDropArea(View view, boolean activate) {
        ((ImageView) view).setImageDrawable(getResources().getDrawable(activate ? R.drawable.skull_green : R.drawable.skull, null));
    }

    private ImageButton createButton(final CombatButtonData data) {
        final ImageButton button = new ImageButton(getContext());
        button.setTag(data.getOnClickMethod());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.default_margin);
        layoutParams.setMargins(margin, margin, margin, margin);
        button.setLayoutParams(layoutParams);
        button.setBackgroundResource(R.drawable.small_button_selector);
        button.setImageResource(data.getImageResId());
        button.setEnabled((Boolean) call(data.getCheckEnabledMethod()));
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                call(data.getOnClickMethod(), button);
            }
        });
        return button;
    }

    private Object call(String methodName) {
        return call(methodName, null);
    }

    private Object call(String methodName, ImageButton button) {
        try {
            if (button != null) {
                return combat.getClass().getMethod(methodName, ImageButton.class).invoke(combat, button);
            } else {
                return combat.getClass().getMethod(methodName).invoke(combat);
            }
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}
