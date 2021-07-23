package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.deckdeveloper.mundodoconfeiteiro.R;

public class ConfiguracoesUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações Usuario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
