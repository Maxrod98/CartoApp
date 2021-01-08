package com.ecarto.cartoapp.ui.InvoiceDetail;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.ViewModels.MainActivityViewModel;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.FileEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Repositories.FileRepository;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.InvoiceDetailFragmentBinding;
import com.ecarto.cartoapp.utils.Selector;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class InvoiceDetailF extends Fragment implements InvoiceDetailA.Listener {

    public static final String TAG = "INVOICE_DETAIL_FRAGMENT_TAG";
    private static final String SELECTED_INVOICE_ID = "SelectedInvoiceID";

    InvoiceDetailFragmentBinding binding;
    InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    MainActivityViewModel filesSelectedViewModel;
    FileRepository fileRepository;

    boolean invoiceExists;
    Long invoiceEntityID;
    List<ExtendedInvoiceDetailEntity> invoiceDetailEntityList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = InvoiceDetailFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initElems();
        getDatabaseData();
        initListeners();
    }

    private void initElems() {
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        fileRepository = new FileRepository(getActivity().getApplication());
    }

    private void getDatabaseData() {
        invoiceEntityID = getArguments().getLong(SELECTED_INVOICE_ID);

        invoiceExists = invoiceEntityID != 0;
        if (invoiceExists) {
            invoiceDetailEntityList = invoiceRepository
                    .findAllExtendedInvoiceDetailBy(null, invoiceEntityID)
                    .subscribeOn(Schedulers.io()).blockingGet();

            setRecyclerView(invoiceDetailEntityList);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecyclerView(List<ExtendedInvoiceDetailEntity> adapterList) {
        InvoiceDetailA invoiceAdapter = new InvoiceDetailA(adapterList, this);

        if (adapterList.isEmpty()) {
            binding.txtNoInvoicesDetail.setVisibility(View.VISIBLE);
        } else {
            binding.txtNoInvoicesDetail.setVisibility(View.INVISIBLE);
        }

        binding.invoiceDetailRecyclerView.setAdapter(invoiceAdapter);
        binding.invoiceDetailRecyclerView.setHasFixedSize(true);
    }

    private void initListeners() {
        binding.tbAddInvoice.setOnClickListener((v) -> {
            NavHostFragment.findNavController(this)
                    .navigate(InvoiceDetailFDirections.actionInvoiceDetailFragmentToInsertInvoiceDetailDialog(0, invoiceEntityID));
        });

        filesSelectedViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        filesSelectedViewModel.getFilesSelected().observe(getViewLifecycleOwner(), fileEntities -> {
            if (fileEntities == null) return;

            Integer selectedPos = getArguments().getInt(Selector.SELECTED_POSITION, Selector.NONE_SELECTED);
            if (fileEntities.isEmpty()) {
                Snackbar.make(binding.getRoot(), "No ha seleccionado un archivo", Snackbar.LENGTH_LONG).show();
            } else if (selectedPos == Selector.NONE_SELECTED || selectedPos >= invoiceDetailEntityList.size()) {
                Snackbar.make(binding.getRoot(), "Seleccione un detalle de factura primero", Snackbar.LENGTH_LONG).show();
            } else { //add it to the files
                for (FileEntity file : fileEntities) {
                    file.setInvoiceDetailID(invoiceDetailEntityList.get(selectedPos).getInvoiceDetailID());
                    fileRepository.insertFileEntity(file).subscribeOn(Schedulers.io()).blockingGet();
                }
                getDatabaseData();
            }

            filesSelectedViewModel.setFilesSelected(null);
        });
    }

    @Override
    public void onInvoiceDetailClick(InvoiceDetailEntity invoiceDetailEntity) {
        NavHostFragment.findNavController(this)
                .navigate(InvoiceDetailFDirections.actionInvoiceDetailFragmentToInvoiceDetailOptionsDialog(invoiceDetailEntity.getInvoiceDetailID()));
    }

    public interface Listener {
    }
}
