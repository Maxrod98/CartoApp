package com.ecarto.cartoapp.ui.InsertFragments;

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
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogInsertInvoiceEntityBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.schedulers.Schedulers;

public class InsertInvoiceDialog extends DialogFragment {
    public static final String TAG = "INSERT_INVOICE_DIALOG_TAG";

    DialogInsertInvoiceEntityBinding binding;
    InsertInvoiceDialog.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceRepository invoiceRepository;

    public InsertInvoiceDialog(Object context) {
        if (context instanceof InsertInvoiceDialog.Listener) {
            listener = (InsertInvoiceDialog.Listener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static InsertInvoiceDialog newInstance(Object context) {
        Bundle args = new Bundle();

        InsertInvoiceDialog fragment = new InsertInvoiceDialog(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInsertInvoiceEntityBinding.inflate(inflater, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        binding.btnAddInvoice.setOnClickListener((p) -> {
            createAndSendEntity();
        });
        binding.imgClose.setOnClickListener((v) -> {
            dismiss();
        });

        final Calendar calendar = Calendar.getInstance();
        int day_ = calendar.get(Calendar.DAY_OF_MONTH);
        int month_ = calendar.get(Calendar.MONTH);
        int year_ = calendar.get(Calendar.YEAR);
        binding.etDateInvoice.setText(day_ + "/" + (month_ + 1) + "/" + year_);

        binding.etDateInvoice.setOnFocusChangeListener((v, hasFocus)->{
            if (hasFocus){
                DatePickerDialog picker;
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                binding.etDateInvoice.setText(date);
                            }
                        }, year, month, day);
                picker.show();
            }

        });

        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        //binding.calDate.setDate();

        return binding.getRoot();
    }

    public void createAndSendEntity() {
        InvoiceEntity invoiceEntity = new InvoiceEntity();

        //convert date format
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d = dateFormat.parse(binding.etDateInvoice.getText().toString());
            long milliseconds = d.getTime();
            invoiceEntity.setDate(milliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        invoiceEntity.setSeller(binding.etSeller.getText().toString());
        invoiceEntity.setDescription(binding.etDescription.getText().toString());
        invoiceEntity.setProjectID(1);
        invoiceRepository.insert(invoiceEntity).subscribeOn(Schedulers.io()).blockingGet();
        listener.invoiceEntityWasInserted();
        dismiss();
    }


    public interface Listener {
        void invoiceEntityWasInserted();
    }
}
