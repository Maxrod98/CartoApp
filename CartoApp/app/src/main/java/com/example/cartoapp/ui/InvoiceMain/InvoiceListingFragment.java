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

import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;

public class InvoiceListingFragment extends Fragment implements InvoiceAdapter.Listener{
    FragmentFirstBinding binding;
    InvoiceRepository invoiceRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);

        displayList();
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void displayList() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        //binding.invoiceListing.setAdapter();

        List<InvoiceEntity> testList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            InvoiceEntity test = new InvoiceEntity();
            test.setDescription("Second asnd i masidasdnuasj isjdis " + String.valueOf(i));
            test.setTotalCost(1000 * i);
            test.setDate(34234234);
            test.setSeller("Gilsa");
            testList.add(test);
        }

        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(testList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.invoiceListing.setLayoutManager(layoutManager);

        binding.invoiceListing.setAdapter(invoiceAdapter);
        binding.invoiceListing.setHasFixedSize(true);

        //invoiceRepository.insert(test);
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