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

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogInsertInvoiceDetailBinding;
import com.ecarto.cartoapp.utils.ActivityUtils;
import com.ecarto.cartoapp.utils.NAVIGATION;

import io.reactivex.schedulers.Schedulers;

public class InsertInvoiceDetailDialog extends DialogFragment {
    private static final String EDIT_CURRENT = "EDIT_CURRENT";
    public static final String TAG = "INSERT_INVOICE_DETAIL_DIALOG_TAG";

    DialogInsertInvoiceDetailBinding binding;
    InsertInvoiceDetailDialog.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceRepository invoiceRepository;
    InvoiceDetailEntity invoiceDetailEntity;

    public static final String CURRENT_INVOICE_ENTITY = "CURRENT_INVOICE_ENTITY";

    public InsertInvoiceDetailDialog() {
    }


    public static InsertInvoiceDetailDialog newInstance(String parent_tag, InvoiceDetailEntity invoiceDetailEntity) {
        Bundle args = new Bundle();
        args.putSerializable( EDIT_CURRENT , invoiceDetailEntity);
        args.putString(NAVIGATION.TAG_PARENT, parent_tag);
        InsertInvoiceDetailDialog fragment = new InsertInvoiceDetailDialog();
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
        listener = ActivityUtils.<Listener>getListener(this);

        if (getArguments() != null){
            invoiceDetailEntity = (InvoiceDetailEntity) getArguments().getSerializable(EDIT_CURRENT);
            if (invoiceDetailEntity != null){
                binding.lblTitle.setText("Editar detalle de factura");
                binding.btnAddInvoiceDetail.setText("Editar");
                binding.etQuantityInsertDetail.setText(String.valueOf(invoiceDetailEntity.getCostOfItem()));
                binding.etProductInsertDetail.setText(invoiceDetailEntity.getConceptDescription());
            }
        }
    }

    public void initListeners(){
        binding.imgClose.setOnClickListener((p) -> {
            dismiss();
        });

        //temp variable
        binding.btnAddInvoiceDetail.setOnClickListener((p) -> {
            Integer invoiceEntityID = Integer.valueOf(sharedPreferences.getInt(getString(R.string.selectedInvoiceEntityID), 0));

            if (invoiceEntityID != 0) {
                InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
                invoiceDetailEntity.setInvoiceID(invoiceEntityID);
                invoiceDetailEntity.setCostOfItem(Integer.valueOf(binding.etQuantityInsertDetail.getText().toString()));
                invoiceDetailEntity.setConceptDescription(binding.etProductInsertDetail.getText().toString());
                invoiceDetailEntity.setInvoiceDetailID(invoiceDetailEntity.getInvoiceID());
                invoiceRepository.insert(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();

                listener.invoiceDetailWasJustAdded();
                dismiss();

            } else {
                Toast.makeText(getActivity().getApplication(), "Error", Toast.LENGTH_SHORT).show();
            } });
    }

    public interface Listener {
        void invoiceDetailWasJustAdded();
    }
}
