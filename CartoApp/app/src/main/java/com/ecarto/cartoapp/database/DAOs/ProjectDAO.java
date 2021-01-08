package com.ecarto.cartoapp.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ProjectDAO {
    @Delete
    Single<Integer> deleteProjectEntity(ProjectEntity projectEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertProjectEntity(ProjectEntity projectEntity);

    @Query("SELECT * FROM ProjectEntity WHERE" +
            "(:projectID is null or :projectID = ProjectID) AND" +
            "(:name is null or Name LIKE :name ) AND" +
            "(:status is null or :status = Status) AND" +
            "(:userID is null or :userID = UserID)")
    Single<List<ProjectEntity>> findAllProjectByParams(Long projectID, String name, Integer status, Integer userID);

}
