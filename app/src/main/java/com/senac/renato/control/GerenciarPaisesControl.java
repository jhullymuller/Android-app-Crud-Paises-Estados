package com.senac.renato.control;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.senac.renato.EditarPaises;
import com.senac.renato.model.bo.PaisBo;
import com.senac.renato.model.dao.EstadoDao;
import com.senac.renato.model.dao.PaisDao;
import com.senac.renato.model.vo.Estado;
import com.senac.renato.model.vo.Pais;
import com.senac.renato.view.MainActivity;

import java.sql.SQLException;
import java.util.List;

public class GerenciarPaisesControl  extends AppCompatActivity {

    private EditarPaises activity;
    private MainActivity mainActivity;
    private ArrayAdapter<Pais>adapterlistPais;
    private List<Pais>listPais;
    private PaisDao paisDao;
    private EstadoDao estadoDao;
    private Pais pais;

    public GerenciarPaisesControl(EditarPaises activity){
        this.activity = activity;
        paisDao = new PaisDao(this.activity);
        estadoDao = new EstadoDao(this.activity);
        configListaViewPaises();
    }
    private void configListaViewPaises() {
            listPais = paisDao.listar();
            adapterlistPais = new ArrayAdapter<>(
                    activity,
                    android.R.layout.simple_list_item_1,
                    listPais
            );
            activity.getListaPaises().setAdapter(adapterlistPais);
            addCliqueLongoListPaises();
            addcliqueCurtoListPais();
    }

    private void addCliqueLongoListPaises(){
        activity.getListaPaises().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               pais = adapterlistPais.getItem(position);
               confirmarExclusaoAction(pais);
                return true;
            }
        });
    }

        private void confirmarExclusaoAction(final Pais e){
            AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
            alerta.setTitle("Escluindo pais");
            alerta.setMessage("Todos os estados do país tambem serão removidos. Deseja realmente Excluir o pais" + e.getNome() +"?");
            alerta.setIcon(android.R.drawable.ic_menu_delete);
            alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pais = null;
                }
            });
            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        for(Estado estado : e.getCollectionEstados()) {
                            estadoDao.getDao().delete(estado);
                        }
                        paisDao.getDao().delete(e);
                        adapterlistPais.remove(e);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pais = null;
                }
            });

            alerta.show();
        }


        public void addcliqueCurtoListPais(){
            activity.getListaPaises().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   pais = adapterlistPais.getItem(position);
                    AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
                    alerta.setTitle("Paises");
                    alerta.setMessage(pais.toString());
                    alerta.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pais = null;
                        }
                    });

                    alerta.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            popularFormAction(pais);
                        }
                    });
                    alerta.show();
                }
            });
        }

     private void cadastrar(){
        Pais pais = getPaisFormulario();
        try {
            int resultado = paisDao.getDao().create(pais);
            adapterlistPais.add(pais);
            if(resultado > 0){
                Toast.makeText(activity,"Cadastrado com sucesso",Toast.LENGTH_SHORT).show();
            }else {
               Toast.makeText(activity,"Tente novamente ", Toast.LENGTH_SHORT).show();
            }
            Log.i("Testando","Cadastrou");
            Toast.makeText(activity,"Id" + pais.getId(),Toast.LENGTH_SHORT).show();
            }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void popularFormAction(Pais e){
        activity.getEditNomePais().setText(e.getNome());
    }
    private Pais getPaisFormulario(){
        Pais e = new Pais();
        e.setNome(activity.getEditNomePais().getText().toString());
        return e;
    }

    public void limparFormActin(){
        activity.getEditNomePais().setText("");
    }

    public void salvarAcao() {
        Pais paisFormulario = getPaisFormulario();
        if (PaisBo.validarNomePais(paisFormulario.getNome())) {
            if (pais == null) {
                cadastrar();
            } else {
                editar(paisFormulario);
            }
            pais = null;
            limparFormActin();
        } else {
            Toast.makeText(activity, "Preencha o campo corretamente", Toast.LENGTH_SHORT);
        }
    }

    private void editar(Pais newpais){
        pais.setNome(newpais.getNome());
        try {
            adapterlistPais.notifyDataSetChanged(); // atualiza no view
            int resultado = paisDao.getDao().update(pais);
            if(resultado> 0){
                Toast.makeText(activity,"Sucesso",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity,"tente mais tarde", Toast.LENGTH_SHORT).show();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
