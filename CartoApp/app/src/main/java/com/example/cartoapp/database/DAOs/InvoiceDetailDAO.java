package com.example.cartoapp.database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cartoapp.database.Entities.InvoiceDetailEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface InvoiceDetailDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InvoiceDetailEntity invoiceDetailEntity);

    @Query("SELECT * FROM InvoiceDetailEntity WHERE" +
            "((:invoiceDetailID is null or InvoiceDetailID = :invoiceDetailID) and" +
            "(:invoiceID is null or InvoiceID = :invoiceID))" )
    Single<List<InvoiceDetailEntity>> findAllInvoiceDetailBy(Integer invoiceDetailID, Integer invoiceID);


}
