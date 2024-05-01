package com.example.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.SectionViewHolder> {
    private Context context;
    private List<Section> sections;

    public SectionsAdapter(Context context, List<Section> sections) {
        this.context = context;
        this.sections = sections;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        Section section = sections.get(position);
        holder.title.setText(section.getTitle());
        holder.levelsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.levelsRecyclerView.setAdapter(new LevelsAdapter(context, section.getLevels()));
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RecyclerView levelsRecyclerView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sectionTitle);
            levelsRecyclerView = itemView.findViewById(R.id.levelsRecyclerView);
        }
    }
}
