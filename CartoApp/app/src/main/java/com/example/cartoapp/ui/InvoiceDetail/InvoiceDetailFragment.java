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
import androidx.fragment.app.Fragment;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.InvoiceDetailFragmentBinding;
import com.example.cartoapp.ui.InsertFragments.InsertInvoiceDetailDialog;
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
        InvoiceDetailAdapter invoiceAdapter = new InvoiceDetailAdapter(adapterList, this);

        if (adapterList.isEmpty()){
            binding.txtNoInvoicesDetail.setVisibility(View.VISIBLE);
        } else {
            binding.txtNoInvoicesDetail.setVisibility(View.INVISIBLE);
        }

        binding.invoiceDetailRecyclerView.setAdapter(invoiceAdapter);
        binding.invoiceDetailRecyclerView.setHasFixedSize(true);

    }


    @Override
    public void invoiceDetailWasJustAdded() {
        getDatabaseData();
    }



    @Override
    public Integer getCurrentSelection() {
        return CURRENT_SELECTION;
    }

    @Override
    public void setCurrentSelection(Integer position) {
        CURRENT_SELECTION = position;
    }

    @Override
    public void goToInvoiceDetailOptionsDialog(InvoiceDetailEntity invoiceDetailEntity) {
        sharedPreferences.edit().putInt(getString(R.string.selectedInvoiceDetailID), invoiceDetailEntity.getInvoiceDetailID()).commit();
        InvoiceDetailOptionsDialog invoiceDetailOptionsDialog = InvoiceDetailOptionsDialog.newInstance(this);
        invoiceDetailOptionsDialog.show(getActivity().getSupportFragmentManager(), "OptionsDetailDialog");
    }


    public interface Listener {

    }
}
