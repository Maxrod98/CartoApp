package com.ecarto.cartoapp.ui.Invoice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.FragmentInvoiceBinding;
import com.ecarto.cartoapp.ui.MainActivity;
import com.ecarto.cartoapp.utils.ActivityUtils;
import com.ecarto.cartoapp.utils.NAVIGATION;
import com.ecarto.cartoapp.utils.Selector;
import com.ecarto.cartoapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class InvoiceFragment extends Fragment
        implements InvoiceAdapter.Listener, InsertInvoiceDialog.Listener, InvoiceOptionsDialog.Listener {
    public static final String TAG = "INVOICE_FRAGMENT_TAG";
    static Integer CURRENT_SELECTION = Selector.NONE_SELECTED;

    FragmentInvoiceBinding binding;
    InvoiceRepository invoiceRepository;
    InvoiceFragment.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceAdapter invoiceAdapter;
    List<ExtendedInvoiceEntity> invoiceEntities;

    public InvoiceFragment() {
    }

    public static InvoiceFragment newInstance(String tagParent) {
        Bundle args = new Bundle();
        args.putString(NAVIGATION.TAG_PARENT, tagParent);
        InvoiceFragment fragment = new InvoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInvoiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        initElems();
        getDatabaseData();
        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDatabaseData();
    }

    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        listener = ActivityUtils.getListener(this);
        MainActivity.navigation = NAVIGATION.INVOICE_LISTING;
    }

    private List<ExtendedInvoiceEntity> getAllInvoiceEntities(){
        return invoiceRepository.findAllExtendedInvoiceBy(null).subscribeOn(Schedulers.io()).blockingGet();
    }

    private void getDatabaseData() {
        invoiceEntities = getAllInvoiceEntities();
        setRecyclerView(invoiceEntities);

    }

    private void setRecyclerView(List<ExtendedInvoiceEntity> adapterList) {
        invoiceAdapter = new InvoiceAdapter(adapterList, this);

        binding.txtTotal.setText(StringUtils.formatMoney(sumAllInvoices(adapterList)));

        if (adapterList.isEmpty()) {
            binding.txtNoInvoices.setVisibility(View.VISIBLE);
        } else {
            binding.txtNoInvoices.setVisibility(View.INVISIBLE);
        }

        binding.invoiceListing.setAdapter(invoiceAdapter);
        binding.invoiceListing.setHasFixedSize(true);
    }

    private void initListeners() {
        binding.tbAddInvoice.setOnClickListener((v) -> {
            InsertInvoiceDialog insertInvoiceEntityDialog = InsertInvoiceDialog.newInstance(TAG, null);
            insertInvoiceEntityDialog.show(getActivity().getSupportFragmentManager(), InsertInvoiceDialog.TAG);
        });

        binding.etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!binding.etSearchBar.getText().toString().isEmpty()) {
                    filterInvoices();
                    CURRENT_SELECTION = Selector.NONE_SELECTED;
                } else {
                    setRecyclerView(getAllInvoiceEntities());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public Integer sumAllInvoices(List<ExtendedInvoiceEntity> extendedInvoiceEntities){
        Integer sum = 0;

        for (ExtendedInvoiceEntity item : extendedInvoiceEntities){
            if (item.getTotalCost() == null) item.setTotalCost(0);
            sum += item.getTotalCost();
        }
        return sum;
    }

    public void filterInvoices(){
        String filterStr = binding.etSearchBar.getText().toString();
        List<ExtendedInvoiceEntity> list = getAllInvoiceEntities();
        List<ExtendedInvoiceEntity> filteredList = new ArrayList<>();

        for (ExtendedInvoiceEntity item : list){
            if (item.toString().toLowerCase().contains(filterStr.toLowerCase())){
                filteredList.add(item);
            }
        }

        setRecyclerView(filteredList);
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
    public void goToInvoiceDetail(InvoiceEntity invoiceEntity) {
        sharedPreferences.edit().putInt(getString(R.string.selectedInvoiceEntityID), invoiceEntity.getInvoiceID()).commit();
        listener.goToInvoiceDetails();
    }

    @Override
    public void goToInvoiceOptions(ExtendedInvoiceEntity invoiceEntity) {
        InvoiceOptionsDialog invoiceOptionsDialog = InvoiceOptionsDialog.newInstance(TAG, invoiceEntity);
        invoiceOptionsDialog.show(getActivity().getSupportFragmentManager(), InvoiceOptionsDialog.TAG);
    }

    @Override
    public void invoiceEntityWasInserted() {
        InvoiceEntity invoiceEntity = invoiceRepository.findLastInvoiceEntity().subscribeOn(Schedulers.io()).blockingGet();
        sharedPreferences.edit().putInt(getString(R.string.selectedInvoiceEntityID), invoiceEntity.getInvoiceID()).commit();
        CURRENT_SELECTION = Selector.LAST_SELECTED;
        listener.goToInvoiceDetails();
    }

    @Override
    public void invoiceEntityWasEdited() {
        getDatabaseData();
    }

    @Override
    public void updateList() {
        CURRENT_SELECTION = Selector.NONE_SELECTED;
        getDatabaseData();
    }


    public interface Listener {
        void goToInvoiceDetails();
    }
}