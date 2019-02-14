package com.example.android.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.ceep.R;
import com.example.android.ceep.modelo.Nota;
import com.example.android.ceep.ui.recycler.ListaCoresAdapter;
import com.example.android.ceep.ui.recycler.OnColorItemClick;

import java.util.Arrays;
import java.util.List;

public class FormularioNotaActivity extends AppCompatActivity implements OnColorItemClick {

    public static final String NOTA = "nota";
    public static final int CODIGO_RESULTADO = 1002;
    public static final String POSICAO = "posicao";
    public static final int POSICAO_INVALIDA = -1;
    public static final String TITULO_APP_BAR_INSERE = "Insere nota";
    public static final String TITULO_APP_BAR_ALTERA = "Altera nota";
    public static final String STATE_NOTA_COR = "notaCor";
    private int posicaoNota = POSICAO_INVALIDA;

    private TextView titulo;
    private TextView descricao;
    private View parentView;

    private int cor;
    Nota notaatual;

    private RecyclerView listaCores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        setTitle(TITULO_APP_BAR_INSERE);
        inicializaCampos();
        if (savedInstanceState != null) {
            setParentViewBg(savedInstanceState.getInt(STATE_NOTA_COR));
        }
        Intent intentRecebida = getIntent();
        if (intentRecebida.hasExtra(NOTA)) {
            setTitle(TITULO_APP_BAR_ALTERA);
            notaatual = intentRecebida.getExtras().getParcelable(NOTA);
            posicaoNota = notaatual.getPosicao();
            titulo.setText(notaatual.getTitulo());
            descricao.setText(notaatual.getDescricao());
            setParentViewBg(notaatual.getCor());
        }
        RecyclertViewConfig();
    }

    private void RecyclertViewConfig() {
        List<Integer> cores = Arrays.asList(getResources().getColor(R.color.cor_branco), getResources().getColor(R.color.cor_azul),
                getResources().getColor(R.color.cor_amarelo), getResources().getColor(R.color.cor_vermelho),
                getResources().getColor(R.color.cor_verde), getResources().getColor(R.color.cor_lilas),
                getResources().getColor(R.color.cor_cinza), getResources().getColor(R.color.cor_marrom),
                getResources().getColor(R.color.cor_roxo));
        ListaCoresAdapter adapter = new ListaCoresAdapter(this, cores, this);
        listaCores.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_nota_salvar:
                setCurrentValuesToNota();
                retornaNota(notaatual);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(NOTA, nota);
        resultadoInsercao.putExtra(POSICAO, nota.getPosicao());
        setResult(CODIGO_RESULTADO, resultadoInsercao);
    }

    private void inicializaCampos() {
        notaatual = new Nota();
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
        listaCores = findViewById(R.id.listaCores);
        parentView = findViewById(R.id.layout_parent);
        cor = getResources().getColor(R.color.cor_branco);
    }

    private void setCurrentValuesToNota() {
        notaatual = new Nota(notaatual.getId(), titulo.getText().toString(), descricao.getText().toString(), cor, notaatual.getPosicao());
    }

    @Override
    public void onColorClick(int cor) {
        setParentViewBg(cor);
    }

    private void setParentViewBg(int cor) {
        this.cor = cor;
        parentView.setBackgroundColor(cor);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current selected color
        savedInstanceState.putInt(STATE_NOTA_COR, cor);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

}
