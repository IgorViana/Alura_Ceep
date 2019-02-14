package com.example.android.ceep.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Nota implements Parcelable {
    private long id;
    private String titulo;
    private String descricao;
    private int cor;
    private int posicao;

    public Nota() {
    }

    public Nota(long id, String titulo, String descricao, int cor, int posicao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.cor = cor;
        this.posicao = posicao;
    }

    protected Nota(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        descricao = in.readString();
        cor = in.readInt();
        posicao = in.readInt();
    }

    public static final Creator<Nota> CREATOR = new Creator<Nota>() {
        @Override
        public Nota createFromParcel(Parcel in) {
            return new Nota(in);
        }

        @Override
        public Nota[] newArray(int size) {
            return new Nota[size];
        }
    };

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCor(){ return cor; }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(descricao);
        dest.writeInt(cor);
        dest.writeInt(posicao);
    }
}
