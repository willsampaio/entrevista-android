package com.example.will.entrevista_android.Ferramentas;

import android.util.Log;

import com.example.will.entrevista_android.Classes.Personagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class JsonPersonagem {

    public static Personagem getPersonagemSimples(String url) throws JSONException, ExecutionException, InterruptedException {
        Personagem p = null;

        String json = new JsonDownload().execute(url).get();
        JSONObject j = new JSONObject(json);

        p = new Personagem();
        String url_ = j.getString("url");
        int id = idFromUrl(url_);
        p.setId(id);
        p.setGender(j.getString("gender"));
        p.setHeight(j.getInt("height"));
        p.setMass(j.getInt("mass"));
        p.setName(j.getString("name"));

        return p;
    }

    public static Personagem getPersonagemCompleto(String url) throws JSONException, ExecutionException, InterruptedException {
        Personagem p = null;

        String json = new JsonDownload().execute(url).get();
        JSONObject j = new JSONObject(json);

        p = new Personagem();
        String url_ = j.getString("url");
        int id = idFromUrl(url_);
        p.setId(id);
        p.setBirth_year(j.getString("birth_year"));
        p.setEye_color(j.getString("eye_color"));
        p.setGender(j.getString("gender"));
        p.setHair_color(j.getString("hair_color"));
        p.setHeight(j.getInt("height"));
        p.setMass(j.getInt("mass"));
        p.setName(j.getString("name"));
        p.setSkin_color(j.getString("skin_color"));
        p.setHomeworld(j.getString("homeworld"));

        JSONArray ja = j.getJSONArray("species");
        ArrayList<String> species = new ArrayList<String>();

        for (int i = 0; i < ja.length(); i++) {
            url = ja.getString(i);
            json = new JsonDownload().execute(url).get();
            j = new JSONObject(json);
            String s = j.getString("name");
            species.add(s);
        }

        url = p.getHomeworld();
        json = new JsonDownload().execute(url).get();
        j = new JSONObject(json);
        p.setHomeworld(j.getString("name"));

        p.setSpecies(species);
        return p;
    }

    private static int idFromUrl(String url){
        String[] partes = url.split("/");
        int id = Integer.parseInt(partes[partes.length -1]);
        return id;
    }

//    private static String getHomeworld(String url) throws ExecutionException, InterruptedException {
//        String json = new JsonDownload().execute(url).get();
//       // String homeworld =
//    }

}
