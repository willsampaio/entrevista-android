package com.example.will.entrevista_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.will.entrevista_android.Classes.Personagem;
import com.example.will.entrevista_android.Ferramentas.JsonPersonagem;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class PersonagemActivity extends AppCompatActivity {

    TextView tvName, tvHeight, tvMass, tvHairColor, tvSkinColor,
            tvEyeColor, tvBirthYear, tvGender, tvHomeworld, tvSpecies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personagem);



        getObjects();

        Personagem p = null;
        try {
            p = JsonPersonagem.getPersonagemSimples("https://swapi.co/api/people/1/");
            mostrarDados(p);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void getObjects(){
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
    }

    private void mostrarDados(Personagem p){
        tvName.setText(getResources().getString(R.string.tv_name) + p.getName());
        tvHeight.setText(getResources().getString(R.string.tv_height) + p.getHeight());
        tvMass.setText(getResources().getString(R.string.tv_mass) + p.getMass());
        tvGender.setText(getResources().getString(R.string.tv_gender) + p.getGender());
//        tvHairColor.setText(getResources().getString(R.string.tv_hair_color) + p.getHair_color());
//        tvSkinColor.setText(getResources().getString(R.string.tv_skin_color) + p.getSkin_color());
//        tvEyeColor.setText(getResources().getString(R.string.tv_eye_color) + p.getEye_color());
//        tvBirthYear.setText(getResources().getString(R.string.tv_birth_year) + p.getBirth_year());
//        tvHomeworld.setText(getResources().getString(R.string.tv_homeworld) + p.getHomeworld());
//        tvSpecies.setText(getResources().getString(R.string.tv_species) + p.getSpeciesString());
    }


}
