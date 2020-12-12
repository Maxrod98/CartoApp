package com.example.cartoapp.ui.InsertFragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.DialogInsertInvoiceEntityBinding;

import io.reactivex.schedulers.Schedulers;

public class InsertInvoiceDialog extends DialogFragment {
    DialogInsertInvoiceEntityBinding binding;
    InsertInvoiceDialog.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceRepository invoiceRepository;

    public InsertInvoiceDialog(Object context) {
        if (context instanceof InsertInvoiceDialog.Listener) {
            listener = (InsertInvoiceDialog.Listener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static InsertInvoiceDialog newInstance(Object context) {
        Bundle args = new Bundle();

        InsertInvoiceDialog fragment = new InsertInvoiceDialog(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInsertInvoiceEntityBinding.inflate(inflater, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        binding.btnAddInvoice.setOnClickListener((p) -> {
            createAndSendEntity();
        });
        binding.imgClose.setOnClickListener((v) -> {
            dismiss();
        });

        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        //binding.calDate.setDate();

        return binding.getRoot();
    }

    public void createAndSendEntity() {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setDate(binding.calDate.getDate());
        invoiceEntity.setSeller(binding.etSeller.getText().toString());
        invoiceEntity.setDescription(binding.etDescription.getText().toString());
        invoiceEntity.setProjectID(1);
        invoiceRepository.insert(invoiceEntity).subscribeOn(Schedulers.io()).blockingGet();
        listener.invoiceEntityWasInserted();
        dismiss();
    }


    public interface Listener {
        void invoiceEntityWasInserted();
    }
}
