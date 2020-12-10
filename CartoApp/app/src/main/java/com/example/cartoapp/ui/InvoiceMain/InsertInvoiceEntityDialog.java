package com.example.cartoapp.ui.InvoiceMain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.databinding.DialogInsertInvoiceEntityBinding;

public class InsertInvoiceEntityDialog extends DialogFragment {
    DialogInsertInvoiceEntityBinding binding;
    InsertInvoiceEntityDialog.Listener listener;

    public InsertInvoiceEntityDialog(Context context) {
        if (context instanceof InsertInvoiceEntityDialog.Listener){
            listener = (InsertInvoiceEntityDialog.Listener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    public static InsertInvoiceEntityDialog newInstance(Context context) {
        Bundle args = new Bundle();

        InsertInvoiceEntityDialog fragment = new InsertInvoiceEntityDialog(context);
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

        binding.btnAddInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndSendEntity();
            }
        });
        //binding.calDate.setDate();

        return binding.getRoot();
    }

    public void createAndSendEntity(){
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setDate(binding.calDate.getDate());
        invoiceEntity.setSeller(binding.etSeller.getText().toString());
        invoiceEntity.setDescription(binding.etDescription.getText().toString());
        invoiceEntity.setProjectID(1);
        listener.insertInvoiceEntity(invoiceEntity);
        dismiss();
    }


    public interface Listener{
        void insertInvoiceEntity(InvoiceEntity invoiceEntity);
    }
}
