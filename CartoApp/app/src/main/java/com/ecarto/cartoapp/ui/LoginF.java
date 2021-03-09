package com.ecarto.cartoapp.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.UserEntity;
import com.ecarto.cartoapp.database.Repositories.ProjectRepository;
import com.ecarto.cartoapp.database.Repositories.UserRepository;
import com.ecarto.cartoapp.databinding.FragmentLoginBinding;
import com.ecarto.cartoapp.web.DTOs.ProjectDTO;
import com.ecarto.cartoapp.web.DTOs.UserDataResponseDTO;
import com.ecarto.cartoapp.web.DTOs.UserResponseDTO;
import com.ecarto.cartoapp.web.DTOs.UserRequestDTO;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginF extends Fragment {
    FragmentLoginBinding binding;
    SharedPreferences sharedPreferences;
    UserRepository userRepository;
    ProjectRepository projectRepository;
    Integer todayNum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferences), Activity.MODE_PRIVATE);

        userRepository = new UserRepository(getActivity().getApplicationContext());
        projectRepository = new ProjectRepository(getActivity().getApplicationContext());

        setTodayNum();
        Integer lastDayLogin = sharedPreferences.getInt(getResources().getString(R.string.lastDayLogin), -1);
        if ( /*lastDayLogin == todayNum && */ lastDayLogin != -1) { //if not loged in before, log in
            goToInvoiceFragment();
        }

        initListeners();
        return binding.getRoot();
    }

    public void setTodayNum() {
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        todayNum = c.get(Calendar.DAY_OF_MONTH);
    }

    //TODO: create sliding menu on the left

    public void goToInvoiceFragment() {
        /*
        if (projectRepository.findAllProjectByParams(null, null, null, null)
                .subscribeOn(Schedulers.io()).blockingGet().isEmpty()){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setCancelable(true);
            builder.setTitle("Descargar informacion de este usuario.");
            builder.setMessage("¿Desea descargar toda la informacion de este usuario?");
            builder.setPositiveButton("Descargar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            projectRepository.downloadUserData(userRepository.getCurrentUser().subscribeOn(Schedulers.io()).blockingGet().getUserId(), new Callback<ProjectDTO[]>() {
                                @Override
                                public void onResponse(Call<ProjectDTO[]> call, Response<ProjectDTO[]> response) {
                                    if (response.isSuccessful()){
                                        for (ProjectDTO projectDTO : response.body()){
                                            projectRepository.saveProjectDTO(projectDTO);
                                        }
                                    } else {
                                        //Snackbar.make(binding.getRoot(), response.message(), Snackbar.LENGTH_SHORT).show();
                                    }
                                    goToInvoiceFragment2();
                                }

                                @Override
                                public void onFailure(Call<ProjectDTO[]> call, Throwable t) {
                                    //Snackbar.make(binding.getRoot(), t.getMessage(), Snackbar.LENGTH_SHORT).show();
                                    goToInvoiceFragment2();
                                }
                            });

                        }
                    });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goToInvoiceFragment2();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }



         */
        goToInvoiceFragment2();
    }

    private void goToInvoiceFragment2(){
        sharedPreferences.edit().putInt(getResources().getString(R.string.lastDayLogin), todayNum).commit();
        NavHostFragment.findNavController(LoginF.this).navigate(LoginFDirections.actionLoginFToInvoiceFragment());
    }

    private void initListeners() {
        binding.btnLogin.setOnClickListener((v) -> {
            if (binding.txtPassword.getText().toString().isEmpty() || binding.txtUser.getText().toString().isEmpty()) {
                Snackbar.make(binding.getRoot(), "Necesita rellenar todos los campos", Snackbar.LENGTH_SHORT).show();
                return;
            }

            UserRequestDTO userRequestDTO = new UserRequestDTO(binding.txtUser.getText().toString(), binding.txtPassword.getText().toString());
            blockInput();
            userRepository.Authenticate(userRequestDTO, new Callback<UserResponseDTO>() {
                @Override
                public void onResponse(Call<UserResponseDTO> call, Response<UserResponseDTO> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            goToInvoiceFragment();
                            insertUserFromWeb(response.body());
                        }
                        Snackbar.make(binding.getRoot(), response.body().getErrorMessage(), Snackbar.LENGTH_SHORT).show();

                    } else {
                        Snackbar.make(binding.getRoot(), "hubo un error", Snackbar.LENGTH_SHORT).show();
                    }
                    enableInput();
                }

                @Override
                public void onFailure(Call<UserResponseDTO> call, Throwable t) {
                    offlineLogin(userRequestDTO);
                    enableInput();
                }
            });
        });
    }

    private boolean offlineLogin(UserRequestDTO userRequestDTO) {
        boolean isSuccess = false;
        UserEntity loggedUser = null;

        try {
            loggedUser = userRepository.getCurrentUser().subscribeOn(Schedulers.io()).blockingGet();
        } catch (Exception e) {
            Snackbar.make(binding.getRoot(), "Necesita acceso a internet para hacer un login inicial", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (loggedUser.getUsername().equals(userRequestDTO.getUser()) && loggedUser.getPasswordHash().equals(userRequestDTO.getPasswordHash())) {
            isSuccess = true;
            goToInvoiceFragment();
        } else if (loggedUser.getUsername().equals(userRequestDTO.getUser())) {
            Snackbar.make(binding.getRoot(), "La contrasena para ese usuario no esta correcta", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(binding.getRoot(), "No hay accesso a internet.", Snackbar.LENGTH_SHORT).show();
        }

        return isSuccess;
    }

    private void insertUserFromWeb(UserResponseDTO userResponseDTO) {
        UserEntity userEntity = new UserEntity(userResponseDTO.getUser());

        userRepository.deleteAllUsers().subscribeOn(Schedulers.io()).blockingGet();

        userRepository.insertUserEntity(userEntity).subscribeOn(Schedulers.io()).blockingGet();
    }

    private void blockInput() {
        binding.txtPassword.setEnabled(false);
        binding.txtUser.setEnabled(false);
        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setAlpha(0.5f);
        binding.progressBarLoading.getRoot().setVisibility(View.VISIBLE);
    }

    private void enableInput() {
        binding.txtPassword.setEnabled(true);
        binding.txtUser.setEnabled(true);
        binding.btnLogin.setEnabled(true);
        binding.btnLogin.setAlpha(1.0f);
        binding.progressBarLoading.getRoot().setVisibility(View.INVISIBLE);
    }
}
