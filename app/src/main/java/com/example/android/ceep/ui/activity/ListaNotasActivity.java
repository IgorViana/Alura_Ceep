package com.example.android.ceep.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.ceep.R;
import com.example.android.ceep.dao.NotaDAO;
import com.example.android.ceep.modelo.Nota;
import com.example.android.ceep.ui.enums.EnumModes;
import com.example.android.ceep.ui.recycler.ListaNotasAdapter;
import com.example.android.ceep.ui.recycler.OnItemListaClick;
import com.example.android.ceep.ui.recycler.helper.callback.NotaItemTouchHelperCallback;

import java.util.List;

public class ListaNotasActivity extends AppCompatActivity {

    public static final int CODIGO_REQUISICAO = 1001;
    public static final int CODIGO_REQUISICAO_LISTA = 101;
    public static final int CODIGO_RESPOSTA = 1002;
    public static final String NOTA = "nota";
    public static final String POSICAO = "posicao";
    public static final String TITULO_APP_BAR = "Notas";
    public static final int SPAN_COUNT = 2;
    public static final String KEY_MANAGER_PREFERENCE = "lista_manager";
    private ListaNotasAdapter adapter;
    private RecyclerView listaNotas;

    private EnumModes tiposModos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        setTitle(TITULO_APP_BAR);

        List<Nota> todasNotas = pegaTodasNotas();
        listaNotas = findViewById(R.id.lista_notas_recyclerview);

        tiposModos = EnumModes.buscaEnum(recuperaEstadoAnteriorDoManager());

        configuraRecyclerView(todasNotas);
        abrirFormularioNota();
    }

    @Override
    protected void onStop() {
        salvaEstadoAtualDoManager(tiposModos.getMode());
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CODIGO_REQUISICAO && resultCode == CODIGO_RESPOSTA && data.hasExtra(NOTA)) {
            Nota nota = data.getExtras().getParcelable(NOTA);
            long id = new NotaDAO(this).inserirNota(nota);
            nota.setPosicao(0);
            nota.setId(id);
            adapter.adiciona(nota);
        }

        if (requestCode == CODIGO_REQUISICAO_LISTA && resultCode == CODIGO_RESPOSTA && data.hasExtra(NOTA) && data.hasExtra(POSICAO)) {
            Nota nota = data.getExtras().getParcelable(NOTA);
            int posicao = data.getExtras().getInt(POSICAO);
            if (posicao > -1) {
                new NotaDAO(this).alterarNota(nota);
                adapter.alterar(nota);
            } else {
                Toast.makeText(this, "Ocorreu um problema na alteração da nota", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_lista_layout);
        mudarMenuIcon(menuItem);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_lista_layout:
                tiposModos = tiposModos.move();
                mudarMenuIcon(item);
                atualizaRecyclerViewManager();
            case R.id.menu_lista_feedback:
                Intent intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO notasdao = new NotaDAO(this);
        String[] projection = {
                NotaDAO.TABLE_ID,
                NotaDAO.TABLE_TITULO,
                NotaDAO.TABLE_DESCRICAO,
                NotaDAO.TABLE_COR,
                NotaDAO.TABLE_POSICAO,
        };
        return notasdao.buscarNotas(projection, null, null, null);
    }

    private void abrirFormularioNota() {
        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                startActivityForResult(intent, CODIGO_REQUISICAO);
            }
        });
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        onListClick();
        listaNotas.setAdapter(adapter);
        atualizaRecyclerViewManager();
        animationHelper(listaNotas);
    }

    private void onListClick() {
        adapter.setOnItemListaClickListener(new OnItemListaClick() {
            @Override
            public void onItemClick(Nota nota) {
                Intent intentPassagem = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                intentPassagem.putExtra(NOTA, nota);
                intentPassagem.putExtra(POSICAO, nota.getPosicao());
                startActivityForResult(intentPassagem, CODIGO_REQUISICAO_LISTA);
            }
        });
    }

    public void animationHelper(RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(this,adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Metodo que estipula qual tipo de layout manager será usado no RecyclerView
     */
    private void atualizaRecyclerViewManager() {
        switch (tiposModos) {
            case MODE_LIST:
                mudarParaLinearLayout();
                break;
            case MODE_GRID:
                mudarParaStaggeredGrid();
                break;
        }
    }

    private void mudarMenuIcon(MenuItem item) {
        switch (tiposModos) {
            case MODE_LIST:
                item.setIcon(R.drawable.ic_layout_grid);
                break;
            case MODE_GRID:
                item.setIcon(R.drawable.ic_layout_linear);
                break;
        }
    }

    /**
     * Metodo responsavel por mudar o icone do menu
     * e mudar o Layout manager do recycler view para StaggeredGrid Layout
     */
    private void mudarParaStaggeredGrid() {
        StaggeredGridLayoutManager gridLayout = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        listaNotas.setLayoutManager(gridLayout);
    }

    /**
     * Metodo responsavel por mudar o icone do menu
     * e mudar o Layout manager do recycler view para Linear Layout
     */
    private void mudarParaLinearLayout() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listaNotas.setLayoutManager(layoutManager);
    }

    private void salvaEstadoAtualDoManager(int mode) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.ceep.ui.activity.ListaNotasActivity", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(KEY_MANAGER_PREFERENCE, mode);
        edit.apply();
    }

    private int recuperaEstadoAnteriorDoManager() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.android.ceep.ui.activity.ListaNotasActivity", MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_MANAGER_PREFERENCE, 1);
    }
}
