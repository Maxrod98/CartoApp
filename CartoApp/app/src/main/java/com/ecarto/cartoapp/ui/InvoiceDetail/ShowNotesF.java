package com.ecarto.cartoapp.ui.InvoiceDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.DialogShowNotesBinding;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.schedulers.Schedulers;

public class ShowNotesF extends Fragment {
    public static String SelectedInvoiceDetailID = "SelectedInvoiceDetailID";
    public static String TAG = "DIALOG_FRAGMENT_TAG";

    DialogShowNotesBinding binding;
    InvoiceRepository invoiceRepository;
    InvoiceDetailEntity notesDetailEntity;
    Long invoiceDetailID;
    //TODO: agregar boton de guardado

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogShowNotesBinding.inflate(inflater, container, false);
        initElems();
        initListeners();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());

        if (getArguments() != null){
            invoiceDetailID = getArguments().getLong(SelectedInvoiceDetailID);
            notesDetailEntity = getNotesDetailEntity();
            binding.etShowNotes.setText(notesDetailEntity.getNotes());
        } else {
            Toast.makeText(getContext(), "Hubo un error al guardar las notas", Toast.LENGTH_SHORT).show();
        }
    }

    private void initListeners(){
        binding.imgCloseNotes.setOnClickListener((v)->{
            if (notesDetailEntity.getNotes() != null){
                if (notesDetailEntity.getNotes().compareTo(binding.etShowNotes.getText().toString())!= 0){
                    Snackbar.make(binding.getRoot(), "Las notas no fueron guardadas", Snackbar.LENGTH_LONG).show();
                }
            }

            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.btnSaveNotes.setOnClickListener((v) -> {
            Snackbar.make(binding.getRoot(), "Notas guardadas correctamente", Snackbar.LENGTH_LONG).show();
            saveNotes();
        });
    }

    private InvoiceDetailEntity getNotesDetailEntity(){
        return invoiceRepository.findAllInvoiceDetailBy(invoiceDetailID, null)
                .subscribeOn(Schedulers.io()).blockingGet()
                .stream().findFirst().orElse(null);
    }

    public void saveNotes(){
        if (notesDetailEntity != null) {
            notesDetailEntity = getNotesDetailEntity(); //update like this to avoid incidents with upload queue, which may be asynchronous
            notesDetailEntity.setNotes(binding.etShowNotes.getText().toString());
            invoiceRepository.updateInvoiceDetailEntity(notesDetailEntity).subscribeOn(Schedulers.io()).blockingGet(); //TODO: fileEntities being deleted when note is edited
        } else {
            Toast.makeText(getContext(), "Hubo un error al guardar las notas", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
