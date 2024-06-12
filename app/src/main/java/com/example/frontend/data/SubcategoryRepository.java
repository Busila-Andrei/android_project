package com.example.frontend.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.frontend.config.ApiResponse;
import com.example.frontend.config.RetrofitClient;
import com.example.frontend.data.database.AppDatabase;
import com.example.frontend.data.database.Subcategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubcategoryRepository {

    private static final String TAG = "SubcategoryRepository";
    private AppDatabase db;

    public SubcategoryRepository(AppDatabase db) {
        this.db = db;
    }

    public void fetchAndStoreSubcategories(int categoryId, Runnable onComplete) {
        Call<ApiResponse> call = RetrofitClient.getApiService().getSubcategories(categoryId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Object data = response.body().getData();
                    Gson gson = new Gson();
                    Type subcategoryListType = new TypeToken<ArrayList<Subcategory>>() {}.getType();
                    List<Subcategory> subcategories = gson.fromJson(gson.toJson(data), subcategoryListType);

                    Log.d(TAG, "Subcategories data from API: " + subcategories);

                    if (subcategories == null || subcategories.isEmpty()) {
                        Log.d(TAG, "API returned empty subcategories list for categoryId: " + categoryId);
                        onComplete.run(); // Call onComplete even if the list is empty
                        return;
                    }

                    for (Subcategory subcategory : subcategories) {
                        subcategory.categoryId = categoryId;
                        Log.d(TAG, "Fetched subcategory from API: " + subcategory);
                    }

                    new Thread(() -> {
                        try {
                            db.subcategoryDao().insertAll(subcategories);
                            Log.d(TAG, "Inserted subcategories into database: " + subcategories);
                            List<Subcategory> insertedSubcategories = db.subcategoryDao().getSubcategoriesByCategoryId(categoryId);
                            Log.d(TAG, "Verified inserted subcategories: " + insertedSubcategories);
                        } catch (Exception e) {
                            Log.e(TAG, "Error inserting subcategories: " + e.getMessage());
                        }
                        onComplete.run();
                    }).start();
                } else {
                    Log.d(TAG, "Response not successful or body is null");
                    onComplete.run();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "API call failed: " + t.getMessage());
                onComplete.run();
            }
        });
    }

    public void getSubcategoriesFromDatabase(int categoryId, DatabaseCallback callback) {
        new Thread(() -> {
            List<Subcategory> subcategories = db.subcategoryDao().getSubcategoriesByCategoryId(categoryId);
            Log.d(TAG, "Read subcategories from database: " + subcategories);
            callback.onResult(subcategories);
        }).start();
    }

    public interface DatabaseCallback {
        void onResult(List<Subcategory> subcategories);
    }
}
