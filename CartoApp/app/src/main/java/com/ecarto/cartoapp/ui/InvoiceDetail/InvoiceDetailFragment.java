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

public class InvoiceDetailFragment extends Fragment
        implements InvoiceDetailAdapter.Listener, InsertInvoiceDetailDialog.Listener, InvoiceDetailOptionsDialog.Listener {

    public static final String TAG = "INVOICE_DETAIL_FRAGMENT_TAG";
    static Integer CURRENT_SELECTION = Selector.NONE_SELECTED;

    InvoiceDetailFragmentBinding binding;
    private InvoiceDetailFragment.Listener listener;
    private InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    Integer invoiceEntityID;

    public InvoiceDetailFragment() {
    }

    public static InvoiceDetailFragment newInstance(String tagParent) {
        Bundle args = new Bundle();
        args.putString(NAVIGATION.TAG_PARENT, tagParent);
        InvoiceDetailFragment fragment = new InvoiceDetailFragment();
        fragment.setArguments(args);
        return fragment;
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
        MainActivity.navigation = NAVIGATION.INVOICE_DETAIL_LISTING;
        listener = ActivityUtils.getListener(this);
        sharedPreferences =  getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
    }

    private void getDatabaseData(){
        invoiceEntityID = sharedPreferences.getInt(getString(R.string.selectedInvoiceEntityID), 0);

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
        InvoiceDetailAdapter invoiceAdapter = new InvoiceDetailAdapter(adapterList, this);

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
            InsertInvoiceDetailDialog insertInvoiceDetailDialog = InsertInvoiceDetailDialog.newInstance(TAG, null);
            insertInvoiceDetailDialog.show(getActivity().getSupportFragmentManager(), InsertInvoiceDetailDialog.TAG);
        });
    }

    //COMMUNICATION
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
        InvoiceDetailOptionsDialog invoiceDetailOptionsDialog = InvoiceDetailOptionsDialog.newInstance(TAG);
        invoiceDetailOptionsDialog.show(getActivity().getSupportFragmentManager(), InvoiceDetailOptionsDialog.TAG);
    }

    @Override
    public void refreshInvoiceDetailList() {
        ActivityUtils.showSnackbar(binding.getRoot() , "Borrado correctamente", R.color.green);
        getDatabaseData();
    }

    public interface Listener {

    }
}
