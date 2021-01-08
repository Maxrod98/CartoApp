package com.ecarto.cartoapp.database.Repositories;

import android.content.Context;

import com.ecarto.cartoapp.database.DAOs.ProjectDAO;
import com.ecarto.cartoapp.database.DatabaseClass;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;

import java.util.List;

import io.reactivex.Single;

public class ProjectRepository {
    private ProjectDAO projectDAO;

    public ProjectRepository(Context application) {
        DatabaseClass db = DatabaseClass.getDatabase(application);
        projectDAO = db.projectDAO();
    }

    public Single<Long> insertProjectEntity(ProjectEntity projectEntity) {
        return projectDAO.insertProjectEntity(projectEntity);
    }

    public Single<Integer> deleteProjectEntity(ProjectEntity projectEntity){
        return projectDAO.deleteProjectEntity(projectEntity);
    }

    public Single<List<ProjectEntity>> findAllProjectByParams(Long projectID, String name, Integer status, Integer userID){
        return projectDAO.findAllProjectByParams(projectID, name, status, userID);
    }
}
