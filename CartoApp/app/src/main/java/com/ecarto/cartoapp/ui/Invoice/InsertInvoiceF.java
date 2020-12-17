package com.ecarto.cartoapp.ui.Invoice;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogInsertInvoiceEntityBinding;
import com.ecarto.cartoapp.utils.StringUtils;

import java.util.Calendar;


import io.reactivex.schedulers.Schedulers;

public class InsertInvoiceF extends Fragment {
    public static final String TAG = "INSERT_INVOICE_DIALOG_TAG";
    public static final String EDITED_INVOICE = "SelectedInvoiceID";

    DialogInsertInvoiceEntityBinding binding;
    InsertInvoiceF.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceRepository invoiceRepository;
    Integer invoiceID;

    Integer day;
    Integer month;
    Integer year_;

    public InsertInvoiceF() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInsertInvoiceEntityBinding.inflate(inflater, container, false);

        initElems();
        initListeners();

        return binding.getRoot();
    }


    private void initListeners() {
        binding.btnAddInvoice.setOnClickListener((p) -> {
            createAndSendEntity();
        });
        binding.imgClose.setOnClickListener((v) -> {
            //TODO: remove
        });

        binding.etDateInvoice.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatePickerDialog picker;
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        (view, year, monthOfYear, dayOfMonth) -> {
                            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            binding.etDateInvoice.setText(date);
                        }, year_, month, day);
                picker.show();
            }
        });
    }

    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        invoiceID = getArguments().getInt(EDITED_INVOICE);
        //listener = ActivityUtils.getListener(this);

        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year_ = calendar.get(Calendar.YEAR);


        if (invoiceID == 0) {
            binding.etDateInvoice.setText(day + "/" + (month + 1) + "/" + year_);
        } else {
            InvoiceEntity invoiceEntity = invoiceRepository.findAllExtendedInvoiceBy(invoiceID)
                    .subscribeOn(Schedulers.io()).blockingGet()
                    .stream().findFirst().orElse(null);

            if (invoiceEntity == null) {
                Toast.makeText(getContext(), "Hubo un error all tratar de cargar la factura", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.etDateInvoice.setText(StringUtils.formatDateFromLong(invoiceEntity.getDate()));
            binding.etDescription.setText(invoiceEntity.getDescription());
            binding.etSeller.setText(invoiceEntity.getSeller());
            binding.btnAddInvoice.setText("MODIFICAR FACTURA");
        }

    }


    public void createAndSendEntity() {
        InvoiceEntity entity = new InvoiceEntity();

        if (invoiceID == 0) invoiceID = null;

        entity.setDate(StringUtils.formatDateFromString(binding.etDateInvoice.getText().toString()));
        entity.setSeller(binding.etSeller.getText().toString());
        entity.setDescription(binding.etDescription.getText().toString());
        entity.setProjectID(1);
        entity.setInvoiceID(invoiceID);
        invoiceRepository.insert(entity).subscribeOn(Schedulers.io()).blockingGet();

        NavHostFragment.findNavController(this).popBackStack();
    }


    public interface Listener {


        void invoiceEntityWasEdited();
    }
}
