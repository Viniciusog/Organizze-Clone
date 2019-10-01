package com.viniciusog.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.viniciusog.organizze.R;
import com.viniciusog.organizze.config.ConfiguracaoFirebase;
import com.viniciusog.organizze.helper.Base64Custom;
import com.viniciusog.organizze.helper.DateCustom;
import com.viniciusog.organizze.model.Movimentacao;
import com.viniciusog.organizze.model.Usuario;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);

        //Preenche campo data com a data atual
        campoData.setText(DateCustom.dataAtual());
        recuperarReceitaTotal();
    }

    public void salvarReceita(View view) {
        if (validarCamposReceita()) {
            movimentacao = new Movimentacao();

            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setData(data);
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setTipo("r");

            Double receitaAtualizada = receitaTotal + valorRecuperado;
            //Atualizar receita no banco
            atualizarReceita(receitaAtualizada);
            //Salvar a movimentação no banco de dados
            movimentacao.salvar(data);

            //Fecha a tela (activity)
            finish();

        }
    }

    public boolean validarCamposReceita() {

        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        String textoValor = campoValor.getText().toString();

        if (!textoValor.isEmpty()) {
            if (!textoCategoria.isEmpty()) {
                if (!textoDescricao.isEmpty()) {
                    if (!textoData.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(ReceitasActivity.this, "Data não foi preenchida!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ReceitasActivity.this, "Descrição não foi preenchida!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ReceitasActivity.this, "Insira uma categoria", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ReceitasActivity.this, "Valor não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperarReceitaTotal() {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Firebase vai retornar os dados em forma de um objeto da classe Usuario
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita(Double receita) {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        usuarioRef.child("receitaTotal").setValue(receita);
    }
}