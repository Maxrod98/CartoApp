package com.ecarto.cartoapp.ui.Invoice;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.database.Repositories.ProjectRepository;
import com.ecarto.cartoapp.database.Repositories.UserRepository;
import com.ecarto.cartoapp.databinding.FragmentInvoiceBinding;
import com.ecarto.cartoapp.utils.Selector;
import com.ecarto.cartoapp.utils.StringUtils;
import com.ecarto.cartoapp.web.DTOs.ProjectDTO;
import com.ecarto.cartoapp.web.RetrofitInstance;
import com.ecarto.cartoapp.web.Services.ProjectService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InvoiceF extends Fragment implements InvoiceA.Listener {
    public static final String TAG = "INVOICE_FRAGMENT_TAG";

    FragmentInvoiceBinding binding;
    InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    ProjectRepository projectRepository;
    UserRepository userRepository;
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
        userRepository = new UserRepository(getActivity().getApplication());
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
        binding.imgUploadProject.setOnClickListener((view) -> {
            ProjectDTO projectDTO = null;
            try {
                projectDTO = new ProjectDTO(
                        projectRepository.findAllProjectByParams(getSelectedProjectID(), null, null, null)
                                .subscribeOn(Schedulers.io()).blockingGet().stream().findFirst().orElse(null), getActivity());
            } catch (Exception e){
            }

            blockUploadProjectButton();

            if (projectDTO != null){
                Retrofit retrofit = RetrofitInstance.getInstance(getContext()).getRetrofit();
                ProjectService projectService = retrofit.create(ProjectService.class);
                Call<String> callRequest = projectService.uploadProjectDTO(projectDTO);

                callRequest.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            Snackbar.make(binding.getRoot(), response.body(), Snackbar.LENGTH_SHORT).show();
                        } else {

                        }
                        freeUploadProjectButton();
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Snackbar.make(binding.getRoot(), "No hay conexion para subir los datos", Snackbar.LENGTH_SHORT).show();
                        freeUploadProjectButton();
                    }
                });
            }
        });

        binding.tbAddInvoice.setOnClickListener((v) -> {
            if (getSelectedProjectID() == -1) {
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

    private void blockUploadProjectButton(){
        binding.imgUploadProject.setVisibility(View.INVISIBLE);
        binding.progressBarUploadingProject.setVisibility(View.VISIBLE);
        binding.imgUploadProject.setEnabled(false);
    }

    private void freeUploadProjectButton(){
        binding.imgUploadProject.setVisibility(View.VISIBLE);
        binding.progressBarUploadingProject.setVisibility(View.INVISIBLE);
        binding.imgUploadProject.setEnabled(true);
        Snackbar.make(binding.getRoot(), "Proyectos subidos correctamente", Snackbar.LENGTH_SHORT).show();
    }

    private void setProjectsSpinner() {


        binding.txtProjectID.setOnClickListener( (view) -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", ""+ getSelectedProjectID());
            clipboard.setPrimaryClip(clip);

            Snackbar.make(binding.getRoot(), "ID del proyecto copiado", Snackbar.LENGTH_SHORT).show();
        });

        List<ProjectEntity> projects = projectRepository
                .findAllProjectByParams(null, null, null, null)
                .subscribeOn(Schedulers.io()).blockingGet();

        List<String> projectNames = projects
                .stream().map((projectEntity -> projectEntity.getName()))
                .collect(Collectors.toList());

        int selectedProjectIndex = projects.stream()
                .map(project -> project.getProjectID())
                .collect(Collectors.toList())
                .indexOf(getSelectedProjectID());

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
                    binding.txtProjectID.setText("ID de Proyecto: " + getSelectedProjectID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadRecyclerViewAsync(Integer time) {
        //loading User Interface
        binding.progressBarLoading.getRoot().setVisibility(View.VISIBLE);
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
            binding.progressBarLoading.getRoot().setVisibility(View.INVISIBLE);
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
                .findAllExtendedInvoiceByParams(null, getSelectedProjectID(), null, null, null, null, null)
                .subscribeOn(Schedulers.io()).blockingGet();
    }

    private void setRecyclerView(List<ExtendedInvoiceEntity> adapterList, boolean loadAll) {
        getActivity().runOnUiThread(() -> {
            Integer total = 0;
            if (loadAll) {//improves efficiency
                try {
                    total = invoiceRepository.sumAllInvoiceEntitiesByParams(getSelectedProjectID())
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
        return extendedInvoiceEntities.stream().mapToInt(ExtendedInvoiceEntity::getTotalCost).sum();
    }

    private Long getSelectedProjectID() {
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