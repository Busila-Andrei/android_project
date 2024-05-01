package com.example.frontend.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.frontend.*;
import com.example.frontend.config.ApiResponse;
import com.example.frontend.config.ApiService;
import com.example.frontend.config.RetrofitClient;
import com.example.frontend.dto.LoginRequest;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    public interface OnLoginListener {
        void onLoginComplete();
    }

    private OnLoginListener listener;
    private static final String TAG = "LoginFragment";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewError;
    private ApiService apiService;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginListener) {
            listener = (OnLoginListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnLoginListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        textViewError = view.findViewById(R.id.textViewError);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);

        apiService = RetrofitClient.getApiService();

        buttonLogin.setOnClickListener(v -> authenticateUser());

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void authenticateUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            textViewError.setText("Fields cannot be empty");
            textViewError.setVisibility(View.VISIBLE);
            Log.e(TAG, "authenticateUser: Email or password fields are empty.");
            return;
        }

        Log.d(TAG, "authenticateUser: Preparing authentication request for email: " + email);

        LoginRequest authRequest = new LoginRequest(email, password);
        Call<ApiResponse> call = apiService.loginAccount(authRequest);

        call.enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Authentication successful.");

                    // Save the JWT to SharedPreferences
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    ApiResponse apiResponse = response.body();
                    editor.putString("jwt_token", String.valueOf(apiResponse.getData()));
                    editor.apply();
                    listener.onLoginComplete();
                } else {
                    Log.e(TAG, "Authentication failed with status code: " + response.code());
                    textViewError.setText("Authentication failed. Status code: " + response.code());
                    textViewError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: Failed to execute login request", t);
                textViewError.setText("Login Failed: " + t.getMessage());
                textViewError.setVisibility(View.VISIBLE);
            }
        });
    }
}
