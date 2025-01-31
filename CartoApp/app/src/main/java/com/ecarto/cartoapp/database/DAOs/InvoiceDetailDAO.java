package com.ecarto.cartoapp.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface InvoiceDetailDAO {

    @Delete
    Single<Integer> deleteInvoiceDetail(InvoiceDetailEntity invoiceDetailEntity);

    @Delete
    Single<Integer> deleteInvoice(InvoiceEntity invoiceEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(InvoiceDetailEntity invoiceDetailEntity);

    @Query("SELECT * FROM InvoiceDetailEntity WHERE" +
            "((:invoiceDetailID is null or InvoiceDetailID = :invoiceDetailID) and" +
            "(:invoiceID is null or InvoiceID = :invoiceID))" )
    Single<List<InvoiceDetailEntity>> findAllInvoiceDetailBy(Long invoiceDetailID, Long invoiceID);

    @Query("SELECT InvoiceDetailEntity.*, COUNT(FileEntity.FileID) as NumFiles FROM InvoiceDetailEntity " +
            "LEFT JOIN FileEntity ON FileEntity.InvoiceDetailID = InvoiceDetailEntity.InvoiceDetailID " +
            "WHERE" +
            "((:invoiceDetailID is null or InvoiceDetailEntity.InvoiceDetailID = :invoiceDetailID) and" +
            "(:invoiceID is null or InvoiceID = :invoiceID)) " +
            "GROUP BY InvoiceDetailEntity.InvoiceDetailID " +
            "ORDER BY InvoiceDetailEntity.InvoiceDetailID" )
    Single<List<ExtendedInvoiceDetailEntity>> findAllExtendedInvoiceDetailBy(Long invoiceDetailID, Long invoiceID);

    @Query("SELECT * FROM InvoiceEntity " +
            "ORDER BY InvoiceID DESC " +
            "LIMIT 1")
    Single<InvoiceEntity> findLastInvoiceEntity();

    @Update
    Single<Integer> update(InvoiceDetailEntity invoiceDetailEntity);

    @Update
    Single<Integer> updateInvoiceEntity(InvoiceEntity invoiceEntity);



}
