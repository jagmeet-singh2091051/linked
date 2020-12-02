package com.example.linked;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RViewAdapter extends RecyclerView.Adapter<RViewAdapter.RecycleViewHolder> {
    private String[] message;
    public RViewAdapter(){
        this.message = message;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder( {
        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    })
}
