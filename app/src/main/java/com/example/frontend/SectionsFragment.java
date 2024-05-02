package com.example.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SectionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections, container, false);
        RecyclerView sectionsRecyclerView = view.findViewById(R.id.sectionsRecyclerView);
        sectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<SectionItem> items = new ArrayList<>();
        // Adăugați exemple de fragmente și text
        items.add(new SectionItem("Text pentru secțiunea 1", new SectionOneFragment()));
        items.add(new SectionItem("Text pentru secțiunea 2", new SectionTwoFragment()));

        sectionsRecyclerView.setAdapter(new SectionsAdapter(getChildFragmentManager(), items));
        return view;
    }
}
