package com.example.will.entrevista_android.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.will.entrevista_android.ADO.PersonagemADO;
import com.example.will.entrevista_android.Classes.Personagem;
import com.example.will.entrevista_android.Classes.PersonagemAdapter;
import com.example.will.entrevista_android.Ferramentas.JsonPersonagem;
import com.example.will.entrevista_android.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private int indexFimP = 0, maxIndexP = 90, indexInicioP = 0, minIndexP = 1, maxListSize = 30, qtdRemove = 20;
    private int idMin = -1, idMax = -1, addItensQtd = 10, firstVisibleItemAux;
    private boolean carregarInicio, carregarFim, carregarFav;
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

//                if(firstVisibleItem == 0){
//                    if(carregarInicio) {
//                        addItensInicioLocal();
//                        carregarInicio = false;
//                        return;
//                    }else {
//                        carregarInicio = true;
//                    }
//                }
//
//                if((firstVisibleItem + visibleItemCount) >= (totalItemCount -3) && totalItemCount != 0)
//                {
//                    if(carregarFim) {
//                        addItensFimLocal();
//                        carregarFim = false;
//                        return;
//                    }else{
//                        carregarFim = true;
//                    }
//                }
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

    private Personagem carregarPersonagem(int id){
        Personagem p = pado.buscarPersonagemID(id);
        return p;
    }

    private void addItensInicioLocal(){
        ArrayList<Personagem> al = buscarPersonagensInicioLocal(addItensQtd, carregarFav);
        arrayListPersonagens.addAll(0, al);
        limparLixo('b');

        if(arrayListPersonagens.size() > 0) {
            idMin = arrayListPersonagens.get(0).getId();
        }
        personagemAdapter.notifyDataSetChanged();
    }

    private void addItensFimLocal(){
        ArrayList<Personagem> al = buscarPersonagensFimLocal(addItensQtd, carregarFav);
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

//    private void addItensInicio() {
//
//        if(indexInicioP <= minIndexP){
//            return;
//        }
//
//        limparLixo('b');
//
//        arrayListPersonagens.addAll(0, buscarPersonagensInicio(10));
//        indexInicioP = arrayListPersonagens.get(0).getId();
//        personagemAdapter.notifyDataSetChanged();
//    }

//    private void addItensFim() {
//
//        if(indexFimP >= maxIndexP){
//            return;
//        }
//
//        limparLixo('c');
//
//        arrayListPersonagens.addAll(buscarPersonagensFim(10));
//        indexInicioP = arrayListPersonagens.get(0).getId();
//        indexFimP = arrayListPersonagens.get(arrayListPersonagens.size() -1).getId();
//        personagemAdapter.notifyDataSetChanged();
//    }

//    private ArrayList<Personagem> buscarPersonagensFim(int max) {
//        try{
//            ArrayList<Personagem> list = new ArrayList<>();
//            max += indexFimP;
//
//            for (; indexFimP < max; indexFimP++){
//                Personagem p = JsonPersonagem.getPersonagemSimples("https://swapi.co/api/people/" + indexFimP + "/");
//
//                if(p != null) {
//                    list.add(p);
//                }
//            }
//
//            return list;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private ArrayList<Personagem> buscarPersonagensInicio(int min) {
//        try{
//            ArrayList<Personagem> list = new ArrayList<>();
//            min = indexInicioP - min;
//
//            if(min < minIndexP){
//                min = minIndexP;
//            }
//
//            for (; min < indexInicioP; min++){
//                Personagem p = JsonPersonagem.getPersonagemSimples("https://swapi.co/api/people/" + min + "/");
//
//                if(p != null) {
//                    list.add(p);
//                }
//            }
//
//            return list;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
