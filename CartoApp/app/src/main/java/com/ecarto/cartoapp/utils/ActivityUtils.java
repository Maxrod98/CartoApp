package com.ecarto.cartoapp.utils;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.ui.InsertFragments.InsertInvoiceDetailDialog;
import com.google.android.material.snackbar.Snackbar;

public class ActivityUtils {

    //implement this
    public static void showSnackbar(View view, String message, int colorId) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(view.getResources().getColor(colorId))
                .setTextColor(view.getResources().getColor(R.color.white))
                .show();
    }

    public static <Listener> Listener getListener(Fragment fragment){
        Listener listener = null;
        String parentTag = fragment.getArguments().getString(NAVIGATION.TAG_PARENT, "");
        if (parentTag.isEmpty()){
            listener = (Listener) fragment.getActivity();
        } else {
            listener = (Listener) fragment.getActivity().getSupportFragmentManager().findFragmentByTag(parentTag);
        }
        return listener;
    }
}
