package com.ecarto.cartoapp.database.Repositories;

import android.content.Context;

import com.ecarto.cartoapp.database.DAOs.InvoiceDAO;
import com.ecarto.cartoapp.database.DAOs.InvoiceDetailDAO;
import com.ecarto.cartoapp.database.DatabaseClass;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;

import java.util.List;

import io.reactivex.Single;

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

    public Single<List<ExtendedInvoiceDetailEntity>> findAllExtendedInvoiceDetailBy(Integer invoiceDetailID, Integer invoiceID) {
        return invoiceDetailDAO.findAllExtendedInvoiceDetailBy(invoiceDetailID, invoiceID);
    }
    //YOU ALWAYS FORGET THAT THIS IS A SINGLE
    public Single<Integer> deleteInvoiceDetailEntity(InvoiceDetailEntity invoiceDetailEntity) {
        return invoiceDetailDAO.deleteInvoiceDetail(invoiceDetailEntity);
    }

}
