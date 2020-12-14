package com.example.cartoapp.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.example.cartoapp.database.Entities.FileEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;

import java.io.File;
import java.util.List;

import io.reactivex.Single;

@Dao
public interface FileDAO  {
    @Delete
    Single<Integer> deleteFileEntity(FileEntity fileEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(FileEntity fileEntity);

    @Query("SELECT * FROM FileEntity WHERE " +
            "(:fileID is null or FileID = :fileID)")
    Single<FileEntity> findFileEntityByFileID(Integer fileID);

    @Query("SELECT * FROM FileEntity WHERE " +
            "(InvoiceDetailID is null)")
    Single<List<FileEntity>> findAllFreedFileEntitiesBy();

    @Query("SELECT * FROM FileEntity " +
            "ORDER BY FileID DESC " +
            "LIMIT 1")
    Single<FileEntity> findLastFileEntity();
}
