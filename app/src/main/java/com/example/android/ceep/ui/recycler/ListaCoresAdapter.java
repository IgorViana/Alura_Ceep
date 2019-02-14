package com.example.android.ceep.ui.recycler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.ceep.R;

import java.util.List;

public class ListaCoresAdapter extends RecyclerView.Adapter<ListaCoresAdapter.CoresViewHolder> {

    private final Context context;
    private final List<Integer> cores;
    private final OnColorItemClick colorClick;

    public ListaCoresAdapter(Context context, List<Integer> cores, OnColorItemClick colorClick) {
        this.context = context;
        this.cores = cores;
        this.colorClick = colorClick;
    }

    @NonNull
    @Override
    public CoresViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_cor, viewGroup, false);
        return new CoresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoresViewHolder viewHolder, int i) {
        int corAtual = cores.get(i);
        viewHolder.shape.setColor(corAtual);
    }

    @Override
    public int getItemCount() {
        return cores.size();
    }

    public class CoresViewHolder extends RecyclerView.ViewHolder {
        private final View cor;
        private Drawable bgDrawable;
        private GradientDrawable shape;

        public CoresViewHolder(@NonNull View itemView) {
            super(itemView);
            cor = itemView.findViewById(R.id.layout_cor);
            bgDrawable = cor.getBackground();
            shape = (GradientDrawable) bgDrawable;

            cor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorClick.onColorClick(cores.get(getAdapterPosition()));
                }
            });
        }
    }
}
