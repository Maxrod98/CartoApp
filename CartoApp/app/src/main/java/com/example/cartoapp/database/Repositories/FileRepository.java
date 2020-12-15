package com.example.cartoapp.database.Repositories;

import android.content.Context;

import com.example.cartoapp.database.DAOs.FileDAO;
import com.example.cartoapp.database.DatabaseClass;
import com.example.cartoapp.database.Entities.FileEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.utils.FileUtils;

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

    public Single<FileEntity> findFileEntityByFileID(Integer fileID){
        return fileDAO.findFileEntityByFileID(fileID);
    }

    public Single<List<FileEntity>> findAllFreedFileEntitiesBy(){
        return fileDAO.findAllFreedFileEntitiesBy();
    }

    public Single<FileEntity> findLastFileEntity(){
        return fileDAO.findLastFileEntity();
    }

    public void deleteFiles(List<FileEntity> fileEntities){ //deletes both directory files and entities
        for (FileEntity file : fileEntities){
            FileUtils.deleteFile(file.getPathToFile());
            deleteFileEntity(file).subscribeOn(Schedulers.io()).blockingGet();
        }
    }

}
