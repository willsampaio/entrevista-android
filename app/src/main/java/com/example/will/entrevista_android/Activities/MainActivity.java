package com.example.will.entrevista_android.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.will.entrevista_android.ADO.PersonagemADO;
import com.example.will.entrevista_android.Classes.Personagem;
import com.example.will.entrevista_android.Classes.PersonagemAdapter;
import com.example.will.entrevista_android.Ferramentas.PostRequestFav;
import com.example.will.entrevista_android.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private int maxListSize = 30, qtdRemove = 20, idMin = -1,
            idMax = -1, qtdAdd = 10, firstVisibleItemAux;
    public static boolean carregarFav;
    private ListView listViewPersonagens;
    private ArrayList<Personagem> arrayListPersonagens;
    private PersonagemAdapter personagemAdapter;
    PersonagemADO pado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config();
        addItensFimLocal();

        try {
            PostRequestFav.favoritarExternoLista(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filtrar_favoritos_menu:
                carregarFav = !carregarFav;
                item.setChecked(carregarFav);
                recarregarLista();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(personagemAdapter.positionAux >= 0) {
            recarregarLista();
        }
    }

    private void config() {
        arrayListPersonagens = new ArrayList<Personagem>();
        listViewPersonagens = (ListView) findViewById(R.id.listViewPersonagens);
        personagemAdapter = new PersonagemAdapter(arrayListPersonagens, this);
        listViewPersonagens.setAdapter(personagemAdapter);
        pado  = new PersonagemADO(this);

        listViewPersonagens.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(arrayListPersonagens.size() == 0) {
                    return;
                }

                if(firstVisibleItem < firstVisibleItemAux){
                    addItensInicioLocal();
                    firstVisibleItemAux = firstVisibleItem;
                    return;
                }

                if((firstVisibleItem + visibleItemCount) >= (totalItemCount -3) && totalItemCount != 0)
                {
                    addItensFimLocal();
                    firstVisibleItemAux = firstVisibleItem;
                    return;
                }
            }
        });
    }

    private void recarregarLista(){
        arrayListPersonagens.clear();
        personagemAdapter.notifyDataSetChanged();
        idMax = idMin = -1;
        addItensFimLocal();
        personagemAdapter.notifyDataSetChanged();
        personagemAdapter.positionAux = -1;
    }

    private void limparLixo(char cb){
        switch (cb){
            case 'c':
                if(arrayListPersonagens.size() >= maxListSize){
                    for(int i = 0; i < qtdRemove; i++){
                        arrayListPersonagens.remove(0);
                    }
                }
                break;
            case 'b':
                if(arrayListPersonagens.size() >= maxListSize){
                    for(int i = 0; i < qtdRemove; i++){
                        arrayListPersonagens.remove(arrayListPersonagens.size() -1);
                    }
                }
                break;
        }
    }

    private void addItensInicioLocal(){
        ArrayList<Personagem> al = buscarPersonagensInicioLocal(qtdAdd, carregarFav);
        arrayListPersonagens.addAll(0, al);
        limparLixo('b');

        if(arrayListPersonagens.size() > 0) {
            idMin = arrayListPersonagens.get(0).getId();
        }
        personagemAdapter.notifyDataSetChanged();
    }

    private void addItensFimLocal(){
        ArrayList<Personagem> al = buscarPersonagensFimLocal(qtdAdd, carregarFav);
        arrayListPersonagens.addAll(al);
        limparLixo('c');

        if(arrayListPersonagens.size() > 0) {
            idMax = arrayListPersonagens.get(arrayListPersonagens.size() - 1).getId();
        }

        personagemAdapter.notifyDataSetChanged();
    }

    private ArrayList<Personagem> buscarPersonagensInicioLocal(int limit, boolean fav){
        if(idMin == 0){
            return new ArrayList<Personagem>();
        }

        return pado.buscarPersonagensMenor(idMin, limit, fav);
    }

    private ArrayList<Personagem> buscarPersonagensFimLocal(int limit, boolean fav){
        return pado.buscarPersonagensMaior(idMax, limit, fav);
    }

}
