package com.ecarto.cartoapp.ui.ShowNotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogShowNotesBinding;

import io.reactivex.schedulers.Schedulers;

public class ShowNotesDialog extends DialogFragment {
    public static String NOTES = "NOTES";
    public static String TAG = "DIALOG_FRAGMENT_TAG";

    DialogShowNotesBinding binding;
    InvoiceRepository invoiceRepository;
    InvoiceDetailEntity notesDetailEntity;

    public static ShowNotesDialog newInstance(InvoiceDetailEntity invoiceDetailEntity) {

        Bundle args = new Bundle();
        args.putSerializable(NOTES, invoiceDetailEntity);
        ShowNotesDialog fragment = new ShowNotesDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogShowNotesBinding.inflate(inflater, container, false);

        invoiceRepository = new InvoiceRepository(getActivity().getApplication());

        if (getArguments() != null){
            notesDetailEntity = (InvoiceDetailEntity) getArguments().getSerializable(NOTES);
            binding.etShowNotes.setText(notesDetailEntity.getNotes());
        }

        binding.imgCloseNotes.setOnClickListener((v)->{
            saveNotes();
        });


        return binding.getRoot();
    }

    public void saveNotes(){
        if (notesDetailEntity != null) {
            notesDetailEntity.setNotes(binding.etShowNotes.getText().toString());
            invoiceRepository.insert(notesDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
        }
        dismiss();
    }

    @Override
    public void onDestroy() {
        saveNotes();
        super.onDestroy();
    }
}
