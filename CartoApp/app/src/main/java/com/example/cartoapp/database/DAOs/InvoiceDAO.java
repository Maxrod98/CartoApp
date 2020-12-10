package com.example.cartoapp.database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.cartoapp.database.Entities.InvoiceEntity;

@Dao
public interface InvoiceDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InvoiceEntity invoiceEntity);


}
