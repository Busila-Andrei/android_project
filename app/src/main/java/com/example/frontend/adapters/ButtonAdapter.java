package com.example.frontend.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.NewActivity;
import com.example.frontend.R;

import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {

    private List<String> subcategories;
    private Context context;

    public ButtonAdapter(Context context, List<String> subcategories) {
        this.context = context;
        this.subcategories = subcategories;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item_button, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        String subcategory = subcategories.get(position);
        holder.button.setText(subcategory);

        holder.button.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewActivity.class);
            intent.putExtra("SUBCATEGORY_NAME", subcategory);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        AppCompatButton button;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }
    }
}
