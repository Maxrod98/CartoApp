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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.database.Repositories.ProjectRepository;
import com.ecarto.cartoapp.databinding.FragmentInvoiceBinding;
import com.ecarto.cartoapp.utils.Selector;
import com.ecarto.cartoapp.utils.StringUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import io.reactivex.schedulers.Schedulers;

public class InvoiceF extends Fragment implements InvoiceA.Listener {
    public static final String TAG = "INVOICE_FRAGMENT_TAG";

    FragmentInvoiceBinding binding;
    InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    ProjectRepository projectRepository;
    TextWatcher textWatcher = null;
    private Timer timer = new Timer();

    @Override
    public void onResume() {
        super.onResume();
        loadRecyclerViewAsync(0);
    }

    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        projectRepository = new ProjectRepository(getActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInvoiceBinding.inflate(inflater, container, false);
        initElems();
        initListeners();
        setProjectsSpinner();
        loadRecyclerViewAsync(0);
        return binding.getRoot();
    }

    private void initListeners() {
        binding.tbAddInvoice.setOnClickListener((v) -> {
            if (getProjectID() == -1) {
                Snackbar.make(binding.getRoot(), "Debe de crear un proyecto primero", Snackbar.LENGTH_SHORT).show();
            } else {
                NavHostFragment.findNavController(this)
                        .navigate(InvoiceFDirections.actionInvoiceFragmentToInsertInvoiceDialog2(0));
            }
        });

        //textWatcher
        if (textWatcher != null) {
            binding.etSearchBar.removeTextChangedListener(textWatcher);
        }

        textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadRecyclerViewAsync(500);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        };
        binding.etSearchBar.addTextChangedListener(textWatcher);
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
                .indexOf(getProjectID());

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
                    loadRecyclerViewAsync(100);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadRecyclerViewAsync(Integer time) {
        //loading User Interface
        binding.progressBarLoading.setVisibility(View.VISIBLE);
        binding.invoiceRecyclerView.setClickable(false);
        timer.cancel();
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        loadRecyclerView();
                    }
                },
                time
        );

    }

    private void loadRecyclerView() {
        if (binding.etSearchBar.getText().toString().isEmpty()) {
            setRecyclerView(getAllInvoiceEntities(), true);
        } else {
            filterInvoices();
        }

        getActivity().runOnUiThread(() -> {
            Integer selectedPos = getArguments().getInt(Selector.SELECTED_POSITION, 1);
            binding.invoiceRecyclerView.scrollToPosition(selectedPos);
            binding.progressBarLoading.setVisibility(View.INVISIBLE);
            binding.invoiceRecyclerView.setClickable(true);
        });
    }

    public void filterInvoices() {
        List<String> filterStrArray = Arrays.asList(binding.etSearchBar.getText().toString().split(" "));
        List<ExtendedInvoiceEntity> list = getAllInvoiceEntities();
        List<ExtendedInvoiceEntity> filteredList = new ArrayList<>();

        for (ExtendedInvoiceEntity item : list) {
            boolean flag = true;
            for (String searchItem : filterStrArray) {
                if (!item.toString().toLowerCase().contains(searchItem.toLowerCase())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                filteredList.add(item);
                continue;
            }
        }
        setRecyclerView(filteredList, false);
    }

    private List<ExtendedInvoiceEntity> getAllInvoiceEntities() {
        return invoiceRepository
                .findAllExtendedInvoiceByParams(null, getProjectID(), null, null, null, null, null)
                .subscribeOn(Schedulers.io()).blockingGet();
    }

    private void setRecyclerView(List<ExtendedInvoiceEntity> adapterList, boolean loadAll) {
        getActivity().runOnUiThread(() -> {
            Integer total = 0;
            if (loadAll) {//improves efficiency
                try {
                    total = invoiceRepository.sumAllInvoiceEntitiesByParams(getProjectID())
                            .subscribeOn(Schedulers.io()).blockingGet();
                } catch (Exception e) {
                }
            } else {
                total = sumAllInvoices(adapterList);
            }
            binding.txtTotal.setText(StringUtils.formatMoney(total));

            binding.txtNoInvoices.setVisibility(adapterList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
            InvoiceA invoiceAdapter = new InvoiceA(adapterList, InvoiceF.this);
            binding.invoiceRecyclerView.setAdapter(invoiceAdapter);
            binding.invoiceRecyclerView.setHasFixedSize(true);
        });
    }

    public Integer sumAllInvoices(List<ExtendedInvoiceEntity> extendedInvoiceEntities) {
        Integer sum = 0;
        for (ExtendedInvoiceEntity item : extendedInvoiceEntities) {
            sum += item.getTotalCost();
        }
        return sum;
    }

    private Long getProjectID() {
        return sharedPreferences.getLong(getString(R.string.selectedProjectID), -1);
    }

    @Override
    public void onInvoiceClick(ExtendedInvoiceEntity extendedInvoiceEntity) {
        NavHostFragment.findNavController(this)
                .navigate(InvoiceFDirections.actionInvoiceFragmentToInvoiceDetailFragment(extendedInvoiceEntity.getInvoiceID()));
    }

    @Override
    public void onInvoiceLongClick(ExtendedInvoiceEntity extendedInvoiceEntity) {
        NavHostFragment.findNavController(this)
                .navigate(InvoiceFDirections.actionInvoiceFragmentToInvoiceOptionsDialog(extendedInvoiceEntity.getInvoiceID()));
    }
}