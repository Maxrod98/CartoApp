package com.ecarto.cartoapp.ui.FileList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.FileEntity;
import com.ecarto.cartoapp.database.Repositories.FileRepository;
import com.ecarto.cartoapp.databinding.FragmentAddFilesBinding;
import com.ecarto.cartoapp.utils.NAVIGATION;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class AddFileFragment extends Fragment implements  AddFileAdapter.Listener{
    public static final String TAG = "ADD_FILE_FRAGMENT_TAG";

    FragmentAddFilesBinding binding;
    FileRepository fileRepository;
    AddFileFragment.Listener listener;
    List<FileEntity> fileEntityList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public AddFileFragment(){

    }

    public static AddFileFragment newInstance(String tagParent) {
        Bundle args = new Bundle();
        args.putString(NAVIGATION.TAG_PARENT, tagParent);
        AddFileFragment fragment = new AddFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddFilesBinding.inflate(inflater, container, false);

        String parentTag = getArguments().getString(NAVIGATION.TAG_PARENT, "");
        if (parentTag.isEmpty()){
            listener = (Listener) getActivity();
        } else {
            listener = (Listener) getActivity().getSupportFragmentManager().findFragmentByTag(parentTag);
        }

        fileRepository = new FileRepository(getActivity().getApplicationContext());
        List<FileEntity> fileEntityList = fileRepository.findAllFreedFileEntitiesBy().subscribeOn(Schedulers.io()).blockingGet();

        AddFileAdapter addFileAdapter = new AddFileAdapter(fileEntityList, this);
        binding.addFileRecyclerView.setAdapter(addFileAdapter);

        binding.fafAddFileToInvoiceDetail.setOnClickListener((v)-> {
            if (fileEntityList != null){
                if (!fileEntityList.isEmpty()){
                    listener.addFilesToInvoiceDetail(fileEntityList);
                }
            }
        });

        binding.fafClose.setOnClickListener((v -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            trans.remove(this);
            trans.commit();
        }));

        return binding.getRoot();
    }

    @Override
    public void updateSelectedFiles(List<FileEntity> fileEntities) {
        fileEntityList = fileEntities;
    }

    public interface Listener {
        void addFilesToInvoiceDetail(List<FileEntity> fileEntities);
    }
}
