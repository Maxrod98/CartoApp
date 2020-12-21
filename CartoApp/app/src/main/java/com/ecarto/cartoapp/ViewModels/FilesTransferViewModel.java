package com.ecarto.cartoapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecarto.cartoapp.database.Entities.FileEntity;

import java.util.List;

public class FilesTransferViewModel extends ViewModel {
    private MutableLiveData<List<FileEntity>> filesSelected = new MutableLiveData<>();

    public MutableLiveData<List<FileEntity>> getFilesSelected(){
        return filesSelected;
    }

    public void setFilesSelected(List<FileEntity> filesSelected){
        this.filesSelected.setValue(filesSelected);
    }

    public LiveData<Long> getInvoiceDetailID;

}
