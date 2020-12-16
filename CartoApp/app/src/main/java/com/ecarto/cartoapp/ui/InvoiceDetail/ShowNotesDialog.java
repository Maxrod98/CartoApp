package com.ecarto.cartoapp.ui.InvoiceDetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    //TODO: agregar boton de guardado

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

        initElems();
        initListeners();

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void saveNotes(){
        if (notesDetailEntity != null) {
            notesDetailEntity.setNotes(binding.etShowNotes.getText().toString());
            invoiceRepository.insert(notesDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
        } else {
            Toast.makeText(getContext(), "Hubo un error al guardar las notas", Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void initListeners(){
        binding.imgCloseNotes.setOnClickListener((v)->{
            saveNotes();
        });
    }

    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());

        if (getArguments() != null){
            notesDetailEntity = (InvoiceDetailEntity) getArguments().getSerializable(NOTES);
            binding.etShowNotes.setText(notesDetailEntity.getNotes());
        } else {
            Toast.makeText(getContext(), "Hubo un error al guardar las notas", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveNotes();
    }
}
