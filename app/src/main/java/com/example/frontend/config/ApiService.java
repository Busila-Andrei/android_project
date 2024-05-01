package com.example.frontend.config;

import com.example.frontend.dto.EmailRequest;
import com.example.frontend.dto.LoginRequest;
import com.example.frontend.dto.RegisterRequest;
import com.example.frontend.dto.TokenRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("/api/v1/auth/check-server-connection")
    Call<ApiResponse> checkServerConnection();

    @POST("api/v1/auth/create-account")
    Call<ApiResponse> createAccount(@Body RegisterRequest registerRequest);

    @POST("/api/v1/auth/check-enabled-account")
    Call<ApiResponse> checkEnableAccount(@Body EmailRequest emailRequest);

    @POST("api/v1/auth/resend-confirmation-email")
    Call<ApiResponse> resendConfirmationEmail(@Body EmailRequest emailRequest);

    // New registration method
    @POST("api/v1/auth/login-account")
    Call<ApiResponse> loginAccount(@Body LoginRequest loginRequest);

    @POST("api/v1/auth/verify-token")
    Call<ApiResponse> verifyToken(@Body TokenRequest tokenRequest);

    @POST("api/v1/auth/logout-account")
    Call<ApiResponse> logoutAccount(@Body String tokenJson);



}
