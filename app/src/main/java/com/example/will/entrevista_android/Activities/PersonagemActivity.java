package com.example.will.entrevista_android.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.will.entrevista_android.ADO.PersonagemADO;
import com.example.will.entrevista_android.Classes.Personagem;
import com.example.will.entrevista_android.Ferramentas.JsonPersonagem;
import com.example.will.entrevista_android.Ferramentas.PostRequestFav;
import com.example.will.entrevista_android.R;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class PersonagemActivity extends AppCompatActivity {

    private TextView tvName, tvHeight, tvMass, tvHairColor, tvSkinColor,
            tvEyeColor, tvBirthYear, tvGender, tvHomeworld, tvSpecies;
    private Menu menu;
    private Personagem personagem;
    private PersonagemADO pado;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personagem);

        try {
            config();
//            mostrarDados(personagem);
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
        inflater.inflate(R.menu.menu_personagem, menu);
        this.menu = menu;
        setTitleMenu();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favoritar_menu:
                favoritarPersonagem(personagem);
                setTitleMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitleMenu();
    }

    private void config() throws InterruptedException, ExecutionException, JSONException {
        tvName = findViewById(R.id.textViewName);
        tvHeight = findViewById(R.id.textViewHeight);
        tvMass = findViewById(R.id.textViewMass);
        tvHairColor = findViewById(R.id.textViewHairColor);
        tvSkinColor = findViewById(R.id.textViewSkinColor);
        tvEyeColor = findViewById(R.id.textViewEyeColor);
        tvBirthYear = findViewById(R.id.textViewBirthYear);
        tvGender = findViewById(R.id.textViewGender);
        tvHomeworld = findViewById(R.id.textViewHomeworld);
        tvSpecies = findViewById(R.id.textViewSpecies);

        pado = new PersonagemADO(getApplicationContext());
        intent = getIntent();

        personagem = getPersonagemIntent();

        //personagem = pado.buscarPersonagemID(id);

        mostrarDados(personagem);
        carrecarDadosSecundarios();
    }

    private Personagem getPersonagemIntent(){
        int id = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");
        String height = intent.getStringExtra("height");
        String gender = intent.getStringExtra("gender");
        String mass = intent.getStringExtra("mass");
        boolean fav = intent.getBooleanExtra("fav", false);

        Personagem p = new Personagem();
        p.setId(id);
        p.setName(name);
        p.setHeight(height);
        p.setGender(gender);
        p.setMass(mass);
        p.setFav(fav);
        return p;
    }

    private void setTitleMenu(){
        if(menu == null){
            return;
        }

        if(personagem == null){
            personagem = getPersonagemIntent();
        }

        MenuItem mi = menu.findItem(R.id.favoritar_menu);
        if (personagem.isFav()) {
            mi.setTitle(getResources().getString(R.string.menu_rem_fav));
        } else {
            mi.setTitle(getResources().getString(R.string.menu_add_fav));
        }
    }

    private void mostrarDados(Personagem p){
        tvName.setText(getResources().getString(R.string.tv_name) + p.getName());
        tvHeight.setText(getResources().getString(R.string.tv_height) + p.getHeight());
        tvGender.setText(getResources().getString(R.string.tv_gender) + p.getGender());
        tvMass.setText(getResources().getString(R.string.tv_mass) + p.getMass());

        if(p.getHomeworld() != null) {
            tvHairColor.setText(getResources().getString(R.string.tv_hair_color) + p.getHair_color());
            tvSkinColor.setText(getResources().getString(R.string.tv_skin_color) + p.getSkin_color());
            tvEyeColor.setText(getResources().getString(R.string.tv_eye_color) + p.getEye_color());
            tvBirthYear.setText(getResources().getString(R.string.tv_birth_year) + p.getBirth_year());
            tvHomeworld.setText(getResources().getString(R.string.tv_homeworld) + p.getHomeworld());
            tvSpecies.setText(getResources().getString(R.string.tv_species) + p.getSpeciesString());
        }
    }

    private void favoritarPersonagem(Personagem p){
        if(p.isFav()) {
            p.setFav(false);
        }else {
            p.setFav(true);

            try {
                PostRequestFav.favoritarExterno(p.getId(), this);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        PersonagemADO pado = new PersonagemADO(this);
        pado.favoritarPersonagem(p);
    }

    private void carrecarDadosSecundarios(){
        final Handler h = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean fav = personagem.isFav();
                    personagem = JsonPersonagem.getPersonagemCompleto(getResources().getString(R.string.url) + personagem.getId());

                    if(personagem != null){
                        personagem.setFav(fav);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                h.post(new Runnable() {
                    @Override
                    public void run() {
                        if(personagem != null) {
                            mostrarDados(personagem);
                            pado.atualizarPersonagem(personagem);
                        }else {
                            error();
                        }
                    }
                });
            }
        });
        t.start();
    }

    private void error(){
        personagem = getPersonagemIntent();
        Toast.makeText(this, getResources().getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
    }
}
