package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.UserFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ConfiguracoesUsuario extends AppCompatActivity {

    private EditText nome_Usuario;
    private EditText endereco_Usuario;
    private String IdUsuario;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        //Inicializar
        Inicializar();
        IdUsuario = UserFireBase.getIdUser();
        firebaseRef = ConfiguracaoFireBase.getFireBase();

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações Usuario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        //Recuperar Dados Usuario
        RecuperarDadosUsuario();

    }

    private void RecuperarDadosUsuario() {

        DatabaseReference usuarioRef = firebaseRef
                .child("Usuarios")
                .child(IdUsuario);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    nome_Usuario.setText(usuario.getNome());
                    endereco_Usuario.setText(usuario.getEndereco());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Inicializar() {
        nome_Usuario = findViewById(R.id.editUserNome);
        endereco_Usuario = findViewById(R.id.editUserEndereco);
    }

    public void ValidarDadosUsuario(View view){
        //Validar Se Os Campos Foram Preenchidos
        String nome = nome_Usuario.getText().toString();
        String endereco = endereco_Usuario.getText().toString();

        if(!nome.isEmpty()){
            if(!endereco.isEmpty()){
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(IdUsuario);
                usuario.setNome(nome);
                usuario.setEndereco(endereco);
                usuario.Salvar();

                ExibirMensagem("Dados Atualizados Com Sucesso");
                finish();

            }else{
                ExibirMensagem("Digite Seu Endereço");
            }
        }else{
            ExibirMensagem("Digite Seu Nome");
        }
    }

    private void ExibirMensagem(String Texto){
        Toast.makeText(
                ConfiguracoesUsuario.this,
                Texto,
                Toast.LENGTH_SHORT
        ).show();
    }
}
