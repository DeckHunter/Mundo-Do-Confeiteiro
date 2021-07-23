package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Inicializar();
        auth = ConfiguracaoFireBase.getAuth();

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Confeitarias");
        setSupportActionBar(toolbar);
    }

    private void Inicializar() {
        searchView = findViewById(R.id.materialSearchView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        //Configurar Botao De Pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.Sair :
                DeslogarUsuario();
                break;
            case R.id.menuConfiguracoes :
                AbriConfiguracoes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AbriConfiguracoes() {
        startActivity(new Intent(HomeActivity.this, ConfiguracoesUsuario.class));
    }

    private void DeslogarUsuario() {
        try {
            auth.signOut();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
