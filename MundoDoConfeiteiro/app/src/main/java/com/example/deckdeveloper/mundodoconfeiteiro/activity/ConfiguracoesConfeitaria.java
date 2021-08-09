package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.UserFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Confeitaria;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class ConfiguracoesConfeitaria extends AppCompatActivity {

    private EditText nome_confeitaria, tempo_confeitaria, taxa_confeitaria, categoria_confeitaria;
    private ImageView imageConfeitaria;

    private static final int SELECAO_GALERIA = 200;

    private StorageReference storageReference;
    private DatabaseReference firebaseRef;

    private String IDUser;
    private String URL_Img_Selecionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_confeitaria);

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inicializar Componentes
        Inicializar();
        storageReference = ConfiguracaoFireBase.getStorage();
        firebaseRef = ConfiguracaoFireBase.getFireBase();
        IDUser = UserFireBase.getIdUser();

        imageConfeitaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        //Recuperar Dados Da Confeitaria
        RecuperarDadosConfeitaria();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {
                switch (requestCode){
                    case SELECAO_GALERIA :
                        Uri local_imagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(
                                getContentResolver(),
                                local_imagem);
                        break;
                }
            if(imagem != null){
                imageConfeitaria.setImageBitmap(imagem);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] dadosDaImagem =  baos.toByteArray();

                StorageReference imgRef = storageReference
                        .child("imagens")
                        .child("Confeitarias")
                        .child(IDUser + "jpeg");
                UploadTask uploadTask = imgRef.putBytes(dadosDaImagem);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                ConfiguracoesConfeitaria.this,
                                "Erro Ao Fazer Upload Da Imagem",
                                Toast.LENGTH_SHORT
                                ).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri = task.getResult();
                                URL_Img_Selecionada = uri.toString();
                            }
                        });

                        Toast.makeText(
                                ConfiguracoesConfeitaria.this,
                                "Sucesso Ao Fazer Upload Da Imagem",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

            }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void Inicializar(){
        nome_confeitaria = findViewById(R.id.nome_confeitaria);
        tempo_confeitaria = findViewById(R.id.tempo_confeitaria);
        taxa_confeitaria = findViewById(R.id.taxa_confeitaria);
        categoria_confeitaria = findViewById(R.id.categoria_confeitaria);
        imageConfeitaria = findViewById(R.id.imagePerfil);
    }

    private void RecuperarDadosConfeitaria(){
        DatabaseReference consfeitariaRef = firebaseRef
                .child("Confeitarias")
                .child(IDUser);
        consfeitariaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    Confeitaria confeitaria = snapshot.getValue(Confeitaria.class);
                    nome_confeitaria.setText(confeitaria.getNome());
                    categoria_confeitaria.setText(confeitaria.getCategoria());
                    tempo_confeitaria.setText(confeitaria.getTempo());
                    taxa_confeitaria.setText(confeitaria.getTaxa().toString());
                    URL_Img_Selecionada = confeitaria.getURLImage();

                    if(!URL_Img_Selecionada.equals("")){
                        Picasso.get().load(URL_Img_Selecionada).into(imageConfeitaria);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ValidarDadosConfeitaria(View view){

        //Validar Se Os Campos Foram Preenchidos
        String nome = nome_confeitaria.getText().toString();
        String tempo = tempo_confeitaria.getText().toString();
        String taxa = taxa_confeitaria.getText().toString();
        String categoria = categoria_confeitaria.getText().toString();
        String imagen = URL_Img_Selecionada;

        if(!nome.isEmpty()){
            if(!tempo.isEmpty()){
                if(!categoria.isEmpty()){
                    if(!taxa.isEmpty()){
                        if(!imagen.isEmpty()){
                            Confeitaria confeitaria = new Confeitaria();
                            confeitaria.setIdUsuario(IDUser);
                            confeitaria.setNome(nome);
                            confeitaria.setCategoria(categoria);
                            confeitaria.setTempo(tempo);
                            confeitaria.setTaxa(Double.parseDouble(taxa));
                            confeitaria.setURLImage(URL_Img_Selecionada);
                            confeitaria.Salvar();
                            finish();
                        }else{
                            ExibirMensagem("Adicione Uma Imagen");
                        }
                    }else{
                        ExibirMensagem("Digite Uma Taxa Media");
                    }
                }else{
                    ExibirMensagem("Digite Uma Categoria");
                }
            }else{
                ExibirMensagem("Digite Um Tempo Medio Para A Entrega");
            }
        }else{
            ExibirMensagem("Digite Um Nome Para A Confeitaria");
        }
    }
    private void ExibirMensagem(String Texto){
        Toast.makeText(
                ConfiguracoesConfeitaria.this,
                Texto,
                Toast.LENGTH_SHORT
        ).show();
    }
}
