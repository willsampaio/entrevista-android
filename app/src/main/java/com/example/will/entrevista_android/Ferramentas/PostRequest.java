package com.example.will.entrevista_android.Ferramentas;

import android.os.AsyncTask;

import com.example.will.entrevista_android.Classes.Personagem;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class PostRequest extends AsyncTask<Integer, Void, String> {

    @Override
    protected String doInBackground(Integer... integers) {
        try {
            final URL url = new URL("http://private-782d3-starwarsfavorites.apiary-mock.com/favorite/" + integers[0]);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();

//            final OutputStream outputStream = connection.getOutputStream();
//            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//            writer.write(integers[i]+"");
//            writer.flush();
//            writer.close();
//            outputStream.close();

            InputStream stream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            reader.close();
            inputStreamReader.close();

            stream.close();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
