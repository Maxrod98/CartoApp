package com.ecarto.cartoapp.ui.Invoice;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentProvider;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.database.Repositories.ProjectRepository;
import com.ecarto.cartoapp.databinding.FragmentInvoiceBinding;
import com.ecarto.cartoapp.utils.StringUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.schedulers.Schedulers;

public class InvoiceF extends Fragment implements InvoiceA.Listener {
    public static final String TAG = "INVOICE_FRAGMENT_TAG";

    FragmentInvoiceBinding binding;
    InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    List<ExtendedInvoiceEntity> invoiceEntities;
    ProjectRepository projectRepository;

    public InvoiceF() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInvoiceBinding.inflate(inflater, container, false);
        projectRepository = new ProjectRepository(getActivity().getApplication());

        initElems();
        setProjectsSpinner();
        getDatabaseData();
        initListeners();

        return binding.getRoot();
    }



    private void setProjectsSpinner() {
        List<ProjectEntity> projects = projectRepository
                .findAllProjectByParams(null, null, null)
                .subscribeOn(Schedulers.io()).blockingGet();

        List<String> projectNames = projects
                .stream().map((projectEntity -> projectEntity.getName()))
                .collect(Collectors.toList());

        int selectedProjectIndex = projects.stream()
                .map(project -> project.getProjectID())
                .collect(Collectors.toList())
                .indexOf(sharedPreferences.getLong(getString(R.string.selectedProjectID), 0));

        projectNames.add("--Crear nuevo projecto--");
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, projectNames);
        binding.spProject.setAdapter(adapter);
        binding.spProject.setSelection(selectedProjectIndex);
        binding.spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == projectNames.size() - 1) { //add project
                    NavHostFragment.findNavController(InvoiceF.this).navigate(InvoiceFDirections.actionInvoiceFragmentToAddProjectF());
                    setProjectsSpinner();
                } else { //select existing project
                    sharedPreferences.edit().putLong(getString(R.string.selectedProjectID), projects.get(position).getProjectID()).commit();
                    getDatabaseData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDatabaseData();
    }

    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);

    }

    private void getDatabaseData() {
        invoiceEntities = getAllInvoiceEntities();
        setRecyclerView(invoiceEntities);
    }

    private List<ExtendedInvoiceEntity> getAllInvoiceEntities() {
        return invoiceRepository
                .findAllExtendedInvoiceByParams(null, sharedPreferences.getLong(getString(R.string.selectedProjectID), 0),
                        null, null, null, null, null)
                .subscribeOn(Schedulers.io()).blockingGet();
    }

    private void setRecyclerView(List<ExtendedInvoiceEntity> adapterList) {
        binding.txtTotal.setText(StringUtils.formatMoney(sumAllInvoices(adapterList)));
        binding.txtNoInvoices.setVisibility(adapterList.isEmpty() ? View.VISIBLE : View.INVISIBLE);

        InvoiceA invoiceAdapter = new InvoiceA(adapterList, this);
        binding.invoiceRecyclerView.setAdapter(invoiceAdapter);
        binding.invoiceRecyclerView.setHasFixedSize(true);
    }

    private void initListeners() {
        binding.tbAddInvoice.setOnClickListener((v) -> {
            if (sharedPreferences.getLong(getString(R.string.selectedProjectID), -1) == -1) {
                Snackbar.make(binding.getRoot(), "Debe de crear un proyecto primero", Snackbar.LENGTH_SHORT).show();
            } else {
                NavHostFragment.findNavController(this)
                        .navigate(InvoiceFDirections.actionInvoiceFragmentToInsertInvoiceDialog2(0));
            }
        });

        binding.etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!binding.etSearchBar.getText().toString().isEmpty()) {
                    filterInvoices();
                } else {
                    setRecyclerView(getAllInvoiceEntities());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public Integer sumAllInvoices(List<ExtendedInvoiceEntity> extendedInvoiceEntities) {
        Integer sum = 0;

        for (ExtendedInvoiceEntity item : extendedInvoiceEntities) {
            if (item.getTotalCost() == null) item.setTotalCost(0);
            sum += item.getTotalCost();
        }
        return sum;
    }

    public void filterInvoices() {
        List<String> filterStrArray = Arrays.asList(binding.etSearchBar.getText().toString().split(" "));
        List<ExtendedInvoiceEntity> list = getAllInvoiceEntities();
        List<ExtendedInvoiceEntity> filteredList = new ArrayList<>();

        for (ExtendedInvoiceEntity item : list) {
            boolean flag = true;
            for (String searchItem : filterStrArray) {
                if (!item.toString().toLowerCase().contains(searchItem.toLowerCase())) flag = false;
            }
            if (flag) {
                filteredList.add(item);
            }
        }
        setRecyclerView(filteredList);
    }

    @Override
    public void onInvoiceSelectedClick(ExtendedInvoiceEntity extendedInvoiceEntity) {
        NavHostFragment.findNavController(this)
                .navigate(InvoiceFDirections.actionInvoiceFragmentToInvoiceDetailFragment(extendedInvoiceEntity.getInvoiceID()));
    }

    @Override
    public void onInvoiceLongClick(ExtendedInvoiceEntity extendedInvoiceEntity) {
        NavHostFragment.findNavController(this)
                .navigate(InvoiceFDirections.actionInvoiceFragmentToInvoiceOptionsDialog(extendedInvoiceEntity.getInvoiceID()));

    }

}