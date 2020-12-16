package com.ecarto.cartoapp.ui.Invoice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.databinding.DialogInvoiceOptionsBinding;

public class InvoiceOptionsDialog extends DialogFragment {
    public static final String TAG = "INVOICE_OPTIONS_DIALOG_TAG";
    static String SELECTED_INVOICE = "SELECTED_INVOICE";

    DialogInvoiceOptionsBinding binding;
    Listener listener = null;

    //TODO: finish this, add a header fragment with the info

    public static InvoiceOptionsDialog newInstance(Object context, InvoiceEntity invoiceEntity) {
        Bundle args = new Bundle();

        args.putSerializable(SELECTED_INVOICE, invoiceEntity);

        InvoiceOptionsDialog fragment = new InvoiceOptionsDialog(context);
        fragment.setArguments(args);
        return fragment;
    }

    public InvoiceOptionsDialog(Object context){
        if (context instanceof Listener){
            listener = (Listener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInvoiceOptionsBinding.inflate(inflater, container, false);

        if (getArguments() != null){
            InvoiceEntity invoiceEntity = (InvoiceEntity) getArguments().getSerializable(SELECTED_INVOICE);



        }

        return binding.getRoot();
    }

    public interface Listener {

    }
}
