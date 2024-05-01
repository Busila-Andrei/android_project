package com.example.frontend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelViewHolder> {
    private Context context;
    private List<String> levels;

    public LevelsAdapter(Context context, List<String> levels) {
        this.context = context;
        this.levels = levels;
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_level, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LevelViewHolder holder, int position) {
        String level = levels.get(position);
        holder.levelButton.setText(level);

        // Adaugă un listener pentru butonul de nivel
        if (position == 0) { // Asigură-te că este primul buton
            holder.levelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    public static class LevelViewHolder extends RecyclerView.ViewHolder {
        public Button levelButton;

        public LevelViewHolder(View itemView) {
            super(itemView);
            levelButton = itemView.findViewById(R.id.levelButton);
        }
    }
}
