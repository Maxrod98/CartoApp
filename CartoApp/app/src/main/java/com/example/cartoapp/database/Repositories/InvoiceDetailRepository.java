package com.example.cartoapp.database.Repositories;

import android.content.Context;

import com.example.cartoapp.database.DAOs.InvoiceDetailDAO;
import com.example.cartoapp.database.DatabaseClass;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;

import java.util.List;

import io.reactivex.Single;

public class InvoiceDetailRepository {
    private InvoiceDetailDAO invoiceDetailDAO;

    public InvoiceDetailRepository(Context application){
        DatabaseClass db = DatabaseClass.getDatabase(application);
        invoiceDetailDAO = db.invoiceDetailDAO();
    }

    public void insert(InvoiceDetailEntity invoiceDetailEntity){
        DatabaseClass.databaseWriteExecutor.execute(()-> {
            invoiceDetailDAO.insert(invoiceDetailEntity);
        });
    }

    public Single<List<InvoiceDetailEntity>> findAllInvoiceDetailBy(Integer invoiceDetailID, Integer invoiceID){
        return invoiceDetailDAO.findAllInvoiceDetailBy(invoiceDetailID, invoiceID);
    }


}
