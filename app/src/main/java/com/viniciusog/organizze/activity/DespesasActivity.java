package com.viniciusog.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.viniciusog.organizze.R;
import com.viniciusog.organizze.helper.DateCustom;
import com.viniciusog.organizze.model.Movimentacao;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);

        //Preenche campo data com a data atual
        campoData.setText(DateCustom.dataAtual());
    }

    public void salvarDespesa(View view) {
        movimentacao = new Movimentacao();

        String data = campoData.getText().toString();
        movimentacao.setValor(Double.parseDouble(campoValor.getText().toString()));
        movimentacao.setCategoria(campoCategoria.getText().toString());
        movimentacao.setData( data );
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setTipo("d");

        movimentacao.salvar( data );

    }
}
