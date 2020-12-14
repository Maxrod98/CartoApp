package com.example.cartoapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cartoapp.R;

public class BaseActivity extends AppCompatActivity {

    public void navigateTo(Fragment fragment) {
        navigateTo(fragment, true, fragment.getClass().getSimpleName());
    }

    public void navigateTo(Fragment fragment, boolean addToBackStack, String fragmentTag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (addToBackStack) fragmentTransaction = fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainActivityFragmentContainer, fragment, fragmentTag).commit();
    }
}
