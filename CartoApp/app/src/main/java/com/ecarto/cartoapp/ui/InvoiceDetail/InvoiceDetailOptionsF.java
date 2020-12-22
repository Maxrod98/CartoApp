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
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogInvoiceDetailOptionsBinding;
import com.ecarto.cartoapp.utils.ActivityUtils;
import com.ecarto.cartoapp.utils.NAVIGATION;
import com.ecarto.cartoapp.utils.StringUtils;

import io.reactivex.schedulers.Schedulers;

public class InvoiceDetailOptionsF extends Fragment {
    public static final String TAG = "INVOICE_DETAIL_OPTIONS_DIALOG";
    public static final String SELECTED_INVOICE_DETAIL = "SelectedInvoiceDetailID";

    DialogInvoiceDetailOptionsBinding binding;
    InvoiceDetailEntity invoiceDetailEntity;
    InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;

    Long invoiceDetailID;

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

    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Context.MODE_PRIVATE);
    }

    public void getDatabaseData() {
        if (getArguments() != null) {
            invoiceDetailID = getArguments().getLong(SELECTED_INVOICE_DETAIL);

            invoiceDetailEntity = invoiceRepository.findAllInvoiceDetailBy(invoiceDetailID, null)
                    .subscribeOn(Schedulers.io()).blockingGet()
                    .stream().findFirst().orElse(null);
        }
    }

    private void initListeners() {
        if (invoiceDetailEntity != null) {
            binding.didoEdit.setOnClickListener(v -> {
                NavHostFragment.findNavController(this)
                        .navigate(InvoiceDetailOptionsFDirections.actionInvoiceDetailOptionsDialogToInsertInvoiceDetailDialog
                                        (invoiceDetailEntity.getInvoiceDetailID(), invoiceDetailEntity.getInvoiceID()) );

            });

            binding.didoNotes.setOnClickListener(v -> {
                NavHostFragment.findNavController(this)
                        .navigate(InvoiceDetailOptionsFDirections.actionInvoiceDetailOptionsDialogToShowNotesDialog(invoiceDetailEntity.getInvoiceDetailID()));
            });

            binding.didoFiles.setOnClickListener((v) -> {
                NavHostFragment.findNavController(this).navigate(InvoiceDetailOptionsFDirections.actionInvoiceDetailOptionsDialogToFilesF(invoiceDetailID));
            });

            binding.didoDelete.setOnClickListener(v -> {
                deleteInvoiceDetailEntity();
            });
        } else {
            Toast.makeText(getActivity(), "Hubo un error, no se pudo cargar el detalle de factura", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteInvoiceDetailEntity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true)
                .setTitle("Borrar detalle de factura de " + invoiceDetailEntity.getConceptDescription())
                .setMessage("¿Seguro que desea borrar el detalle de la factura?")
                .setPositiveButton("Borrar", (dialog, which) -> {
                    invoiceRepository.deleteInvoiceDetailEntity(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
                    NavHostFragment.findNavController(this).popBackStack();
                })
                .setNegativeButton("Cancelar", ((dialog, which) -> {
                }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface Listener {
    }
}
