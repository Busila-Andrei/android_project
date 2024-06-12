package com.example.frontend.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.data.CategoryRepository;
import com.example.frontend.data.database.AppDatabase;
import com.example.frontend.data.database.Category;
import com.example.frontend.utils.SectionItem;
import com.example.frontend.adapters.SectionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsFragment extends Fragment {

    private static final String TAG = "SectionsFragment";
    private AppDatabase db;
    private CategoryRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections, container, false);
        RecyclerView sectionsRecyclerView = view.findViewById(R.id.sectionsRecyclerView);
        sectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = AppDatabase.getDatabase(getActivity());
        repository = new CategoryRepository(db);

        fetchCategoriesAndSubcategories(sectionsRecyclerView);
        return view;
    }

    private void fetchCategoriesAndSubcategories(RecyclerView recyclerView) {
        repository.fetchAndStoreCategoriesAndSubcategories(() -> displayCategories(recyclerView));
    }

    private void displayCategories(RecyclerView recyclerView) {
        repository.getCategoriesFromDatabase(categories -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    List<SectionItem> items = new ArrayList<>();
                    for (Category category : categories) {
                        items.add(new SectionItem(category.name, category.id));
                        Log.d(TAG, "Added SectionItem: " + category.name + " with ID: " + category.id);
                    }
                    recyclerView.setAdapter(new SectionsAdapter(items, db));
                    Log.d(TAG, "Set adapter with items: " + items);
                });
            }
        });
    }

    private void showToast(String message) {
        if (isAdded() && getActivity() != null) {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show());
        }
    }
}
