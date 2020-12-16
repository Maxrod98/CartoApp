package com.ecarto.cartoapp.ui.InvoiceDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogInvoiceDetailOptionsBinding;
import com.ecarto.cartoapp.utils.ActivityUtils;
import com.ecarto.cartoapp.utils.NAVIGATION;

import io.reactivex.schedulers.Schedulers;

public class InvoiceDetailOptionsDialog extends DialogFragment {
    public static final String TAG = "INVOICE_DETAIL_OPTIONS_DIALOG";

    DialogInvoiceDetailOptionsBinding binding;
    InvoiceDetailEntity invoiceDetailEntity;
    InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    Listener listener = null;

    public InvoiceDetailOptionsDialog() {
    }

    public static InvoiceDetailOptionsDialog newInstance(String tagParent) {
        Bundle args = new Bundle();
        args.putString(NAVIGATION.TAG_PARENT, tagParent);

        InvoiceDetailOptionsDialog fragment = new InvoiceDetailOptionsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInvoiceDetailOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initElems();
        getDatabaseData();
        initListeners();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.anim.slide_in;
        return dialog;
    }

    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Context.MODE_PRIVATE);
        listener = ActivityUtils.getListener(this);
    }

    public void getDatabaseData(){
        Integer selectedInvoiceDetailID = sharedPreferences.getInt(getString(R.string.selectedInvoiceDetailID), 0);

        invoiceDetailEntity = invoiceRepository.findAllInvoiceDetailBy(selectedInvoiceDetailID, null)
                .subscribeOn(Schedulers.io()).blockingGet()
                .stream().findFirst().orElse(null);
    }

    private void initListeners() {
        if (invoiceDetailEntity != null) {
            binding.didoEdit.setOnClickListener(v -> {
                InsertInvoiceDetailDialog insertInvoiceDetailDialog = InsertInvoiceDetailDialog.newInstance(InvoiceDetailFragment.TAG, invoiceDetailEntity);
                insertInvoiceDetailDialog.show(getActivity().getSupportFragmentManager(), InsertInvoiceDetailDialog.TAG);
            });

            binding.didoNotes.setOnClickListener(v -> {
                ShowNotesDialog showNotesDialog = ShowNotesDialog.newInstance(invoiceDetailEntity);
                showNotesDialog.show(getActivity().getSupportFragmentManager(), ShowNotesDialog.TAG);
            });

            binding.didoDelete.setOnClickListener(v -> {
                deleteInvoiceDetailEntity();
            });
        } else {
            Toast.makeText(getActivity(), "Hubo un error, no se pudo cargar el detalle de factura", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteInvoiceDetailEntity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true)
                .setTitle("Borrar detalle de factura de " + invoiceDetailEntity.getConceptDescription())
                .setMessage("¿Seguro que desea borrar el detalle de la factura?")
                .setPositiveButton("Borrar", (dialog, which) -> {
                    invoiceRepository.deleteInvoiceDetailEntity(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
                    listener.refreshInvoiceDetailList();
                    dismiss();
                })
                .setNegativeButton("Cancelar", ((dialog, which) -> {
                }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface Listener {
        void refreshInvoiceDetailList();
    }

}
