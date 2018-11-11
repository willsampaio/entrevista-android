package com.example.will.entrevista_android.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.example.will.entrevista_android.ADO.PersonagemADO;
import com.example.will.entrevista_android.Classes.Personagem;
import com.example.will.entrevista_android.Ferramentas.JsonPersonagem;
import com.example.will.entrevista_android.R;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class CarregarDadosActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button button;
    private final int total = 87;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carregar_dados);

        config();

        PersonagemADO pado = new PersonagemADO(this);

//        pado.limparBanco();

        if(pado.buscarPersonagens(0, 3) != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        try {
            carregarDados(pado);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void config(){
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void carregarDados(final PersonagemADO pado) throws InterruptedException {

        Thread t = new Thread(new Runnable() {
            public void run() {
                double count = 1;
                while (count < total) {
                    count += 1;
                    Personagem p = null;
                    try {
                        p = JsonPersonagem.getPersonagemSimples("https://swapi.co/api/people/" + count + "/");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(p != null) {
                        pado.inserirPersonagem(p);
                    }

                    final int percent = (int) ((count / total) * 100);
                    count++;

                    final double finalCount = count;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(percent);

                            if(finalCount == total){
                                button.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });

        t.start();
    }

}


