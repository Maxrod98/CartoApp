package com.ecarto.cartoapp.ui.InvoiceDetail;

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
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogInsertInvoiceDetailBinding;
import com.ecarto.cartoapp.utils.NAVIGATION;

import io.reactivex.schedulers.Schedulers;

public class InsertInvoiceDetailF extends DialogFragment {
    private static final String EDIT_CURRENT = "SelectedInvoiceDetailID";
    public static final String TAG = "INSERT_INVOICE_DETAIL_DIALOG_TAG";
    private static final String INVOICE_ID = "SelectedInvoiceID";

    DialogInsertInvoiceDetailBinding binding;
    InsertInvoiceDetailF.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceRepository invoiceRepository;
    InvoiceDetailEntity invoiceDetailEntity;
    Integer invoiceDetailID;
    Integer invoiceID;

    public static final String CURRENT_INVOICE_ENTITY = "CURRENT_INVOICE_ENTITY";

    public InsertInvoiceDetailF() {
    }


    public static InsertInvoiceDetailF newInstance(String parent_tag, InvoiceDetailEntity invoiceDetailEntity) {
        Bundle args = new Bundle();
        args.putSerializable(EDIT_CURRENT, invoiceDetailEntity);
        args.putString(NAVIGATION.TAG_PARENT, parent_tag);
        InsertInvoiceDetailF fragment = new InsertInvoiceDetailF();
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
            invoiceDetailID = getArguments().getInt(EDIT_CURRENT);
            invoiceID = getArguments().getInt(INVOICE_ID);
            if (invoiceDetailID != 0) {
                invoiceDetailEntity = invoiceRepository.findAllExtendedInvoiceDetailBy(invoiceDetailID, null)
                        .subscribeOn(Schedulers.io()).blockingGet()
                        .stream().findFirst().orElse(null);

                if (invoiceDetailEntity != null) {
                    binding.lblTitle.setText("Editar detalle de factura");
                    binding.btnAddInvoiceDetail.setText("Editar");
                    binding.etQuantityInsertDetail.setText(String.valueOf(invoiceDetailEntity.getCostOfItem()));
                    binding.etProductInsertDetail.setText(invoiceDetailEntity.getConceptDescription());
                } else {
                    Toast.makeText(getContext(), "Error al cargar detalle de factura", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void initListeners() {

        //temp variable
        binding.btnAddInvoiceDetail.setOnClickListener((p) -> {

            InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
            invoiceDetailEntity.setInvoiceID(invoiceID);
            invoiceDetailEntity.setCostOfItem(Integer.valueOf(binding.etQuantityInsertDetail.getText().toString()));
            invoiceDetailEntity.setConceptDescription(binding.etProductInsertDetail.getText().toString());
            invoiceDetailEntity.setInvoiceDetailID(invoiceDetailID == 0 ? null : invoiceDetailID); //makes sure that a new detail entity is inserted or the detail entity is updated
            invoiceRepository.insert(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();

            NavHostFragment.findNavController(this).popBackStack();
        });


    }

    public interface Listener {
        void invoiceDetailWasJustAdded();
    }
}
