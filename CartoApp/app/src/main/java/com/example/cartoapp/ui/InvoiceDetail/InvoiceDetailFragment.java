package com.example.cartoapp.ui.InvoiceDetail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.InvoiceDetailFragmentBinding;
import com.example.cartoapp.ui.MainActivity;
import com.example.cartoapp.utils.NAVIGATION;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class InvoiceDetailFragment extends Fragment implements InvoiceDetailAdapter.Listener {
    InvoiceDetailFragmentBinding binding;
    public static String INVOICE_ENTITY = "INVOICE_ENTITY";
    private InvoiceDetailFragment.Listener listener;
    private InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;

    public InvoiceDetailFragment(Context context) {
        if (context instanceof InvoiceDetailFragment.Listener) {
            listener = (InvoiceDetailFragment.Listener) context;
        }
    }

    public InvoiceDetailFragment() {
    }

    public static InvoiceDetailFragment newInstance(Context context) {
        Bundle args = new Bundle();

        InvoiceDetailFragment fragment = new InvoiceDetailFragment(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences =  getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.navigation = NAVIGATION.INVOICE_DETAIL_LISTING;
        binding = InvoiceDetailFragmentBinding.inflate(inflater, container, false);

        getDatabaseData();

        return binding.getRoot();
    }


    private void getDatabaseData(){
        Integer invoiceEntityID = sharedPreferences.getInt(getString(R.string.selectedInvoiceEntityID), 0);

        if (invoiceEntityID != 0){
            List<InvoiceDetailEntity> invoiceDetailEntityList = invoiceRepository.findAllInvoiceDetailBy(null, invoiceEntityID).subscribeOn(Schedulers.io()).blockingGet();
            setAdapterToRecyclerView(invoiceDetailEntityList);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }


    private void setAdapterToRecyclerView(List<InvoiceDetailEntity> adapterList) {
        InvoiceDetailAdapter invoiceAdapter = new InvoiceDetailAdapter(adapterList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.invoiceDetailRecyclerView.setLayoutManager(layoutManager);

        binding.invoiceDetailRecyclerView.setAdapter(invoiceAdapter);
        binding.invoiceDetailRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void deleteInvoiceDetail(InvoiceDetailEntity invoiceDetailEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(true);
        builder.setTitle("Borrar detalle de factura de " + invoiceDetailEntity.getProduct() );
        builder.setMessage("¿Seguro que desea borrar el detalle de la factura?");
        builder.setPositiveButton("Borrar", (dialog, which) -> {
            deleteDetailEntity(invoiceDetailEntity);
        });
        builder.setNegativeButton("Cancelar", ((dialog, which) -> { }));

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void deleteDetailEntity(InvoiceDetailEntity invoiceDetailEntity) {
        invoiceRepository.deleteInvoiceDetailEntity(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
        getDatabaseData();
    }

    public interface Listener {

    }
}
