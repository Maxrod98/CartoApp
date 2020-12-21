package com.ecarto.cartoapp.ui.InvoiceDetail;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogInsertInvoiceDetailBinding;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.schedulers.Schedulers;

public class InsertInvoiceDetailF extends Fragment {
    public static final String TAG = "INSERT_INVOICE_DETAIL_DIALOG_TAG";
    private static final String SelectedInvoiceDetailID = "SelectedInvoiceDetailID";
    public static final String SelectedInvoiceID = "SelectedInvoiceID";

    DialogInsertInvoiceDetailBinding binding;
    InsertInvoiceDetailF.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceRepository invoiceRepository;

    boolean invoiceDetailExists;
    Long invoiceDetailID;
    Long invoiceID;

    public InsertInvoiceDetailF() {
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
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        initElems();
        initListeners();
    }

    private void initElems() {
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        //listener = ActivityUtils.<Listener>getListener(this);

        if (getArguments() != null) {
            invoiceDetailID = getArguments().getLong(SelectedInvoiceDetailID);
            invoiceID = getArguments().getLong(SelectedInvoiceID);

            invoiceDetailExists = invoiceDetailID != 0;

            if (invoiceDetailExists) {
                InvoiceDetailEntity invoiceDetailEntity = getInvoiceDetailEntity();

                binding.lblTitle.setText("Editar detalle de factura");
                binding.btnAddInvoiceDetail.setText("Editar");
                binding.etQuantityInsertDetail.setText(String.valueOf(invoiceDetailEntity.getCostOfItem()));
                binding.etProductInsertDetail.setText(invoiceDetailEntity.getConceptDescription());

            }
        }
    }

    public void initListeners() {
        //temp variable
        binding.btnAddInvoiceDetail.setOnClickListener((p) -> {
            createOrModifyEntity();
        });

        binding.imgClose.setOnClickListener((v) -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void createOrModifyEntity() {
        String description = binding.etProductInsertDetail.getText().toString();
        String quantity = binding.etQuantityInsertDetail.getText().toString();

        if (description.isEmpty() || quantity.isEmpty()) { //on error
            Snackbar.make(binding.getRoot(), "Necesita completar los campos", Snackbar.LENGTH_LONG).show();
        } else { // if correct
            InvoiceDetailEntity invoiceDetailEntity = getInvoiceDetailEntity();
            try {
                invoiceDetailEntity.setConceptDescription(description); //TODO get the data directly from database, then update
                invoiceDetailEntity.setInvoiceID(invoiceID);
                invoiceDetailEntity.setCostOfItem(Integer.valueOf(quantity));

                if (invoiceDetailExists) {
                    Snackbar.make(binding.getRoot(), "Articulo editado correctamente.", Snackbar.LENGTH_SHORT).show();
                    invoiceRepository.updateInvoiceDetailEntity(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
                } else {
                    invoiceRepository.insertInvoiceDetailEntity(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
                }
                NavHostFragment.findNavController(this).popBackStack();
            } catch (Exception e) {
                Snackbar.make(binding.getRoot(), "Error al cargar la cantidad, cheque que introduzca el numero correcto.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public InvoiceDetailEntity getInvoiceDetailEntity() {
        try {
            InvoiceDetailEntity entity = invoiceRepository.findAllExtendedInvoiceDetailBy(invoiceDetailID, null)
                    .subscribeOn(Schedulers.io()).blockingGet()
                    .stream().findFirst().orElse(null);
            return entity != null ? entity : new InvoiceDetailEntity();
        } catch (Exception e) {
            return new ExtendedInvoiceDetailEntity();
        }
    }

    public interface Listener {
        void invoiceDetailWasJustAdded();
    }
}
