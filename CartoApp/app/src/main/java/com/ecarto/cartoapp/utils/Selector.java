package com.ecarto.cartoapp.utils;

import androidx.fragment.app.Fragment;

public class Selector {
    public static Integer NONE_SELECTED = -1;
    public static Integer LAST_SELECTED = -2;

    Integer selectedItem = NONE_SELECTED;
    Fragment fragment = null;

    public Selector(Fragment context) {
        if (context instanceof Fragment) {
            fragment =  context;
        } else {
            throw new RuntimeException("Selector's context is not a fragment");
        }
    }

    public void onItemClickSelection(Integer position) {
        if (selectedItem == NONE_SELECTED) {
            makeSelected(position);
        } else if (selectedItem == LAST_SELECTED) {
            //makeSelected(listener.getNumElems());
        } else {
            makeSelected(position);
        }
    }

    public void makeSelected(Integer position) {
        selectedItem = position;
    }

    public void makeDeselected(Integer position) {
        selectedItem = NONE_SELECTED;
    }

    public Boolean isSelected(Integer position) {
        return selectedItem == position;
    }

}
