package com.ecarto.cartoapp.web.Services;

import com.ecarto.cartoapp.web.DTOs.UserResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizationService {
    @POST("authenticate")
    Call<UserResponseDTO> authenticate(@Body UserRequestDTO userMessageSendDTO);
}
