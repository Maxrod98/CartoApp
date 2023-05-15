package com.ecarto.cartoapp.database.Repositories;

import android.content.Context;

import com.ecarto.cartoapp.database.DAOs.FileDAO;
import com.ecarto.cartoapp.database.DatabaseClass;
import com.ecarto.cartoapp.database.Entities.FileEntity;
import com.ecarto.cartoapp.utils.FileManipUtils;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class FileRepository {
    private FileDAO fileDAO;

    public FileRepository(Context application) {
        DatabaseClass db = DatabaseClass.getDatabase(application);
        fileDAO = db.fileDAO();
    }

    public Single<Integer> deleteFileEntity(FileEntity fileEntity){
        return fileDAO.deleteFileEntity(fileEntity);
    }

    public Single<Long> insertFileEntity(FileEntity fileEntity){
        return fileDAO.insert(fileEntity);
    }

    public Single<Integer> updateFileEntity(FileEntity fileEntity){
        return fileDAO.updateFileEntity(fileEntity);
    }

    public Single<FileEntity> findFileEntityByFileID(Integer fileID){
        return fileDAO.findFileEntityByFileID(fileID);
    }

    public Single<List<FileEntity>> findAllFreedFileEntitiesBy(){
        return fileDAO.findAllFreedFileEntitiesBy();
    }

    public Single<List<FileEntity>> findAllFileEntitiesByParams(Long fileID, Long invoiceDetailID, String pathToFile, String typeOfFile, String originalName, Integer status){
        return fileDAO.findAllFileEntitiesByParams(fileID, invoiceDetailID, pathToFile, typeOfFile, originalName, status);
    }

    public Single<FileEntity> findLastFileEntity(){
        return fileDAO.findLastFileEntity();
    }

    public void deleteFiles(List<FileEntity> fileEntities){ //deletes both directory files and entities
        for (FileEntity file : fileEntities){
            FileManipUtils.deleteFile(file.getPathToFile());
            deleteFileEntity(file).subscribeOn(Schedulers.io()).blockingGet();
        }
    }

}
