package com.ecarto.cartoapp.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.databinding.FragmentLoginBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class LoginF extends Fragment {
    FragmentLoginBinding binding;
    SharedPreferences sharedPreferences;
    Integer todayNum;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);

        setTodayNum();

        Integer lastDayLogin = sharedPreferences.getInt(getResources().getString(R.string.lastDayLogin), -1);
        if (lastDayLogin == todayNum && lastDayLogin != -1){
            goToInvoiceFragment();
        }

        initListeners();

        return binding.getRoot();
    }

    public void setTodayNum(){
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        todayNum = c.get(Calendar.DAY_OF_MONTH);
    }

    public void goToInvoiceFragment(){
        NavHostFragment.findNavController(this).navigate(LoginFDirections.actionLoginFToInvoiceFragment());
    }

    private void initListeners() {
        binding.btnLogin.setOnClickListener((v) -> {
            if (binding.txtPassword.getText().toString().isEmpty() || binding.txtUser.getText().toString().isEmpty()){
                Snackbar.make(binding.getRoot(), "Necesita rellenar todos los campos", Snackbar.LENGTH_SHORT).show();
            } else {
                goToInvoiceFragment();
                sharedPreferences.edit().putInt(getResources().getString(R.string.lastDayLogin), todayNum).commit();
            }


        });
    }

}
