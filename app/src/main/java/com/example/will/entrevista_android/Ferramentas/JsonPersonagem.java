package com.example.will.entrevista_android.Ferramentas;

import android.content.Context;

import com.example.will.entrevista_android.Classes.Personagem;
import com.example.will.entrevista_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class JsonPersonagem {

    public static Personagem getPersonagemSimples(String url, Context context) throws JSONException, ExecutionException, InterruptedException {
        Personagem p = null;

        String json = new JsonDownload().execute(url).get();

        if(json == null){
            return null;
        }

        JSONObject j = new JSONObject(json);

        p = new Personagem();
        String url_ = j.getString(context.getResources().getString(R.string.json_url));
        int id = idFromUrl(url_);
        p.setId(id);
        p.setGender(j.getString(context.getResources().getString(R.string.json_gender)));
        p.setHeight(j.getString(context.getResources().getString(R.string.json_height)));
        p.setMass(j.getString(context.getResources().getString(R.string.json_mass)));
        p.setName(j.getString(context.getResources().getString(R.string.json_name)));

        return p;
    }

    public static Personagem getPersonagemCompleto(String url, Context context) throws JSONException, ExecutionException, InterruptedException {
        Personagem p = null;

        String json = new JsonDownload().execute(url).get();

        if(json == null){
            return null;
        }

        JSONObject j = new JSONObject(json);

        p = new Personagem();
        String url_ = j.getString(context.getResources().getString(R.string.json_url));
        int id = idFromUrl(url_);
        p.setId(id);
        p.setBirth_year(j.getString(context.getResources().getString(R.string.json_birth_year)));
        p.setEye_color(j.getString(context.getResources().getString(R.string.json_eye_color)));
        p.setGender(j.getString(context.getResources().getString(R.string.json_gender)));
        p.setHair_color(j.getString(context.getResources().getString(R.string.json_hair_color)));
        p.setHeight(j.getString(context.getResources().getString(R.string.json_height)));
        p.setMass(j.getString(context.getResources().getString(R.string.json_mass)));
        p.setName(j.getString(context.getResources().getString(R.string.json_name)));
        p.setSkin_color(j.getString(context.getResources().getString(R.string.json_skin_color)));
        p.setHomeworld(j.getString(context.getResources().getString(R.string.json_homeworld)));

        JSONArray ja = j.getJSONArray(context.getResources().getString(R.string.json_species));
        ArrayList<String> species = new ArrayList<String>();

        for (int i = 0; i < ja.length(); i++) {
            url = ja.getString(i);
            json = new JsonDownload().execute(url).get();
            j = new JSONObject(json);
            String s = j.getString(context.getResources().getString(R.string.json_name));
            species.add(s);
        }

        url = p.getHomeworld();
        json = new JsonDownload().execute(url).get();
        j = new JSONObject(json);
        p.setHomeworld(j.getString(context.getResources().getString(R.string.json_name)));

        p.setSpecies(species);
        return p;
    }

    private static int idFromUrl(String url){
        String[] partes = url.split("/");
        int id = Integer.parseInt(partes[partes.length -1]);
        return id;
    }

}
