package com.example.will.entrevista_android.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.will.entrevista_android.ADO.PersonagemADO;
import com.example.will.entrevista_android.Classes.Personagem;
import com.example.will.entrevista_android.Ferramentas.JsonPersonagem;
import com.example.will.entrevista_android.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CarregarDadosActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button button;
    private TextView textView;
    private final int total = 87, maxId = 90;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carregar_dados);

        config();

        PersonagemADO pado = new PersonagemADO(this);

//        pado.limparBanco();

        ArrayList<Personagem> al = pado.buscarPersonagensMaior(0, 90, false);
        if(al.size() == total){
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
        textView = findViewById(R.id.textViewLoading);

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
                int count = 0, id = 0;
                while (count < total) {
                    Personagem p = null;
                    try {
                        p = JsonPersonagem.getPersonagemSimples("https://swapi.co/api/people/" + id++ + "/");

                        if(p != null) {
                            pado.inserirPersonagem(p);
                            count++;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int percent = (int) (((double)count / total) * 100);

                    final int finalPercent = percent;
                    final int finalCount = count;
                    final int finalId = id;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(finalPercent);

                            if(finalCount == total){
                                button.setVisibility(View.VISIBLE);
                            }

                            if((finalId > maxId) && (finalCount != total)){
                                error();
                                return;
                            }
                        }
                    });
                }
            }
        });

        t.start();
    }

    private void error(){
        textView.setText(getResources().getString(R.string.error_loading));
    }

}


