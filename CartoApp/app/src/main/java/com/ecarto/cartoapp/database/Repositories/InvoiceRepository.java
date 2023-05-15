package com.ecarto.cartoapp.database.Repositories;

import android.content.Context;

import com.ecarto.cartoapp.database.DAOs.InvoiceDAO;
import com.ecarto.cartoapp.database.DAOs.InvoiceDetailDAO;
import com.ecarto.cartoapp.database.DatabaseClass;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.web.DTOs.MailInvoiceDTO;
import com.ecarto.cartoapp.web.DTOs.ProjectDTO;
import com.ecarto.cartoapp.web.RetrofitInstance;
import com.ecarto.cartoapp.web.Services.ProjectService;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class InvoiceRepository {
    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO invoiceDetailDAO;
    Retrofit retrofit;


    public InvoiceRepository(Context application) {
        DatabaseClass db = DatabaseClass.getDatabase(application);
        invoiceDAO = db.invoiceDAO();
        invoiceDetailDAO = db.invoiceDetailDAO();
        retrofit = RetrofitInstance.getInstance(application).getRetrofit();

    }

    public void getMailData( Callback<MailInvoiceDTO[]> callback){
        ProjectService projectService = retrofit.create(ProjectService.class);
        Call<MailInvoiceDTO[]> callRequest = projectService.getMailData();
        callRequest.enqueue(callback);
    }

    public Single<Long> insertInvoiceEntity(InvoiceEntity invoiceEntity) {
        return invoiceDAO.insert(invoiceEntity);
    }

    public Single<List<ExtendedInvoiceEntity>> findAllExtendedInvoiceByParams(Long invoiceID, Long projectID, String userID, String seller, String description, Integer status, Boolean deleted) {
        return invoiceDAO.findAllExtendedInvoiceBy(invoiceID, projectID, userID, seller, description, status, deleted);
    }

    public Single<Integer> deleteInvoiceEntity(InvoiceEntity invoiceEntity) {
        return invoiceDetailDAO.deleteInvoice(invoiceEntity);
    }

    public Single<InvoiceEntity> findLastInvoiceEntity(){
        return invoiceDetailDAO.findLastInvoiceEntity();
    }

    //InvoiceDetailEntity

    public Single<Long> insertInvoiceDetailEntity(InvoiceDetailEntity invoiceDetailEntity) {
        return invoiceDetailDAO.insert(invoiceDetailEntity);
    }

    public void insertInvoiceDetailEntityPrefilled(String description, int cost, long invoiceID) {
        InvoiceDetailEntity invoiceDetail = new InvoiceDetailEntity();
        invoiceDetail.setStatus(0);
        invoiceDetail.setNotes("");
        invoiceDetail.setConceptDescription(description + " - Detalle");
        invoiceDetail.setCostOfItem(cost);
        invoiceDetail.setInvoiceID(invoiceID);
        insertInvoiceDetailEntity(invoiceDetail).subscribeOn(Schedulers.io()).blockingGet();
    }

    public Single<Integer> updateInvoiceDetailEntity(InvoiceDetailEntity invoiceDetailEntity){
        return invoiceDetailDAO.update(invoiceDetailEntity);
    }

    public Single<Integer> updateInvoiceEntity(InvoiceEntity invoiceEntity){
        return invoiceDetailDAO.updateInvoiceEntity(invoiceEntity);
    }

    public Single<List<InvoiceDetailEntity>> findAllInvoiceDetailBy(Long invoiceDetailID, Long invoiceID) {
        return invoiceDetailDAO.findAllInvoiceDetailBy(invoiceDetailID, invoiceID);
    }

    public Single<List<ExtendedInvoiceDetailEntity>> findAllExtendedInvoiceDetailBy(Long invoiceDetailID, Long invoiceID) {
        return invoiceDetailDAO.findAllExtendedInvoiceDetailBy(invoiceDetailID, invoiceID);
    }
    //YOU ALWAYS FORGET THAT THIS IS A SINGLE
    public Single<Integer> deleteInvoiceDetailEntity(InvoiceDetailEntity invoiceDetailEntity) {
        return invoiceDetailDAO.deleteInvoiceDetail(invoiceDetailEntity);
    }

    public Single<Integer> sumAllInvoiceEntitiesByParams(Long projectID){
        return invoiceDAO.sumAllInvoiceEntitiesByParams(projectID);
    }

}
