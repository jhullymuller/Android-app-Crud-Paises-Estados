package com.senac.renato;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.senac.renato.control.GerenciarPaisesControl;

public class EditarPaises extends AppCompatActivity {
    private EditText editNomePais;
    private ListView listaPaises;
    private Button btnSalvar;
    private GerenciarPaisesControl control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_paises);
        initialize();
        control = new GerenciarPaisesControl(this);
    }
        private void initialize(){
        editNomePais = findViewById(R.id.editNomePais);
        listaPaises = findViewById(R.id.listaPaises);
        btnSalvar = findViewById(R.id.botaoSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.salvarAcao();
            }
        });


    }

    public ListView getListaPaises(){
        return listaPaises;
    }

    public EditText getEditNomePais() {
        return editNomePais;
    }

    public void setEditNomePais(EditText editNomePais) {
        this.editNomePais = editNomePais;
    }

    public void setListaPaises(ListView listaPaises) {
        this.listaPaises = listaPaises;
    }

    public Button getBtnSalvar() {
        return btnSalvar;
    }

    public void setBtnSalvar(Button btnSalvar) {
        this.btnSalvar = btnSalvar;
    }
}
