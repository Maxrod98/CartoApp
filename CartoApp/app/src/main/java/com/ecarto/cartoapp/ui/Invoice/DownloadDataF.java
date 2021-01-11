package com.ecarto.cartoapp.ui.Invoice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.database.Repositories.ProjectRepository;
import com.ecarto.cartoapp.databinding.FragmentDownloadDataBinding;
import com.ecarto.cartoapp.web.DTOs.InvoiceDTO;
import com.ecarto.cartoapp.web.DTOs.InvoiceDetailDTO;
import com.ecarto.cartoapp.web.DTOs.ProjectDTO;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DownloadDataF extends Fragment {
    FragmentDownloadDataBinding binding;
    ProjectRepository projectRepository;
    InvoiceRepository invoiceRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDownloadDataBinding.inflate(inflater, container, false);

        projectRepository = new ProjectRepository(getActivity().getApplication());
        invoiceRepository = new InvoiceRepository(getActivity().getApplication());

        initListeners();

        return binding.getRoot();
    }

    private void initListeners() {

        //--------------DOWNLOAD PROJECT BUTTON
        binding.btnDownloadProject.setOnClickListener((view) -> {
            if (binding.etProjectID.getText().toString().isEmpty()) return;

            projectRepository.downloadProject(Long.valueOf(binding.etProjectID.getText().toString().trim()),
                    new Callback<ProjectDTO>() {
                        @Override
                        public void onResponse(Call<ProjectDTO> call, Response<ProjectDTO> response) {
                            if (response.isSuccessful()) {
                                ProjectDTO projectDTO = response.body();
                                ProjectEntity localProjectEntity = null;
                                try {
                                    localProjectEntity = projectRepository.findAllProjectByParams(projectDTO.getProjectID(), null, null, null)
                                            .subscribeOn(Schedulers.io()).blockingGet()
                                            .stream().findFirst().orElse(null);
                                } catch (Exception e) {
                                }

                                if (localProjectEntity != null) {
                                    Snackbar.make(binding.getRoot(), "El proyecto ya esta descargado!", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                projectDTO.forEachElement(new ProjectDTO.ForEachElementListener() {
                                    @Override
                                    public void onEachProjectDTO(ProjectDTO projectDTO) {
                                        projectRepository.insertProjectEntity(new ProjectEntity(projectDTO)).subscribeOn(Schedulers.io()).blockingGet();
                                    }

                                    @Override
                                    public void onEachInvoiceDTO(InvoiceDTO invoiceDTO) {
                                        invoiceRepository.insertInvoiceEntity(new InvoiceEntity(invoiceDTO)).subscribeOn(Schedulers.io()).blockingGet();
                                    }

                                    @Override
                                    public void onEachInvoiceDetailDTO(InvoiceDetailDTO invoiceDetailDTO) {
                                        invoiceRepository.insertInvoiceDetailEntity(new InvoiceDetailEntity(invoiceDetailDTO)).subscribeOn(Schedulers.io()).blockingGet();
                                    }
                                });
                                Snackbar.make(binding.getRoot(), "Proyecto descargado correctamente", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(binding.getRoot(), "No se encontro el proyecto", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProjectDTO> call, Throwable t) {
                            Snackbar.make(binding.getRoot(), "El proyecto no se pudo descargar correctamente", Snackbar.LENGTH_LONG).show();
                        }
                    });

        });

        binding.imgClose.setOnClickListener((view) -> {
            NavHostFragment.findNavController(DownloadDataF.this).popBackStack();
        });
    }
}
