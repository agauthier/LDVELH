package com.home.ldvelh.ui.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.DF04Character;
import com.home.ldvelh.model.combat.DF04Combat;
import com.home.ldvelh.model.value.DF04AssetValueHolder;

public class DF04CombatPage extends CombatPage {

    @Override
    public void initCombat() {
        combat = new DF04Combat();
        combat.init();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        //noinspection ConstantConditions
        fragmentView.setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (dropAreaAccepts(event)) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DROP:
                            CharSequence dragData = event.getClipData().getItemAt(0).getText();
                            Property<DF04AssetValueHolder> crewMember = Property.getPropertyByName(dragData.toString());
                            //noinspection ConstantConditions
                            if (crewMember.get().isCommander()) {
                                ((DF04Character) Property.CHARACTER.get()).commanderDescendsOnPlanet();
                            } else if (crewMember.get().isShip()) {
                                ((DF04Character) Property.CHARACTER.get()).startSpaceShipFight();
                            } else {
                                crewMember.get().descendOnPlanet();
                            }
                            activateDropArea(false);
                            combat.reset();
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            activateDropArea(true);
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            activateDropArea(false);
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
        return fragmentView;
    }

    private boolean dropAreaAccepts(DragEvent event) {
        return event.getClipDescription() != null && Constants.MIMETYPE_DF04CREWMEMBER.equals(event.getClipDescription().getMimeType(0));
    }

    private void activateDropArea(boolean activate) {
        //noinspection ConstantConditions
        getView().setBackground(getResources().getDrawable(activate ? R.drawable.pencil_border_green : R.drawable.pencil_border, null));
    }
}
