package com.example.cartoapp.utils;

import android.os.Message;

import com.example.cartoapp.ui.Invoice.InvoiceAdapter;

public class Selector {
    public static Integer NONE_SELECTED = -1;
    public static Integer LAST_SELECTED = -2;

    public Integer getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Integer selectedItem) {
        this.selectedItem = selectedItem;
    }

    Integer selectedItem = -1;
    Selector.Listener listener = null;

    public Selector( Object context) {
        if (context instanceof Selector.Listener)
        {
            listener = (Selector.Listener) context;
            selectedItem = listener.getSelectedPosition();

            if( selectedItem != null){
                if (selectedItem == LAST_SELECTED) {
                    makeSelected(listener.getNumElems() - 1);
                }
            }
        } else {
            throw new RuntimeException("Adapter's context is not using the Listener interface");
        }

    }

    public void onItemClickSelection(Integer position) {
        if (selectedItem == NONE_SELECTED) {
            makeSelected(position);
        } else if (selectedItem == LAST_SELECTED) {
            makeSelected(listener.getNumElems());
        }
        else if (selectedItem == position) {
            makeDeselected(position);
        }
        else {
            makeSelected(position);
        }
    }


    public void makeSelected(Integer position) {
        selectedItem = position;
        listener.setSelectedPosition(position);
    }

    public void makeDeselected(Integer position) {
        selectedItem = NONE_SELECTED;
    }

    public Boolean isSelected(Integer position) {
        return selectedItem == position;
    }

    public interface Listener{
        void setSelectedPosition(Integer position);
        Integer getSelectedPosition();
        Integer getNumElems();
    }
}
