package com.example.cartoapp.ui.InvoiceMain;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.FragmentInvoiceBinding;
import com.example.cartoapp.ui.InsertFragments.InsertInvoiceDialog;
import com.example.cartoapp.ui.MainActivity;
import com.example.cartoapp.utils.NAVIGATION;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class InvoiceFragment extends Fragment implements InvoiceAdapter.Listener, InsertInvoiceDialog.Listener {
    FragmentInvoiceBinding binding;
    InvoiceRepository invoiceRepository;
    InvoiceFragment.Listener listener;
    SharedPreferences sharedPreferences;

    public InvoiceFragment() {
    }

    public InvoiceFragment(Object context) {
        if (context instanceof InvoiceFragment.Listener) {
            listener = (InvoiceFragment.Listener) context;
        }
    }

    public static InvoiceFragment newInstance(Object context) {
        Bundle args = new Bundle();

        InvoiceFragment fragment = new InvoiceFragment(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.navigation = NAVIGATION.INVOICE_LISTING;
        binding = FragmentInvoiceBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);

        binding.tbAddInvoice.setOnClickListener((v) -> {
            InsertInvoiceDialog insertInvoiceEntityDialog = InsertInvoiceDialog.newInstance(this);
            insertInvoiceEntityDialog.show(getActivity().getSupportFragmentManager(), "InsertInvoiceEntityDialog");
        });

        getDatabaseData();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDatabaseData();
    }

    private void getDatabaseData() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        setAdapterToRecyclerView(invoiceRepository.findAllExtendedInvoiceBy(null).subscribeOn(Schedulers.io()).blockingGet());
    }

    private void setAdapterToRecyclerView(List<ExtendedInvoiceEntity> adapterList) {
        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(adapterList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.invoiceListing.setLayoutManager(layoutManager);

        binding.invoiceListing.setAdapter(invoiceAdapter);
        binding.invoiceListing.setHasFixedSize(true);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void goToInvoiceDetail(InvoiceEntity invoiceEntity) {
        sharedPreferences.edit().putInt(getString(R.string.selectedInvoiceEntityID), invoiceEntity.getInvoiceID()).commit();
        listener.goToInvoiceDetails();
    }

    @Override
    public void deleteInvoice(InvoiceEntity invoiceEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(true);
        builder.setTitle("Borrar  factura de " + invoiceEntity.getDescription());
        builder.setMessage("¿Seguro que desea borrar la factura?");
        builder.setPositiveButton("Borrar", (dialog, which) -> {
            deleteInvoiceEntityAndRelatedInvoiceDetail(invoiceEntity);
            getDatabaseData();
        });
        builder.setNegativeButton("Cancelar", ((dialog, which) -> {
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteInvoiceEntityAndRelatedInvoiceDetail(InvoiceEntity invoiceEntity){
        invoiceRepository.deleteInvoiceEntity(invoiceEntity).subscribeOn(Schedulers.io()).blockingGet();
    }

    @Override
    public void invoiceEntityWasInserted() {
        InvoiceEntity invoiceEntity = invoiceRepository.findLastInvoiceEntity().subscribeOn(Schedulers.io()).blockingGet();
        sharedPreferences.edit().putInt(getString(R.string.selectedInvoiceEntityID), invoiceEntity.getInvoiceID()).commit();
        listener.goToInvoiceDetails();
    }

    public interface Listener {
        void goToInvoiceDetails();
    }
}