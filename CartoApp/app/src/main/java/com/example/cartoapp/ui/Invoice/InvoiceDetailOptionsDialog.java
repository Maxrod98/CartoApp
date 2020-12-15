package com.example.cartoapp.ui.Invoice;

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

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.DialogInvoiceDetailOptionsBinding;
import com.example.cartoapp.ui.InsertFragments.InsertInvoiceDetailDialog;
import com.example.cartoapp.ui.ShowNotes.ShowNotesDialog;

import io.reactivex.schedulers.Schedulers;

public class InvoiceDetailOptionsDialog extends DialogFragment {
    DialogInvoiceDetailOptionsBinding binding;
    InvoiceDetailEntity invoiceDetailEntity;
    InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    Listener listener = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.anim.slide_in;
        return dialog;
    }

    public static InvoiceDetailOptionsDialog newInstance(Object context) {

        Bundle args = new Bundle();

        InvoiceDetailOptionsDialog fragment = new InvoiceDetailOptionsDialog(context);
        fragment.setArguments(args);
        return fragment;
    }

    public InvoiceDetailOptionsDialog(Object context){
        if (context instanceof Listener){
            listener = (Listener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInvoiceDetailOptionsBinding.inflate(inflater, container, false);
        initElems();

        return binding.getRoot();
    }

    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Context.MODE_PRIVATE);

        Integer selectedInvoiceDetailID =  sharedPreferences.getInt(getString(R.string.selectedInvoiceDetailID), 0);
        invoiceDetailEntity = invoiceRepository.findAllInvoiceDetailBy(selectedInvoiceDetailID, null)
                .subscribeOn(Schedulers.io()).blockingGet()
                .stream().findFirst().orElse(null);

        if (invoiceDetailEntity != null){
            binding.didoEdit.setOnClickListener(v -> {
                InsertInvoiceDetailDialog insertInvoiceDetailDialog = InsertInvoiceDetailDialog.newInstance(this, invoiceDetailEntity);
                insertInvoiceDetailDialog.show(getActivity().getSupportFragmentManager(), "InsertInvoiceDetailDialog");
            });

            binding.didoNotes.setOnClickListener(v -> {
                ShowNotesDialog showNotesDialog = ShowNotesDialog.newInstance(invoiceDetailEntity);
                showNotesDialog.show(getActivity().getSupportFragmentManager(), "ShowNotesDialog");
            });

            binding.didoDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setCancelable(true);
                builder.setTitle("Borrar detalle de factura de " + invoiceDetailEntity.getConceptDescription() );
                builder.setMessage("¿Seguro que desea borrar el detalle de la factura?");
                builder.setPositiveButton("Borrar", (dialog, which) -> {
                    invoiceRepository.deleteInvoiceDetailEntity(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
                });
                builder.setNegativeButton("Cancelar", ((dialog, which) -> { }));

                AlertDialog dialog = builder.create();
                dialog.show();
            });


        } else {
            Toast.makeText(getActivity(), "Hubo un error", Toast.LENGTH_SHORT).show();
        }

    }

    public interface Listener{

    }

}
