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

import io.reactivex.schedulers.Schedulers;

public class ShowNotesF extends Fragment {
    public static String SelectedInvoiceDetailID = "SelectedInvoiceDetailID";
    public static String TAG = "DIALOG_FRAGMENT_TAG";

    DialogShowNotesBinding binding;
    InvoiceRepository invoiceRepository;
    InvoiceDetailEntity notesDetailEntity;
    Integer invoiceDetailID;
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
            invoiceDetailID = getArguments().getInt(SelectedInvoiceDetailID);
            notesDetailEntity = invoiceRepository.findAllInvoiceDetailBy(invoiceDetailID, null)
                    .subscribeOn(Schedulers.io()).blockingGet()
                    .stream().findFirst().orElse(null);

            binding.etShowNotes.setText(notesDetailEntity.getNotes());
        } else {
            Toast.makeText(getContext(), "Hubo un error al guardar las notas", Toast.LENGTH_SHORT).show();
        }
    }

    private void initListeners(){
        binding.imgCloseNotes.setOnClickListener((v)->{
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    public void saveNotes(){
        if (notesDetailEntity != null) {
            notesDetailEntity.setNotes(binding.etShowNotes.getText().toString());
            invoiceRepository.insert(notesDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
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
