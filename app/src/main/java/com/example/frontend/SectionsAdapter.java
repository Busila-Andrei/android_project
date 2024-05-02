package com.example.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.SectionViewHolder> {
    private List<SectionItem> items;
    private FragmentManager fragmentManager;

    public SectionsAdapter(FragmentManager fragmentManager, List<SectionItem> items) {
        this.fragmentManager = fragmentManager;
        this.items = items;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        SectionItem item = items.get(position);
        holder.textView.setText(item.getText());
        Fragment fragment = item.getFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(holder.frameLayout.getId(), fragment);
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        FrameLayout frameLayout;

        SectionViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.section_text);
            frameLayout = itemView.findViewById(R.id.section_frame);
            frameLayout.setId(View.generateViewId());
        }
    }
}



