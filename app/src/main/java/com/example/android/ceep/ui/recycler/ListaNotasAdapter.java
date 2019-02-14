package com.example.android.ceep.ui.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.ceep.R;
import com.example.android.ceep.dao.NotaDAO;
import com.example.android.ceep.modelo.Nota;

import java.util.Collections;
import java.util.List;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    private final List<Nota> notas;
    private final Context context;
    private OnItemListaClick onItemListaClick;

    public ListaNotasAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }

    public void setOnItemListaClickListener(OnItemListaClick onItemListaClick) {
        this.onItemListaClick = onItemListaClick;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, viewGroup, false);
        return new NotaViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder viewHolder, int i) {
        Nota nota = notas.get(i);
        viewHolder.vincula(nota);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    class NotaViewHolder extends RecyclerView.ViewHolder {
        private final TextView titulo;
        private final TextView descricao;
        private final CardView cardViewParent;
        private Nota nota;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
            cardViewParent = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemListaClick.onItemClick(nota);
                }
            });

        }

        public void vincula(Nota nota) {
            this.nota = nota;
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
            cardViewParent.setCardBackgroundColor(nota.getCor());
        }
    }

    public void adiciona(Nota nota) {
        notas.add(0, nota);
        salvarPosicaoLista();
        notifyDataSetChanged();
    }

    public void alterar(Nota nota) {
        notas.set(nota.getPosicao(), nota);
        notifyDataSetChanged();
    }

    public void remove(int posicao) {
        notas.remove(posicao);
        salvarPosicaoLista();
        notifyItemRemoved(posicao);
    }

    public void troca(int posicaoInicial, int posicaoFinal) {
        Collections.swap(notas, posicaoInicial, posicaoFinal);
        salvarPosicaoLista();
        notifyItemMoved(posicaoInicial, posicaoFinal);
    }

    public void salvarPosicaoLista(){
        NotaDAO notaDAO = new NotaDAO(context);
        Nota nota;

        for (int i = 0; i < notas.size(); i++){
            nota = notas.get(i);
            nota.setPosicao(i);
            notaDAO.alterarPosicaoNota(nota);
        }
    }

    public List<Nota> getNotas() {
        return notas;
    }
}
