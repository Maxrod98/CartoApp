package com.ecarto.cartoapp.ui.Invoice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecarto.cartoapp.databinding.FragmentDownloadDataBinding;

public class DownloadDataF extends Fragment {
    FragmentDownloadDataBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDownloadDataBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


}
