package com.ecarto.cartoapp.database.Repositories;

import android.content.Context;
import android.service.autofill.UserData;

import com.ecarto.cartoapp.database.DAOs.ProjectDAO;
import com.ecarto.cartoapp.database.DatabaseClass;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.web.DTOs.InvoiceDTO;
import com.ecarto.cartoapp.web.DTOs.InvoiceDetailDTO;
import com.ecarto.cartoapp.web.DTOs.ProjectDTO;
import com.ecarto.cartoapp.web.DTOs.UserDataResponseDTO;
import com.ecarto.cartoapp.web.RetrofitInstance;
import com.ecarto.cartoapp.web.Services.ProjectService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProjectRepository {
    private ProjectDAO projectDAO;
    private InvoiceRepository invoiceRepository;
    private Context context;
    Retrofit retrofit;

    //All webservices call within a repository

    public ProjectRepository(Context application) {
        DatabaseClass db = DatabaseClass.getDatabase(application);
        projectDAO = db.projectDAO();
        invoiceRepository = new InvoiceRepository(application);
        context = application;
        retrofit = RetrofitInstance.getInstance(context).getRetrofit();
    }

    public void downloadProject(Long projectID, Callback<ProjectDTO> callback){
        ProjectService projectService = retrofit.create(ProjectService.class);
        Call<ProjectDTO> callRequest = projectService.downloadProjectDTO(projectID);
        callRequest.enqueue(callback);
    }

    public void uploadProject(ProjectDTO projectDTO, Callback<String> callback){
        ProjectService projectService = retrofit.create(ProjectService.class);
        Call<String> callRequest = projectService.uploadProjectDTO(projectDTO);
        callRequest.enqueue(callback);
    }

    public void downloadUserData( Integer userID, Callback<ProjectDTO[]> callback){
        ProjectService projectService = retrofit.create(ProjectService.class);
        Call<ProjectDTO[]> callRequest = projectService.downloadUserData(userID);
        callRequest.enqueue(callback);
    }

    public Single<Long> insertProjectEntity(ProjectEntity projectEntity) {
        return projectDAO.insertProjectEntity(projectEntity);
    }

    public Single<Integer> updateProjectEntity(ProjectEntity projectEntity){
        return projectDAO.updateProjectEntity(projectEntity);
    }

    public Single<Integer> deleteProjectEntity(ProjectEntity projectEntity){
        return projectDAO.deleteProjectEntity(projectEntity);
    }

    public Single<List<ProjectEntity>> findAllProjectByParams(Long projectID, String name, Integer status, Integer userID){
        return projectDAO.findAllProjectByParams(projectID, name, status, userID);
    }

    public void saveProjectDTO(ProjectDTO projectDTO) {
        projectDTO.forEachElement(new ProjectDTO.ForEachElementListener() {
            @Override
            public void onEachProjectDTO(ProjectDTO projectDTO) {
                if (updateProjectEntity(new ProjectEntity(projectDTO)).subscribeOn(Schedulers.io()).blockingGet() == 0) {
                    insertProjectEntity(new ProjectEntity(projectDTO)).subscribeOn(Schedulers.io()).blockingGet();
                }
            }

            @Override
            public void onEachInvoiceDTO(InvoiceDTO invoiceDTO) {
                if (invoiceRepository.updateInvoiceEntity(new InvoiceEntity(invoiceDTO)).subscribeOn(Schedulers.io()).blockingGet() == 0) {
                    invoiceRepository.insertInvoiceEntity(new InvoiceEntity(invoiceDTO)).subscribeOn(Schedulers.io()).blockingGet();
                }
            }

            @Override
            public void onEachInvoiceDetailDTO(InvoiceDetailDTO invoiceDetailDTO) {
                if (invoiceRepository.updateInvoiceDetailEntity(new InvoiceDetailEntity(invoiceDetailDTO)).subscribeOn(Schedulers.io()).blockingGet() == 0) {
                    invoiceRepository.insertInvoiceDetailEntity(new InvoiceDetailEntity(invoiceDetailDTO)).subscribeOn(Schedulers.io()).blockingGet();
                }
            }
        });

    }
}
