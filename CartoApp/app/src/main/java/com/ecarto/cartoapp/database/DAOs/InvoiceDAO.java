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
            "WHERE (:invoiceID is null or :invoiceID = InvoiceEntity.InvoiceID) AND " +
            "(:projectID is null or :projectID = InvoiceEntity.ProjectID) AND " +
            "(:userID is null or :userID = InvoiceEntity.UserID) AND " +
            "(:seller  is null or InvoiceEntity.Seller LIKE :seller) AND" +
            "(:description is null or InvoiceEntity.Description LIKE :description ) AND" +
            "(:status is null or InvoiceEntity.Status = :status) AND" +
            "(:deleted is null or (InvoiceEntity.Status = 4) = :deleted)" + //check enums InvoiceStatus
            "GROUP BY InvoiceEntity.InvoiceID" +
            " ORDER BY Date DESC")
    Single<List<ExtendedInvoiceEntity>> findAllExtendedInvoiceBy (Integer invoiceID, Integer projectID, Integer userID, String seller, String description, Integer status, Boolean deleted);
}
