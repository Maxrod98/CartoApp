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
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.databinding.DialogInsertInvoiceDetailBinding;
import com.example.cartoapp.databinding.DialogInsertInvoiceEntityBinding;

public class InsertInvoiceDetailDialog extends DialogFragment {
    DialogInsertInvoiceDetailBinding binding;
    InsertInvoiceDetailDialog.Listener listener;
    SharedPreferences sharedPreferences;

    public static final String CURRENT_INVOICE_ENTITY = "CURRENT_INVOICE_ENTITY";

    public InsertInvoiceDetailDialog() {
    }

    public InsertInvoiceDetailDialog(Context context) {
        if (context instanceof InsertInvoiceDetailDialog.Listener) {
            listener = (InsertInvoiceDetailDialog.Listener) context;
        }
    }

    public static InsertInvoiceDetailDialog newInstance(Context context) {

        Bundle args = new Bundle();
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
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInsertInvoiceDetailBinding.inflate(inflater, container, false);

        binding.btnAddInvoiceDetail.setOnClickListener((p) -> {
            Integer invoiceEntityID = Integer.valueOf(sharedPreferences.getInt(getString(R.string.selectedInvoiceEntityID), 0));

            if (invoiceEntityID != 0) {
                InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
                invoiceDetailEntity.setInvoiceID(invoiceEntityID);
                invoiceDetailEntity.setQuantity(Integer.valueOf(binding.etQuantityInsertDetail.getText().toString()));
                invoiceDetailEntity.setProduct(binding.etProductInsertDetail.getText().toString());
                invoiceDetailEntity.setUnit(binding.etTxtUnitInsertDetail.getText().toString());
                invoiceDetailEntity.setCost(Double.valueOf(binding.etCostInsertDetail.getText().toString()));
                invoiceDetailEntity.setTotalCostOfItem(invoiceDetailEntity.getCost() * invoiceDetailEntity.getQuantity());

                listener.insertInvoiceDetail(invoiceDetailEntity);
                dismiss();
            } else {
                Toast.makeText(getActivity().getApplication(), "Error", Toast.LENGTH_SHORT).show();
            }

        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface Listener {
        void insertInvoiceDetail(InvoiceDetailEntity invoiceDetailEntity);
    }
}
