package com.ecarto.cartoapp.ui.MailInvoices;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.FragmentMailInvoicesBinding;
import com.ecarto.cartoapp.ui.Invoice.InvoiceA;
import com.ecarto.cartoapp.utils.Selector;
import com.ecarto.cartoapp.web.DTOs.MailInvoiceDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MailInvoicesF extends Fragment implements InvoiceA.Listener {
    FragmentMailInvoicesBinding binding;
    InvoiceRepository invoiceRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMailInvoicesBinding.inflate(inflater, container, false);
        invoiceRepository = new InvoiceRepository(getContext());


        getMailData();
        return binding.getRoot();
    }

    private void getMailData() {
        binding.progressBarLoading.getRoot().setVisibility(View.VISIBLE);

        invoiceRepository.getMailData(new Callback<MailInvoiceDTO[]>() {
            @Override
            public void onResponse(Call<MailInvoiceDTO[]> call, Response<MailInvoiceDTO[]> response) {
                if (response.isSuccessful()){
                    List<ExtendedInvoiceEntity> elems = new ArrayList<>();
                    for (MailInvoiceDTO mailInvoiceDTO : response.body()){
                        elems.add(mailInvoiceDTO.toExtendedInvoiceEntity());
                    }
                    InvoiceA invoiceA = new InvoiceA(elems, MailInvoicesF.this);
                    binding.recyclerViewMail.setAdapter(invoiceA);
                    binding.recyclerViewMail.setHasFixedSize(true);
                } else {

                }
                binding.progressBarLoading.getRoot().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<MailInvoiceDTO[]> call, Throwable t) {
                binding.progressBarLoading.getRoot().setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void onInvoiceClick(ExtendedInvoiceEntity extendedInvoiceEntity) {

    }

    @Override
    public void onInvoiceLongClick(ExtendedInvoiceEntity extendedInvoiceEntity) {

    }
}
