package com.ecarto.cartoapp.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface InvoiceDAO {
    @Delete
    Single<Integer> deleteInvoiceEntity(InvoiceEntity invoiceEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(InvoiceEntity invoiceEntity);

    @Query ("SELECT * FROM InvoiceEntity WHERE " +
            "(:invoiceID is null or InvoiceID = :invoiceID)")
    Single<InvoiceEntity> findAllInvoiceBy(Integer invoiceID);

    @Query("SELECT InvoiceEntity.*, SUM(invoiceDetail.CostOfItem) as totalCost FROM InvoiceEntity " +
            "LEFT JOIN InvoiceDetailEntity invoiceDetail ON InvoiceEntity.InvoiceID = invoiceDetail.InvoiceID " +
            "WHERE (:invoiceID is null or :invoiceID = InvoiceEntity.InvoiceID) " +
            "GROUP BY InvoiceEntity.InvoiceID" +
            " ORDER BY Date DESC")
    Single<List<ExtendedInvoiceEntity>> findAllExtendedInvoiceBy (Integer invoiceID);
}
