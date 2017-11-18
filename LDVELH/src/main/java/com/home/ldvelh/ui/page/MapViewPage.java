package com.home.ldvelh.ui.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.ui.widget.map.MapOps;

import java.util.Observable;
import java.util.Observer;

public class MapViewPage extends Fragment implements Observer {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_mapview, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponents();
    }

    @Override
    public void onResume() {
        super.onResume();
        Property.MAP.get().getValue().addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Property.MAP.get().getValue().deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        initComponents();
    }

    @SuppressWarnings("ConstantConditions")
    private void initComponents() {
        ImageButton fitMapToCanvasButton = getView().findViewById(R.id.fitMapToCanvas);
        fitMapToCanvasButton.setEnabled(MapOps.canFitMapToCanvas());
        fitMapToCanvasButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isKeyboardShowing()) {
                    MapOps.fitMapToCanvas(getActivity().findViewById(R.id.mapView));
                }
            }
        });
        ImageButton deleteNodeButton = getView().findViewById(R.id.deleteNode);
        deleteNodeButton.setEnabled(MapOps.canDeleteNode());
        deleteNodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isKeyboardShowing()) {
                    MapOps.deleteNode(getActivity().findViewById(R.id.mapView));
                }
            }
        });
    }
}
