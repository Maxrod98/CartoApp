package com.ecarto.cartoapp.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ecarto.cartoapp.database.Entities.FileEntity;

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

    @Query("SELECT * FROM FileEntity " +
            "WHERE (:fileID is null or :fileID = FileID) AND" +
            "(:invoiceDetailID is null or :invoiceDetailID = InvoiceDetailID) AND" +
            "(:pathToFile is null or PathToFile LIKE :pathToFile) AND" +
            "(:typeOfFile is null or TypeOfFile LIKE TypeOfFile) AND" +
            "(:originalName is null or OriginalName LIKE :originalName) AND" +
            "(:status is null or :status = Status)")
    Single<List<FileEntity>> findAllFileEntitiesByParams(Long fileID, Long invoiceDetailID, String pathToFile, String typeOfFile, String originalName, Integer status);

    @Update
    Single<Integer> updateFileEntity(FileEntity fileEntity);
}
