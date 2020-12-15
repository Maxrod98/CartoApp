package com.example.cartoapp.ui.ShowNotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.DialogShowNotesBinding;

import io.reactivex.schedulers.Schedulers;

public class ShowNotesDialog extends DialogFragment {
    DialogShowNotesBinding binding;
    public static String NOTES = "NOTES";
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
