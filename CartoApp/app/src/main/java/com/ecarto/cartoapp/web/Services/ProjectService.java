package com.ecarto.cartoapp.web.Services;

import com.ecarto.cartoapp.web.DTOs.ProjectDTO;
import com.ecarto.cartoapp.web.DTOs.UserResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProjectService {

    @POST("uploadProject")
    Call<String> uploadProjectDTO(@Body ProjectDTO projectDTO);
}


