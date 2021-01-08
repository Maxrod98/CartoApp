package com.ecarto.cartoapp.database.Repositories;

import android.content.Context;

import com.ecarto.cartoapp.database.DAOs.UserDAO;
import com.ecarto.cartoapp.database.DatabaseClass;
import com.ecarto.cartoapp.database.Entities.UserEntity;

import io.reactivex.Single;

public class UserRepository {
    private UserDAO userDAO;

    public UserRepository(Context context){
        DatabaseClass db = DatabaseClass.getDatabase(context);
        userDAO = db.userDAO();
    }

    public Single<Long> insertUserEntity(UserEntity userEntity){
        return userDAO.addUserEntity(userEntity);
    }

    public Single<Integer> deleteAllUsers(){
        return userDAO.deleteAllUsers();
    }

    public Single<Integer> deleteUser(UserEntity userEntity){
        return userDAO.deleteUserEntity(userEntity);
    }

    public Single<UserEntity> getCurrentUser(){
        return userDAO.getCurrentUser();
    }
}
