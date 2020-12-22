package com.ecarto.cartoapp.ui;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.database.Repositories.ProjectRepository;
import com.ecarto.cartoapp.databinding.FragmentAddProjectBinding;
import com.ecarto.cartoapp.utils.StringUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class AddProjectF extends Fragment {
    FragmentAddProjectBinding binding;
    SharedPreferences sharedPreferences;
    ProjectRepository projectRepository;

    int day;
    int month;
    int year_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddProjectBinding.inflate(inflater, container, false);
        initElems();
        initListeners();
        return binding.getRoot();
    }

    private void initElems() {
        projectRepository = new ProjectRepository(getActivity().getApplication());
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year_ = calendar.get(Calendar.YEAR);
        binding.etStartDate.setText(day + "/" + (month + 1) + "/" + year_);
    }

    private void initListeners() {
        binding.btnClose.setOnClickListener((v) -> {
            if (projectRepository.findAllProjectByParams(null, null, null) //check if no projects are present
                    .subscribeOn(Schedulers.io()).blockingGet().isEmpty()) {
                Snackbar.make(binding.getRoot(), "Tiene que crear un proyecto para continuar", Snackbar.LENGTH_LONG).show();
            } else {
                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        binding.btnAddProject.setOnClickListener((v) -> {
            saveProject();
        });

        binding.etStartDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatePickerDialog picker;
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        (view, year, monthOfYear, dayOfMonth) -> {
                            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            binding.etStartDate.setText(date);
                        }, year_, month, day);
                picker.show();
            }
        });

    }

    private void saveProject() {
        if (binding.etLocation.getText().toString().isEmpty() || binding.etName.getText().toString().isEmpty()) {
            Snackbar.make(binding.getRoot(), "Rellene todos los campos.", Snackbar.LENGTH_SHORT).show();
        } else {
            ProjectEntity project = new ProjectEntity();
            project.setName(binding.etName.getText().toString());
            project.setLocation(binding.etLocation.getText().toString());
            project.setStartDate(StringUtils.formatDateFromString(binding.etLocation.getText().toString()));

            projectRepository.insertProjectEntity(project).subscribeOn(Schedulers.io()).blockingGet();
            Snackbar.make(binding.getRoot(), "Proyecto agregado correctamente", Snackbar.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        }
    }
}
