package com.ecarto.cartoapp.ui.MailInvoices;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.FragmentMailInvoicesBinding;
import com.ecarto.cartoapp.web.DTOs.MailInvoiceDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MailInvoicesF extends Fragment {
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
        invoiceRepository.getMailData(new Callback<MailInvoiceDTO[]>() {
            @Override
            public void onResponse(Call<MailInvoiceDTO[]> call, Response<MailInvoiceDTO[]> response) {
                if (response.isSuccessful()){
                    for (MailInvoiceDTO mailInvoiceDTO : response.body()){
                        Log.d("MAIL", mailInvoiceDTO.getReceptor());
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<MailInvoiceDTO[]> call, Throwable t) {

            }
        });
    }


}
