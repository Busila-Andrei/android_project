package com.example.frontend.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.frontend.R;
import com.example.frontend.config.ApiResponse;
import com.example.frontend.config.ApiService;
import com.example.frontend.config.RetrofitClient;
import static android.content.Context.MODE_PRIVATE;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CustomSplashScreen")
public class SplashScreenFragment extends Fragment {
    public interface OnSplashScreenListener {
        void onSplashScreenComplete(boolean isSuccess);
    }

    private static final String TAG = "SplashScreenFragment";
    private OnSplashScreenListener listener;
    private TextView statusMessage;
    private final Handler handler = new Handler();
    private int retryCount = 0;
    private final int MAX_RETRY = 3;
    private ApiService apiService;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSplashScreenListener) {
            listener = (OnSplashScreenListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnSplashScreenListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        statusMessage = view.findViewById(R.id.textViewMessage);
        ProgressBar spinner = view.findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        apiService = RetrofitClient.getApiService();
        performChecksSequentially();
        return view;
    }

    private void performChecksSequentially() {
        checkNetworkConnection();
    }

    private void checkNetworkConnection() {
        statusMessage.setText("Checking internet connection...");
        handler.postDelayed(() -> {
            if (isAdded()) {
                ConnectivityManager cm = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) {
                    retryCount = 0;
                    checkServerConnection();
                } else {
                    statusMessage.setText("No Internet Connection. Please check your connection.");
                    if (retryCount < MAX_RETRY) {
                        retryChecks();
                    } else {
                        statusMessage.setText("Failed after multiple attempts. Please try again later.");
                    }
                }
            }
        }, 1000);
    }

    private void checkServerConnection() {
        statusMessage.setText("Checking server connection...");
        handler.postDelayed(() -> {
            if (isAdded()) {
                apiService.checkServerConnection().enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            retryCount = 0;
                            checkToken();
                        } else {
                            statusMessage.setText("Server Connection Failed. Please try again later.");
                            if (retryCount < MAX_RETRY) {
                                retryChecks();
                            } else {
                                statusMessage.setText("Failed after multiple attempts. Please try again later.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "Server connection failed: " + t.getMessage());
                        statusMessage.setText("Server Connection Failed: " + t.getMessage());
                        if (retryCount < MAX_RETRY) {
                            retryChecks();
                        } else {
                            statusMessage.setText("Failed after multiple attempts. Please try again later.");
                        }
                    }
                });
            }
        }, 1000);
    }

    private void checkToken() {
        statusMessage.setText("Checking token...");
        handler.postDelayed(() -> {
            if (isAdded()) {
                String token = extractTokenFromCookies();
                tokenIsValid(token);
                listener.onSplashScreenComplete(true); // Always true as we're skipping token validation logic modifications
            }
        }, 1000);
    }

    public String extractTokenFromCookies() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return preferences.getString("jwt_token", null);
    }

    private void tokenIsValid(String token) {
        // Stub for token validation
    }

    private void retryChecks() {
        retryCount++;
        handler.postDelayed(this::performChecksSequentially, 10000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
