package com.ecarto.cartoapp.web.Services;

import com.ecarto.cartoapp.web.DTOs.ProjectDTO;
import com.ecarto.cartoapp.web.DTOs.UserResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProjectService {
    @POST("uploadProject")
    Call<String> uploadProjectDTO(@Body ProjectDTO projectDTO);

    @GET("downloadProject/{projectID}")
    Call<ProjectDTO> downloadProjectDTO(@Path("projectID") Long projectID);
}


