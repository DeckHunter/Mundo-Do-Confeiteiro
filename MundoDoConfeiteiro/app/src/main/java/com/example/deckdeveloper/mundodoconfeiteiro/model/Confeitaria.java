package com.example.deckdeveloper.mundodoconfeiteiro.model;

import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.google.firebase.database.DatabaseReference;

public class Confeitaria {
    private String IdUsuario;
    private String URLImage;
    private Double Taxa;
    private String Tempo;
    private String Categoria;
    private String Nome;

    public Confeitaria() {

    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getURLImage() {
        return URLImage;
    }

    public void setURLImage(String URLImage) {
        this.URLImage = URLImage;
    }

    public Double getTaxa() {
        return Taxa;
    }

    public void setTaxa(Double taxa) {
        Taxa = taxa;
    }

    public String getTempo() {
        return Tempo;
    }

    public void setTempo(String tempo) {
        Tempo = tempo;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public void Salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference confeitariaRef = firebaseRef
                .child("Confeitarias")
                .child(getIdUsuario());
        confeitariaRef.setValue(this);
    }
}
