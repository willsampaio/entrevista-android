package com.example.will.entrevista_android.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.will.entrevista_android.Classes.Personagem;
import com.example.will.entrevista_android.Ferramentas.JsonPersonagem;
import com.example.will.entrevista_android.R;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class PersonagemActivity extends AppCompatActivity {

    private TextView tvName, tvHeight, tvMass, tvHairColor, tvSkinColor,
            tvEyeColor, tvBirthYear, tvGender, tvHomeworld, tvSpecies;

    private Personagem personagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personagem);

        try {
            config();
            mostrarDados(personagem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        Intent it = getIntent();
        int id = it.getIntExtra("id", -1);
        String name = it.getStringExtra("name");
        String height = it.getStringExtra("height");
        String gender = it.getStringExtra("gender");
        String mass = it.getStringExtra("mass");
        boolean fav = it.getBooleanExtra("fav", false);

        personagem = new Personagem();
        personagem.setId(id);
        personagem.setName(name);
        personagem.setHeight(height);
        personagem.setGender(gender);
        personagem.setMass(mass);
        personagem.setFav(fav);

        mostrarDados(personagem);

        //personagem = JsonPersonagem.getPersonagemCompleto(getResources().getString(R.string.url) + id);
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


}
