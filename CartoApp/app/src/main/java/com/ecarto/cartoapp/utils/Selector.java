package com.ecarto.cartoapp.utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class Selector {
    public static Integer NONE_SELECTED = -1;
    public static Integer LAST_SELECTED = -2;
    public static String SELECTED_POSITION = "SelectedPosition";

    Fragment fragment = null;

    public Selector(Fragment context) {
        if (context instanceof Fragment) {
            fragment =  context;
        } else {
            throw new RuntimeException("Selector's context is not a fragment");
        }
    }

    public void onItemClickSelection(Integer position) {
        if (getSelectedPosition() == NONE_SELECTED) {
            makeSelected(position);
        } else if (getSelectedPosition() == LAST_SELECTED) {
            //makeSelected(listener.getNumElems());
        } else {
            makeSelected(position);
        }
    }

    public void makeSelected(Integer position) {
        setSelectedPosition(position);
    }

    public Boolean isSelected(Integer position) {
        return getSelectedPosition() == position;
    }

    public Integer getSelectedPosition(){
        if (fragment.getArguments() != null){
            return fragment.getArguments().getInt(SELECTED_POSITION);
        }
        return NONE_SELECTED;
    }

    public void setSelectedPosition(Integer position){
        if (fragment.getArguments() != null){
            Bundle bundle = fragment.getArguments();
            bundle.putInt(SELECTED_POSITION, position );
            fragment.setArguments(bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(SELECTED_POSITION, position);
            fragment.setArguments(bundle);
        }
    }
}
