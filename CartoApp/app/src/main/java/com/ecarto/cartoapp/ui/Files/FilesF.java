package com.ecarto.cartoapp.ui.Files;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.database.Entities.FileEntity;
import com.ecarto.cartoapp.database.Repositories.FileRepository;
import com.ecarto.cartoapp.databinding.FragmentFileBinding;

import java.io.File;
import java.util.List;

import io.reactivex.schedulers.Schedulers;


public class FilesF extends Fragment implements FilesA.Listener {
    public final static String SelectedInvoiceDetailID = "SelectedInvoiceDetailID";

    FragmentFileBinding binding;
    FileRepository fileRepository;

    Long selectedInvoiceDetailID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFileBinding.inflate(inflater, container, false);

        initListeners();
        initElems();
        getDatabaseData();

        return binding.getRoot();
    }

    private void initListeners() {
        binding.imgClose.setOnClickListener((v) -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void initElems() {
        selectedInvoiceDetailID = getArguments().getLong(SelectedInvoiceDetailID, -1);
        fileRepository = new FileRepository(getActivity().getApplication());
    }

    public void getDatabaseData(){
        List<FileEntity> files = fileRepository.findAllFileEntitiesByParams(null, selectedInvoiceDetailID, null, null, null, null)
                .subscribeOn(Schedulers.io()).blockingGet();

        setRecyclerView(files);
    }

    public void setRecyclerView(List<FileEntity> files){
        FilesA filesA = new FilesA(files, this);

        binding.lblNoFiles.setVisibility(files.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        binding.recyclerViewFiles.setAdapter(filesA);
        binding.recyclerViewFiles.setHasFixedSize(true);
    }

    public void displayPDF(FileEntity fileEntity) {
        File file = null;
        file = new File(fileEntity.getPathToFile());

        String type = "";

        if (fileEntity.getTypeOfFile().equals("pdf") ) type = "application/pdf";
        if (fileEntity.getTypeOfFile().equals("jpeg")) type = "image/jpeg";
        if (fileEntity.getTypeOfFile().equals("jpg")) type = "image/jpg";
        if (fileEntity.getTypeOfFile().equals("png")) type = "image/png";

        if (file.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(FileProvider.getUriForFile(getActivity(), "com.example.cartoapp", file), type);
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
    }

    @Override
    public void onItemSelected(FileEntity fileEntity) {
        displayPDF(fileEntity);
    }
}
