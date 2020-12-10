package com.example.cartoapp.database.Repositories;

import android.content.Context;

import com.example.cartoapp.database.DAOs.InvoiceDAO;
import com.example.cartoapp.database.DatabaseClass;
import com.example.cartoapp.database.Entities.InvoiceEntity;

public class InvoiceRepository {
    private InvoiceDAO invoiceDAO;

    public InvoiceRepository(Context application){
        DatabaseClass db = DatabaseClass.getDatabase(application);
        invoiceDAO = db.invoiceDAO();
    }

    public void insert(InvoiceEntity invoiceEntity){
        DatabaseClass.databaseWriteExecutor.execute(()-> {
            invoiceDAO.insert(invoiceEntity);
        });
    }

}
