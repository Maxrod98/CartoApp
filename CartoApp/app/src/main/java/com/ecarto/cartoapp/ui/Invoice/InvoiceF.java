package com.ecarto.cartoapp.ui.Invoice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.FragmentInvoiceBinding;
import com.ecarto.cartoapp.ui.MainActivity;
import com.ecarto.cartoapp.utils.NAVIGATION;
import com.ecarto.cartoapp.utils.Selector;
import com.ecarto.cartoapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class InvoiceF extends Fragment
        implements InvoiceA.Listener, InsertInvoiceF.Listener, InvoiceOptionsF.Listener {
    public static final String TAG = "INVOICE_FRAGMENT_TAG";
    static Integer CURRENT_SELECTION = Selector.NONE_SELECTED;

    FragmentInvoiceBinding binding;
    InvoiceRepository invoiceRepository;
    InvoiceF.Listener listener;
    SharedPreferences sharedPreferences;
    InvoiceA invoiceAdapter;
    List<ExtendedInvoiceEntity> invoiceEntities;

    public InvoiceF() {
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
        //listener = ActivityUtils.getListener(this);
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
        invoiceAdapter = new InvoiceA(adapterList, this);

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
            NavController navCo = NavHostFragment.findNavController(this);
            NavDirections action = InvoiceFDirections.actionInvoiceFragmentToInsertInvoiceDialog2(0);
            navCo.navigate(action);
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