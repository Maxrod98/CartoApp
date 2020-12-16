package com.ecarto.cartoapp.ui.Invoice;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogInsertInvoiceEntityBinding;
import com.ecarto.cartoapp.utils.ActivityUtils;
import com.ecarto.cartoapp.utils.NAVIGATION;
import com.ecarto.cartoapp.utils.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.schedulers.Schedulers;

public class InsertInvoiceDialog extends DialogFragment {
    public static final String TAG = "INSERT_INVOICE_DIALOG_TAG";
    public static final String EDITED_INVOICE = "EDITED_INVOICE";

    DialogInsertInvoiceEntityBinding binding;
    InsertInvoiceDialog.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceRepository invoiceRepository;
    InvoiceEntity invoiceEntity;

    Integer day;
    Integer month;
    Integer year_;

    public InsertInvoiceDialog() {
    }

    public static InsertInvoiceDialog newInstance(String tagParent, InvoiceEntity invoiceEntity) {
        Bundle args = new Bundle();
        args.putString(NAVIGATION.TAG_PARENT, tagParent);
        args.putSerializable(EDITED_INVOICE, invoiceEntity);
        InsertInvoiceDialog fragment = new InsertInvoiceDialog();
        fragment.setArguments(args);
        return fragment;
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
            dismiss();
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
        invoiceEntity = (InvoiceEntity) getArguments().getSerializable(EDITED_INVOICE);
        listener = ActivityUtils.getListener(this);

        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year_ = calendar.get(Calendar.YEAR);


        if (invoiceEntity == null) {
            binding.etDateInvoice.setText(day + "/" + (month + 1) + "/" + year_);
        } else {
            binding.etDateInvoice.setText(StringUtils.formatDateFromLong(invoiceEntity.getDate()));
            binding.etDescription.setText(invoiceEntity.getDescription());
            binding.etSeller.setText(invoiceEntity.getSeller());
            binding.btnAddInvoice.setText("MODIFICAR FACTURA");
        }

    }


    public void createAndSendEntity() {
        InvoiceEntity entity = (invoiceEntity == null ? new InvoiceEntity() : invoiceEntity);

        entity.setDate(StringUtils.formatDateFromString(binding.etDateInvoice.getText().toString()));
        entity.setSeller(binding.etSeller.getText().toString());
        entity.setDescription(binding.etDescription.getText().toString());
        entity.setProjectID(1);
        entity.setInvoiceID(entity.getInvoiceID());
        invoiceRepository.insert(entity).subscribeOn(Schedulers.io()).blockingGet();

        if (entity.getInvoiceID() == null) {
            listener.invoiceEntityWasInserted();

        } else {
            listener.invoiceEntityWasEdited();
        }

        dismiss();
    }


    public interface Listener {
        void invoiceEntityWasInserted();

        void invoiceEntityWasEdited();
    }
}
