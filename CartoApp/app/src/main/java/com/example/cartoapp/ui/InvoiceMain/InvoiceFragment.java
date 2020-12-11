package com.example.cartoapp.ui.InvoiceMain;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.FragmentFirstBinding;
import com.example.cartoapp.ui.MainActivity;
import com.example.cartoapp.utils.NAVIGATION;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class InvoiceFragment extends Fragment implements InvoiceAdapter.Listener{
    FragmentFirstBinding binding;
    InvoiceRepository invoiceRepository;
    InvoiceFragment.Listener listener;
    SharedPreferences sharedPreferences;

    public InvoiceFragment() {
    }

    public InvoiceFragment(Context context) {
        if (context instanceof InvoiceFragment.Listener){
            listener = (InvoiceFragment.Listener) context;
        }
    }

    public static InvoiceFragment newInstance(Context context) {

        Bundle args = new Bundle();

        InvoiceFragment fragment = new InvoiceFragment(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.navigation = NAVIGATION.INVOICE_LISTING;
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);


        displayList();
        return binding.getRoot();
    }

    private void displayList() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());

        setAdapterToRecyclerView(invoiceRepository.findAllExtendedInvoiceBy(null).subscribeOn(Schedulers.io()).blockingGet());
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

    @Override
    public void goToInvoiceDetail(InvoiceEntity invoiceEntity) {
        sharedPreferences.edit().putInt(getString(R.string.selectedInvoiceEntityID), invoiceEntity.getInvoiceID()).commit();
        listener.goToInvoiceDetailFragmentToActivity(invoiceEntity);
    }

    public interface Listener {
        void goToInvoiceDetailFragmentToActivity(InvoiceEntity invoiceEntity);
    }
}