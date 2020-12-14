package com.example.cartoapp.ui.InvoiceDetail;

import android.app.Activity;
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
import com.example.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.example.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.InvoiceDetailFragmentBinding;
import com.example.cartoapp.ui.InsertFragments.InsertInvoiceDetailDialog;
import com.example.cartoapp.ui.Invoice.InvoiceAdapter;
import com.example.cartoapp.ui.MainActivity;
import com.example.cartoapp.utils.NAVIGATION;
import com.example.cartoapp.utils.Selector;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class InvoiceDetailFragment extends Fragment implements InvoiceDetailAdapter.Listener, InsertInvoiceDetailDialog.Listener {
    InvoiceDetailFragmentBinding binding;
    public static String INVOICE_ENTITY = "INVOICE_ENTITY";
    private InvoiceDetailFragment.Listener listener;
    private InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    Integer invoiceEntityID;
    static Integer CURRENT_SELECTION = Selector.NONE_SELECTED;

    public InvoiceDetailFragment(Object context) {
        if (context instanceof InvoiceDetailFragment.Listener) {
            listener = (InvoiceDetailFragment.Listener) context;
        }
    }

    public InvoiceDetailFragment() {
    }

    public static InvoiceDetailFragment newInstance(Object context) {
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

        binding.tbAddInvoice.setOnClickListener((v) -> {
            InsertInvoiceDetailDialog insertInvoiceDetailDialog = InsertInvoiceDetailDialog.newInstance(this, null);
            insertInvoiceDetailDialog.show(getActivity().getSupportFragmentManager(), "InsertInvoiceDetailDialog");
        });

        return binding.getRoot();
    }


    private void getDatabaseData(){
        invoiceEntityID = sharedPreferences.getInt(getString(R.string.selectedInvoiceEntityID), 0);

        if (invoiceEntityID != 0){
            List<ExtendedInvoiceDetailEntity> invoiceDetailEntityList = invoiceRepository.findAllExtendedInvoiceDetailBy(null, invoiceEntityID).subscribeOn(Schedulers.io()).blockingGet();
            setAdapterToRecyclerView(invoiceDetailEntityList);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }


    private void setAdapterToRecyclerView(List<ExtendedInvoiceDetailEntity> adapterList) {
        List<ExtendedInvoiceEntity> invoiceHeaderList = invoiceRepository.findAllExtendedInvoiceBy(invoiceEntityID).subscribeOn(Schedulers.io()).blockingGet();

        InvoiceAdapter invoiceAdapterHeader = new InvoiceAdapter(invoiceHeaderList, this);
        InvoiceDetailAdapter invoiceAdapter = new InvoiceDetailAdapter(adapterList, this);

        if (adapterList.isEmpty()){
            binding.txtNoInvoicesDetail.setVisibility(View.VISIBLE);
        } else {
            binding.txtNoInvoicesDetail.setVisibility(View.INVISIBLE);
        }

        binding.invoiceDetailRecyclerView.setAdapter(invoiceAdapter);
        binding.invoiceDetailRecyclerView.setHasFixedSize(true);

        binding.invoiceHeader.setAdapter(invoiceAdapterHeader);
        binding.invoiceHeader.setHasFixedSize(true);
    }

    @Override
    public void deleteInvoiceDetail(InvoiceDetailEntity invoiceDetailEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(true);
        builder.setTitle("Borrar detalle de factura de " + invoiceDetailEntity.getConceptDescription() );
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

    @Override
    public void invoiceDetailWasJustAdded() {
        getDatabaseData();
    }

    @Override
    public void checkFileList(InvoiceDetailEntity invoiceDetailEntity) {
        //TODO: hacer esto
    }

    @Override
    public void editInvoiceDetail(InvoiceDetailEntity invoiceDetailEntity) {
        InsertInvoiceDetailDialog insertInvoiceDetailDialog = InsertInvoiceDetailDialog.newInstance(this, invoiceDetailEntity);
        insertInvoiceDetailDialog.show(getActivity().getSupportFragmentManager(), "InsertInvoiceDetailDialog");
    }

    @Override
    public Integer getCurrentSelection() {
        return CURRENT_SELECTION;
    }

    @Override
    public void setCurrentSelection(Integer position) {
        CURRENT_SELECTION = position;
    }

    public interface Listener {

    }
}
