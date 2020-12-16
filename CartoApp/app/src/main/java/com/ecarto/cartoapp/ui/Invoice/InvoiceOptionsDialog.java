package com.ecarto.cartoapp.ui.Invoice;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogInvoiceOptionsBinding;
import com.ecarto.cartoapp.utils.ActivityUtils;
import com.ecarto.cartoapp.utils.NAVIGATION;
import com.ecarto.cartoapp.utils.StringUtils;

import io.reactivex.schedulers.Schedulers;

public class InvoiceOptionsDialog extends DialogFragment {
    public static final String TAG = "INVOICE_OPTIONS_DIALOG_TAG";
    static String SELECTED_INVOICE = "SELECTED_INVOICE";

    DialogInvoiceOptionsBinding binding;
    Listener listener = null;
    ExtendedInvoiceEntity invoiceEntity;
    InvoiceRepository invoiceRepository;

    public InvoiceOptionsDialog(){
    }

    public static InvoiceOptionsDialog newInstance(String tagParent, ExtendedInvoiceEntity invoiceEntity) {
        Bundle args = new Bundle();
        args.putSerializable(SELECTED_INVOICE, invoiceEntity);
        args.putString(NAVIGATION.TAG_PARENT,tagParent);
        InvoiceOptionsDialog fragment = new InvoiceOptionsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInvoiceOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        initElems();
        initListeners();
    }

    public void initElems(){
        listener = (Listener) ActivityUtils.getListener(this);
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());

        if (getArguments() != null ){
            invoiceEntity = (ExtendedInvoiceEntity) getArguments().getSerializable(SELECTED_INVOICE);
            binding.txtDescription.setText(invoiceEntity.getDescription());
            binding.txtDate.setText(StringUtils.formatDateFromLong(invoiceEntity.getDate()));
            binding.txtSeller.setText(invoiceEntity.getSeller());
            binding.txtTotalCost.setText(StringUtils.formatMoney(invoiceEntity.getTotalCost()));

        } else {
            Toast.makeText(getActivity(), "Error al cargar datos de factura", Toast.LENGTH_SHORT).show();
        }

    }

    private void initListeners() {
        binding.dioErase.setOnClickListener((v -> {
            deleteInvoice(invoiceEntity);
        }));

        binding.dioEdit.setOnClickListener((v) -> {
            InsertInvoiceDialog insertInvoiceEntityDialog = InsertInvoiceDialog.newInstance(InvoiceFragment.TAG, invoiceEntity);
            insertInvoiceEntityDialog.show(getActivity().getSupportFragmentManager(), InsertInvoiceDialog.TAG);
            dismiss();
        });
    }

    public void deleteInvoice(InvoiceEntity invoiceEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(true);
        builder.setTitle("Borrar  factura de " + invoiceEntity.getDescription());
        builder.setMessage("¿Seguro que desea borrar la factura?");
        builder.setPositiveButton("Borrar", (dialog, which) -> {
            invoiceRepository.deleteInvoiceEntity(invoiceEntity).subscribeOn(Schedulers.io()).blockingGet();
            listener.updateList();
            dismiss();
        });
        builder.setNegativeButton("Cancelar", ((dialog, which) -> {
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface Listener {
        void updateList();
    }
}
