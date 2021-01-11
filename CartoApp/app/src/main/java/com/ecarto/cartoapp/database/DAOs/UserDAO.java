package com.ecarto.cartoapp.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ecarto.cartoapp.database.Entities.UserEntity;

import io.reactivex.Single;

@Dao
public interface UserDAO {
    @Delete
    Single<Integer> deleteUserEntity(UserEntity userEntity);

    @Update
    Single<Integer> updateUserEntity(UserEntity userEntity);

    @Query("DELETE FROM UserEntity")
    Single<Integer> deleteAllUsers();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addUserEntity(UserEntity userEntity);

    @Query("SELECT * FROM UserEntity LIMIT 1")
    Single<UserEntity> getCurrentUser();


}
