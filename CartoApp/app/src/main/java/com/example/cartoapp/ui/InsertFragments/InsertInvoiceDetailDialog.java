package com.example.cartoapp.ui.InsertFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.DialogInsertInvoiceDetailBinding;

import io.reactivex.schedulers.Schedulers;

public class InsertInvoiceDetailDialog extends DialogFragment {
    private static final String EDIT_CURRENT = "EDIT_CURRENT";
    DialogInsertInvoiceDetailBinding binding;
    InsertInvoiceDetailDialog.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceRepository invoiceRepository;

    public static final String CURRENT_INVOICE_ENTITY = "CURRENT_INVOICE_ENTITY";

    public InsertInvoiceDetailDialog() {
    }

    public InsertInvoiceDetailDialog(Object context) {
        if (context instanceof InsertInvoiceDetailDialog.Listener) {
            listener = (InsertInvoiceDetailDialog.Listener) context;
        }
    }

    public static InsertInvoiceDetailDialog newInstance(Object context, InvoiceDetailEntity invoiceDetailEntity) {
        Bundle args = new Bundle();
        if (invoiceDetailEntity != null){
            args.putSerializable( EDIT_CURRENT ,invoiceDetailEntity);
        }
        InsertInvoiceDetailDialog fragment = new InsertInvoiceDetailDialog(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInsertInvoiceDetailBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        Integer invoiceDetailID = null;
        if (getArguments() != null){
            InvoiceDetailEntity invoiceDetailEntity = (InvoiceDetailEntity) getArguments().getSerializable(EDIT_CURRENT);
            if (invoiceDetailEntity != null){
                binding.lblTitle.setText("Editar detalle de factura");
                binding.btnAddInvoiceDetail.setText("Editar");
                binding.etQuantityInsertDetail.setText(String.valueOf(invoiceDetailEntity.getCostOfItem()));
                binding.etProductInsertDetail.setText(invoiceDetailEntity.getConceptDescription());
                invoiceDetailID = invoiceDetailEntity.getInvoiceDetailID();
            }

        }

        final Integer finalInvoiceDetailID = invoiceDetailID; //temp variable
        binding.btnAddInvoiceDetail.setOnClickListener((p) -> {
            Integer invoiceEntityID = Integer.valueOf(sharedPreferences.getInt(getString(R.string.selectedInvoiceEntityID), 0));

            if (invoiceEntityID != 0) {
                InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
                invoiceDetailEntity.setInvoiceID(invoiceEntityID);
                invoiceDetailEntity.setCostOfItem(Integer.valueOf(binding.etQuantityInsertDetail.getText().toString()));
                invoiceDetailEntity.setConceptDescription(binding.etProductInsertDetail.getText().toString());
                invoiceDetailEntity.setInvoiceDetailID(finalInvoiceDetailID);
                invoiceRepository.insert(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();

                listener.invoiceDetailWasJustAdded();
                dismiss();
            } else {
                Toast.makeText(getActivity().getApplication(), "Error", Toast.LENGTH_SHORT).show();
            } });
        binding.imgClose.setOnClickListener((p) -> {
            dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface Listener {
        void invoiceDetailWasJustAdded();
    }
}
