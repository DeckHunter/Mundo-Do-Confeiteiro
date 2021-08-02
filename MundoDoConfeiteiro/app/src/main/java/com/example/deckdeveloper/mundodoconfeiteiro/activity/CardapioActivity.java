package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.adapter.AdapterConfeitaria;
import com.example.deckdeveloper.mundodoconfeiteiro.adapter.AdapterProduto;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Confeitaria;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardapioActivity extends AppCompatActivity {

    private ImageView imageConfeitariaCardapio;
    private TextView nomeconfeitariaCardapio;
    private RecyclerView recyclerCardapio;
    private Confeitaria confeitariaSelecionada;

    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;

    private String IdConfeitaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        //Inicializar Componentes
        Inicializar();
        firebaseRef = ConfiguracaoFireBase.getFireBase();

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardapio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar Empresa Selecionada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            confeitariaSelecionada = (Confeitaria) bundle.getSerializable("confeitaria");
            nomeconfeitariaCardapio.setText(confeitariaSelecionada.getNome());

            IdConfeitaria = confeitariaSelecionada.getIdUsuario();

            String url = confeitariaSelecionada.getURLImage();
            Picasso.get().load(url).into(imageConfeitariaCardapio);

        }

        //Configurar recyclerView
        recyclerCardapio.setLayoutManager(new LinearLayoutManager(this));
        recyclerCardapio.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos,this);
        recyclerCardapio.setAdapter(adapterProduto);

        RecuperarProdutos();
    }

    private void Inicializar() {
        imageConfeitariaCardapio = findViewById(R.id.imageConfeitariaCardapio);
        nomeconfeitariaCardapio = findViewById(R.id.textNomeConfeitariaCardapio);
        recyclerCardapio = findViewById(R.id.recyclerCardapio);
    }
    private void RecuperarProdutos() {
        DatabaseReference produtosRef = firebaseRef
                .child("Produtos")
                .child(IdConfeitaria);
        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.clear();

                for(DataSnapshot ds : snapshot.getChildren()){
                    produtos.add(ds.getValue(Produto.class));
                }

                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
