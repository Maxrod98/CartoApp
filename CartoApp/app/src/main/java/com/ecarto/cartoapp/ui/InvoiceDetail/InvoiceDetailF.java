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
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.InvoiceDetailFragmentBinding;
import com.ecarto.cartoapp.ui.MainActivity;
import com.ecarto.cartoapp.utils.ActivityUtils;
import com.ecarto.cartoapp.utils.NAVIGATION;
import com.ecarto.cartoapp.utils.Selector;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class InvoiceDetailF extends Fragment implements InvoiceDetailA.Listener {

    public static final String TAG = "INVOICE_DETAIL_FRAGMENT_TAG";
    private static final String SELECTED_INVOICE_ID = "SelectedInvoiceID";

    InvoiceDetailFragmentBinding binding;
    private InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    Integer invoiceEntityID;

    public InvoiceDetailF() {
    }

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
        sharedPreferences =  getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
    }

    private void getDatabaseData(){
        invoiceEntityID = getArguments().getInt(SELECTED_INVOICE_ID);

        if (invoiceEntityID != 0){
            List<ExtendedInvoiceDetailEntity> invoiceDetailEntityList = invoiceRepository
                    .findAllExtendedInvoiceDetailBy(null, invoiceEntityID)
                    .subscribeOn(Schedulers.io()).blockingGet();

            setRecyclerView(invoiceDetailEntityList);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecyclerView(List<ExtendedInvoiceDetailEntity> adapterList) {
        InvoiceDetailA invoiceAdapter = new InvoiceDetailA(adapterList, this);

        if (adapterList.isEmpty()){
            binding.txtNoInvoicesDetail.setVisibility(View.VISIBLE);
        } else {
            binding.txtNoInvoicesDetail.setVisibility(View.INVISIBLE);
        }

        binding.invoiceDetailRecyclerView.setAdapter(invoiceAdapter);
        binding.invoiceDetailRecyclerView.setHasFixedSize(true);
    }

    private void initListeners() {
        binding.tbAddInvoice.setOnClickListener((v) -> {
            NavController navCo = NavHostFragment.findNavController(this);
            NavDirections action = InvoiceDetailFDirections.actionInvoiceDetailFragmentToInsertInvoiceDetailDialog(0, invoiceEntityID);
            navCo.navigate(action);
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
