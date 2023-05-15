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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.ViewModels.MainActivityViewModel;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.database.Repositories.ProjectRepository;
import com.ecarto.cartoapp.database.Repositories.UserRepository;
import com.ecarto.cartoapp.databinding.FragmentInvoiceBinding;
import com.ecarto.cartoapp.utils.MyTouchListener;
import com.ecarto.cartoapp.utils.Selector;
import com.ecarto.cartoapp.utils.StringUtils;
import com.ecarto.cartoapp.web.DTOs.ProjectDTO;
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

public class InvoiceF extends Fragment implements InvoiceA.Listener {
    public static final String TAG = "INVOICE_FRAGMENT_TAG";

    FragmentInvoiceBinding binding;
    InvoiceRepository invoiceRepository;
    SharedPreferences sharedPreferences;
    ProjectRepository projectRepository;
    UserRepository userRepository;
    TextWatcher textWatcher = null;
    MainActivityViewModel mainActivityViewModel;


    private Timer timer = new Timer();

    @Override
    public void onResume() {
        super.onResume();
        loadRecyclerViewAsync(0);
    }
    //TODO: fix the download all the data from the user
    private void initElems() {
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());
        projectRepository = new ProjectRepository(getActivity().getApplication());
        userRepository = new UserRepository(getActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInvoiceBinding.inflate(inflater, container, false);
        initElems();
        initListeners();
        setProjectsSpinner();
        loadRecyclerViewAsync(0);

        binding.handlerView.setOnTouchListener(new MyTouchListener(binding.layout, binding.handlerView, new MyTouchListener.Listener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }
        }));



        return binding.getRoot();
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    private void initListeners() {
        //-----------------FETCH MAILS
        binding.imgMails.setOnClickListener((view) -> {
            NavHostFragment.findNavController(this).navigate(InvoiceFDirections.actionInvoiceFragmentToMailInvoicesF());
        });

        //-----------------UPLOAD PROJECT
        binding.imgUploadProject.setOnClickListener((view) -> {
            if (getSelectedProjectID() == -1) return;

            ProjectDTO projectDTO = null;
            try {
                projectDTO = new ProjectDTO(
                        projectRepository.findAllProjectByParams(getSelectedProjectID(), null, null, null)
                                .subscribeOn(Schedulers.io()).blockingGet().stream().findFirst().orElse(null), getActivity());
                if (projectDTO.getLatitude() == null) projectDTO.setLatitude("");
                if (projectDTO.getLongitude() == null) projectDTO.setLongitude("");
                if (projectDTO.getStatus() == null) projectDTO.setStatus(0);
                projectDTO.getInvoiceDTOs().stream().forEach((x) -> {
                    if (x.getLatitude() == null) x.setLatitude("");
                    if (x.getLongitude() == null) x.setLongitude("");
                    if (x.getStatus() == null) x.setStatus(0);

                    x.getInvoiceDetailDTOs().stream().forEach((y) -> {
                        if (y.getNotes() == null) y.setNotes("");
                        if (y.getStatus() == null) y.setStatus(0);
                    });
                });
            } catch (Exception e) {
            }

            blockUploadProjectButton();

            if (projectDTO != null) {
                projectRepository.uploadProject(projectDTO, new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Snackbar.make(binding.getRoot(), "subida correctamente", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(binding.getRoot(), "Hubo un error al subir los datos", Snackbar.LENGTH_SHORT).show();
                        }
                        freeUploadProjectButton();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Snackbar.make(binding.getRoot(), t.getMessage(), Snackbar.LENGTH_LONG).show();
                        freeUploadProjectButton();
                    }
                });
            }
        });
        //------------COPY PROJECT ID
        binding.txtProjectID.setOnClickListener((view) -> {
            if (getSelectedProjectID() == -1) {
                Snackbar.make(binding.getRoot(), "No hay un proyecto seleccionado!", Snackbar.LENGTH_SHORT).show();
                return;
            }

            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", "" + getSelectedProjectID());
            clipboard.setPrimaryClip(clip);

            Snackbar.make(binding.getRoot(), "ID del proyecto copiado", Snackbar.LENGTH_SHORT).show();
        });
        //--------------ADD INVOICE
        binding.tbAddInvoice.setOnClickListener((v) -> {
            if (getSelectedProjectID() == -1) {
                Snackbar.make(binding.getRoot(), "Debe de crear o descargar un proyecto primero", Snackbar.LENGTH_SHORT).show();
            } else {
                NavHostFragment.findNavController(this)
                        .navigate(InvoiceFDirections.actionInvoiceFragmentToInsertInvoiceDialog2(0, null));
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

        binding.imgUpdateFromWeb.setOnClickListener((view) -> {
            if (getSelectedProjectID() == -1) return;

            projectRepository.downloadProject(getSelectedProjectID(), new Callback<ProjectDTO>() {
                @Override
                public void onResponse(Call<ProjectDTO> call, Response<ProjectDTO> response) {
                    if (response.isSuccessful()) {
                        ProjectDTO projectDTO = response.body();
                        projectRepository.saveProjectDTO(projectDTO);
                        Snackbar.make(binding.getRoot(), "Datos actualizados", Snackbar.LENGTH_LONG).show();
                        loadRecyclerViewAsync(0);
                    }
                }

                @Override
                public void onFailure(Call<ProjectDTO> call, Throwable t) {
                    Snackbar.make(binding.getRoot(), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        });

        mainActivityViewModel.getShouldCreateNewInvoiceFromFiles().observe(requireActivity(), shouldCreateNewInvoiceFromFiles -> {
            if (shouldCreateNewInvoiceFromFiles){
                //Toast.makeText(getActivity(), "WORKING", Toast.LENGTH_LONG).show();

                NavHostFragment.findNavController(this)
                        .navigate(InvoiceOptionsFDirections.actionInvoiceOptionsDialogToInsertInvoiceDialog2(0, null));

            }
        });

    }

    private void blockUploadProjectButton() {
        binding.imgUploadProject.setVisibility(View.INVISIBLE);
        binding.progressBarUploadingProject.setVisibility(View.VISIBLE);
        binding.imgUploadProject.setEnabled(false);
    }

    private void freeUploadProjectButton() {
        binding.imgUploadProject.setVisibility(View.VISIBLE);
        binding.progressBarUploadingProject.setVisibility(View.INVISIBLE);
        binding.imgUploadProject.setEnabled(true);

    }

    private void setProjectsSpinner() {


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
        projectNames.add("--Descargar proyecto--");
        setSpinnerAdapter(projectNames);
        binding.spProject.setSelection(selectedProjectIndex);
        binding.spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (projects.isEmpty() && projectNames.size() == 2) { //no projects present
                    projectNames.add(0, "--Ninguno--");
                    setSpinnerAdapter(projectNames);
                    binding.spProject.setSelection(0);
                    sharedPreferences.edit().putLong(getString(R.string.selectedProjectID), -1).commit();
                } else if (position == projectNames.size() - 2) { //add project
                    goToAddProject();
                    setProjectsSpinner();
                } else if (position == projectNames.size() - 1) { //download project
                    goToDownloadProject();
                    setProjectsSpinner();
                } else if (!projects.isEmpty()) { //select existing project
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

    private void setSpinnerAdapter(List<String> projectNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, projectNames);
        binding.spProject.setAdapter(adapter);
    }

    private void goToAddProject() {
        NavHostFragment.findNavController(InvoiceF.this).navigate(InvoiceFDirections.actionInvoiceFragmentToAddProjectF());
    }

    private void goToDownloadProject() {
        NavHostFragment.findNavController(InvoiceF.this).navigate(InvoiceFDirections.actionInvoiceFragmentToDownloadDataF());
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