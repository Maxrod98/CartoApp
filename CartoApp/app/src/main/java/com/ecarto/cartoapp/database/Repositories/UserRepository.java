package com.ecarto.cartoapp.database.Repositories;

import android.content.Context;

import com.ecarto.cartoapp.database.DAOs.UserDAO;
import com.ecarto.cartoapp.database.DatabaseClass;
import com.ecarto.cartoapp.database.Entities.UserEntity;
import com.ecarto.cartoapp.web.DTOs.UserResponseDTO;
import com.ecarto.cartoapp.web.RetrofitInstance;
import com.ecarto.cartoapp.web.Services.AuthorizationService;
import com.ecarto.cartoapp.web.DTOs.UserRequestDTO;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRepository {
    private UserDAO userDAO;
    Context context;
    Retrofit retrofit;

    public UserRepository(Context context) {
        DatabaseClass db = DatabaseClass.getDatabase(context);
        userDAO = db.userDAO();
        this.context = context;
        retrofit = RetrofitInstance.getInstance(context).getRetrofit();
    }

    public Single<Long> insertUserEntity(UserEntity userEntity) {
        return userDAO.addUserEntity(userEntity);
    }

    public Single<Integer> updateUserEntity(UserEntity userEntity) {
        return userDAO.updateUserEntity(userEntity);
    }

    public Single<Integer> deleteAllUsers() {
        return userDAO.deleteAllUsers();
    }

    public Single<Integer> deleteUser(UserEntity userEntity) {
        return userDAO.deleteUserEntity(userEntity);
    }

    public Single<UserEntity> getCurrentUser() {
        return userDAO.getCurrentUser();
    }

    public void Authenticate(UserRequestDTO userRequestDTO, Callback<UserResponseDTO> callback) {
        AuthorizationService authorizationService = retrofit.create(AuthorizationService.class);
        Call<UserResponseDTO> callRequest = authorizationService.authenticate(userRequestDTO);
        callRequest.enqueue(callback);
    }

    public interface AuthenticationListener{
        void onFailure(Throwable t);
        void onSuccess(Response<UserResponseDTO> response);
    }
}
