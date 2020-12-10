package com.example.cartoapp.ui.InvoiceMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.database.Repositories.InvoiceDetailRepository;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class InvoiceListingFragment extends Fragment implements InvoiceAdapter.Listener{
    FragmentFirstBinding binding;
    InvoiceRepository invoiceRepository;
    InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);

        displayList();
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void displayList() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        invoiceDetailRepository = new InvoiceDetailRepository(getActivity().getApplication());

        //binding.invoiceListing.setAdapter();

        /*
        for (int i = 0; i < 10; i++){
            InvoiceDetailEntity test = new InvoiceDetailEntity();
            test.setTotalCostOfItem(i * 10);
            test.setInvoiceID(i);
            test.setProduct("Test " + String.valueOf(i));
            test.setQuantity(3 * i);
            invoiceDetailRepository.insert(test);
        }
         */

        setAdapterToRecyclerView(invoiceRepository.findAllExtendedInvoiceBy(null).subscribeOn(Schedulers.io()).blockingGet());

        //invoiceRepository.insert(test);
    }

    private void setAdapterToRecyclerView(List<ExtendedInvoiceEntity> adapterList){
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
    public void navigateToInvoiceDetail() {

    }

    public interface Listener {

    }
}