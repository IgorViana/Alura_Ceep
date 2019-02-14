package com.example.android.ceep.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.android.ceep.R;

public class LaucherActivity extends AppCompatActivity {
    public static final String OPENNED = "ja_abriu";
    private final long tempoPrimeiraAbertura = 2000;
    private final long tempoOutrasAberturas = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher_);

        SharedPreferences preferences = getSharedPreferences("com.example.android.ceep.ui.activity.LaucherActivity", MODE_PRIVATE);
        if (preferences.contains(OPENNED)) {
            delaySplashScreen(tempoOutrasAberturas);
        } else {
            marcarComoAberto(preferences);
            delaySplashScreen(tempoPrimeiraAbertura);
        }
    }

    private void marcarComoAberto(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(OPENNED, true);
        editor.apply();
    }

    private void delaySplashScreen(long tempo) {
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarListaNotas();
            }
        }, tempo);
    }

    private void mostrarListaNotas() {
        Intent intent = new Intent(LaucherActivity.this, ListaNotasActivity.class);
        startActivity(intent);
    }
}
