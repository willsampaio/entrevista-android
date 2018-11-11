package com.example.will.entrevista_android.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.will.entrevista_android.Classes.Personagem;
import com.example.will.entrevista_android.Classes.PersonagemAdapter;
import com.example.will.entrevista_android.Ferramentas.JsonPersonagem;
import com.example.will.entrevista_android.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private int indexFimP = 0, maxIndexP = 90, indexInicioP = 0, minIndexP = 1, maxListSize = 30, qtdRemove = 20;
    private boolean carregarInicio, carregarFim;
    private ListView listViewPersonagens;
    private ArrayList<Personagem> arrayListPersonagens;
    private PersonagemAdapter personagemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config();
        addItemsFim();
    }

    private void config() {
        arrayListPersonagens = new ArrayList<Personagem>();
        listViewPersonagens = (ListView) findViewById(R.id.listViewPersonagens);
        personagemAdapter = new PersonagemAdapter(arrayListPersonagens, this);
        listViewPersonagens.setAdapter(personagemAdapter);

        listViewPersonagens.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem == 0){
                    if(carregarInicio) {
                        addItemsInicio();
                        carregarInicio = false;
                        return;
                    }else {
                        carregarInicio = true;
                    }
                }

                if((firstVisibleItem + visibleItemCount) >= (totalItemCount) && totalItemCount != 0)
                {
                    if(carregarFim) {
                        addItemsFim();
                        carregarFim = false;
                        return;
                    }else{
                        carregarFim = true;
                    }
                }
            }
        });

    }

    private void addItemsFim() {

        if(indexFimP >= maxIndexP){
            return;
        }

        if(arrayListPersonagens.size() >= maxListSize){
            for(int i = 0; i < qtdRemove; i++){
                arrayListPersonagens.remove(0);
            }
        }

        arrayListPersonagens.addAll(buscarPersonagensFim(10));
        indexInicioP = arrayListPersonagens.get(0).getId();
        indexFimP = arrayListPersonagens.get(arrayListPersonagens.size() -1).getId();
        personagemAdapter.notifyDataSetChanged();
    }

    private ArrayList<Personagem> buscarPersonagensFim(int max) {
        try{
            ArrayList<Personagem> list = new ArrayList<>();
            max += indexFimP;

            for (; indexFimP < max; indexFimP++){
                Personagem p = JsonPersonagem.getPersonagemSimples("https://swapi.co/api/people/" + indexFimP + "/");

                if(p != null) {
                    list.add(p);
                }
            }

            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addItemsInicio() {

        if(indexInicioP <= minIndexP){
            return;
        }

        if(arrayListPersonagens.size() >= maxListSize){
            for(int i = 0; i < qtdRemove; i++){
                arrayListPersonagens.remove(arrayListPersonagens.size() -1);
            }
        }

        arrayListPersonagens.addAll(0, buscarPersonagensInicio(10));
        indexInicioP = arrayListPersonagens.get(0).getId();
        personagemAdapter.notifyDataSetChanged();
    }

    private ArrayList<Personagem> buscarPersonagensInicio(int min) {
        try{
            ArrayList<Personagem> list = new ArrayList<>();
            min = indexInicioP - min;

            if(min < minIndexP){
                min = minIndexP;
            }

            for (; min < indexInicioP; min++){
                Personagem p = JsonPersonagem.getPersonagemSimples("https://swapi.co/api/people/" + min + "/");

                if(p != null) {
                    list.add(p);
                }
            }

            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
