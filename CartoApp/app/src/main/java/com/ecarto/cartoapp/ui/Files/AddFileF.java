package com.ecarto.cartoapp.ui.Files;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.ViewModels.FilesTransferViewModel;
import com.ecarto.cartoapp.database.Entities.FileEntity;
import com.ecarto.cartoapp.database.Repositories.FileRepository;
import com.ecarto.cartoapp.databinding.FragmentAddFilesBinding;
import com.ecarto.cartoapp.utils.NAVIGATION;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class AddFileF extends Fragment implements  AddFileA.Listener{
    public static final String TAG = "ADD_FILE_FRAGMENT_TAG";

    FragmentAddFilesBinding binding;
    FileRepository fileRepository;
    FilesTransferViewModel filesSelectedViewModel;

    List<FileEntity> fileEntityList;

    public static AddFileF newInstance(String tagParent) {
        Bundle args = new Bundle();
        args.putString(NAVIGATION.TAG_PARENT, tagParent);
        AddFileF fragment = new AddFileF();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddFilesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initElems();
        getDatabaseData();
        initListeners();
    }

    @Override
    public void onDestroy() {
        fileEntityList = fileRepository.findAllFreedFileEntitiesBy().subscribeOn(Schedulers.io()).blockingGet();
        fileRepository.deleteFiles(fileEntityList);
        super.onDestroy();
    }

    private void initElems() {
        fileRepository = new FileRepository(getActivity().getApplicationContext());
        filesSelectedViewModel = new ViewModelProvider(requireActivity()).get( FilesTransferViewModel.class);
    }

    private void getDatabaseData() {
        fileEntityList = fileRepository.findAllFreedFileEntitiesBy().subscribeOn(Schedulers.io()).blockingGet();
        setRecyclerView(fileEntityList);
    }

    public void setRecyclerView(List<FileEntity> fileEntities){
        AddFileA addFileAdapter = new AddFileA(fileEntities, this);
        binding.addFileRecyclerView.setAdapter(addFileAdapter);
    }

    private void initListeners() {
        binding.fafClose.setOnClickListener((v) -> {
            //TODO: maybe do navhost too?
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                        .remove(this);
                trans.commit();

        });

        binding.fafAddFileToInvoiceDetail.setOnClickListener((v -> {
            filesSelectedViewModel.setFilesSelected(fileEntityList);
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                    .remove(this);
            trans.commit();
        }));
    }

    @Override
    public void onCheckedBoxSelected(List<FileEntity> fileEntities) {
        fileEntityList = fileEntities;
    }
}
