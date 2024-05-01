package com.example.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SectionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SectionsAdapter adapter;
    private List<Section> sections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sections, container, false);

        // Initialize sections data
        initSections();

        // Setup the RecyclerView for sections
        setupRecyclerView(view);

        return view;
    }

    private void initSections() {
        sections = new ArrayList<>();

        // Create sections with levels
        Section section1 = new Section("Secțiunea 1");
        Section section2 = new Section("Secțiunea 2");
        Section section3 = new Section("Secțiunea 3");
        Section section4 = new Section("Secțiunea 4");
        Section section5 = new Section("Secțiunea 5");
        // You can add more sections if needed

        sections.add(section1);
        sections.add(section2);
        sections.add(section3);
        sections.add(section4);
        sections.add(section5);
    }

    private void setupRecyclerView(View view) {
        // Find the RecyclerView
        recyclerView = view.findViewById(R.id.sectionsRecyclerView);

        // Set the Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Create an instance of the adapter
        adapter = new SectionsAdapter(view.getContext(), sections);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }
}
