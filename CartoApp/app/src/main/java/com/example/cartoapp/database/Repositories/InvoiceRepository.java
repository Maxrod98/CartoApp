package com.example.cartoapp.database.Repositories;

import android.content.Context;

import com.example.cartoapp.database.DAOs.InvoiceDAO;
import com.example.cartoapp.database.DAOs.InvoiceDetailDAO;
import com.example.cartoapp.database.DatabaseClass;
import com.example.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class InvoiceRepository {
    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO invoiceDetailDAO;


    public InvoiceRepository(Context application) {
        DatabaseClass db = DatabaseClass.getDatabase(application);
        invoiceDAO = db.invoiceDAO();
        invoiceDetailDAO = db.invoiceDetailDAO();
    }

    public Single<Long> insert(InvoiceEntity invoiceEntity) {
        return invoiceDAO.insert(invoiceEntity);
    }

    public Single<List<ExtendedInvoiceEntity>> findAllExtendedInvoiceBy(Integer invoiceID) {
        return invoiceDAO.findAllExtendedInvoiceBy(invoiceID);
    }

    public Single<Integer> deleteInvoiceEntity(InvoiceEntity invoiceEntity) {
        return invoiceDetailDAO.deleteInvoice(invoiceEntity);
    }

    public Single<InvoiceEntity> findLastInvoiceEntity(){
        return invoiceDetailDAO.findLastInvoiceEntity();
    }

    //InvoiceDetailEntity

    public Single<Long> insert(InvoiceDetailEntity invoiceDetailEntity) {
        return invoiceDetailDAO.insert(invoiceDetailEntity);
    }

    public Single<List<InvoiceDetailEntity>> findAllInvoiceDetailBy(Integer invoiceDetailID, Integer invoiceID) {
        return invoiceDetailDAO.findAllInvoiceDetailBy(invoiceDetailID, invoiceID);
    }

    public Single<Integer> deleteInvoiceDetailEntity(InvoiceDetailEntity invoiceDetailEntity) {
        return invoiceDetailDAO.deleteInvoiceDetail(invoiceDetailEntity);
    }

}
