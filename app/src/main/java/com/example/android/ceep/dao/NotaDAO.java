package com.example.android.ceep.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.android.ceep.modelo.Nota;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NotaDAO extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ceep.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Nota";
    public static final String TABLE_ID = "id";
    public static final String TABLE_TITULO = "titulo";
    public static final String TABLE_DESCRICAO = "descricao";
    public static final String TABLE_COR = "cor";
    public static final String TABLE_POSICAO = "posicao";

    public NotaDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TABLE_TITULO + " VARCHAR (100),"
                + TABLE_DESCRICAO + " VARCHAR (500),"
                + TABLE_COR + " INTEGER NOT NULL, "
                + TABLE_POSICAO + " INTEGER NOT NULL);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            default:
                return;
        }
    }

    public long inserirNota(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = setNotasContentValues(nota);
        return db.insert(TABLE_NAME, null, values);
    }

    public void alterarNota(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = setNotasContentValues(nota);
        String sqlWhere = TABLE_ID + "=?";
        String[] args = new String[]{String.valueOf(nota.getId())};
        db.update(TABLE_NAME, values, sqlWhere, args);
    }

    public void alterarPosicaoNota(Nota nota) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = setNotasContentValuesPosicao(nota);
        String sqlWhere = TABLE_ID + "=?";
        String[] args = new String[]{String.valueOf(nota.getId())};
        int alterados = db.update(TABLE_NAME, values, sqlWhere, args);
    }

    public void deletarNota(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String sqlWhere = TABLE_ID + "=?";
        String[] args = new String[]{String.valueOf(id)};
        db.delete(TABLE_NAME, sqlWhere, args);
    }

    private ContentValues setNotasContentValues(Nota nota) {
        ContentValues values = new ContentValues();
        values.put(TABLE_TITULO, nota.getTitulo());
        values.put(TABLE_DESCRICAO, nota.getDescricao());
        values.put(TABLE_COR, nota.getCor());
        values.put(TABLE_POSICAO, nota.getPosicao());
        return values;
    }

    private ContentValues setNotasContentValuesPosicao(Nota nota) {
        ContentValues values = new ContentValues();
        values.put(TABLE_POSICAO, nota.getPosicao());
        return values;
    }

    public List<Nota> buscarNotas(@Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, TABLE_POSICAO);
        List<Nota> notas = new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(TABLE_ID));
            String titulo = cursor.getString(cursor.getColumnIndex(TABLE_TITULO));
            String descricao = cursor.getString(cursor.getColumnIndex(TABLE_DESCRICAO));
            int cor = cursor.getInt(cursor.getColumnIndex(TABLE_COR));
            int posicao = cursor.getInt(cursor.getColumnIndex(TABLE_POSICAO));
            Nota nota = new Nota(id, titulo, descricao, cor, posicao);
            notas.add(nota);
        }
        cursor.close();
        return notas;
    }
}
