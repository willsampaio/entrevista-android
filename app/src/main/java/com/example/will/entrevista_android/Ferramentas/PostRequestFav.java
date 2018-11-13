package com.example.will.entrevista_android.Ferramentas;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.will.entrevista_android.ADO.PersonagemADO;
import com.example.will.entrevista_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PostRequestFav {

    public static boolean favoritarExterno(int id, Context context) throws ExecutionException, InterruptedException, JSONException {
        String json = new PostRequest().execute(id).get();
        JSONObject j = null;

        if(json != null) {
            j = new JSONObject(json);
        }else{
            //showMessage("",context.getResources().getString(R.string.error_post_request), context);
            Toast.makeText(context, context.getResources().getString(R.string.error_post_request), Toast.LENGTH_SHORT).show();
            PersonagemADO pado = new PersonagemADO(context);
            pado.inserirFavNaoEnviadoId(id);
            return false;
        }

        String message = "", title = "";

        try {
            title = j.getString(context.getResources().getString(R.string.json_req_status));
            message = j.getString(context.getResources().getString(R.string.json_req_message));
            showMessage(title, message, context);
            return true;
        }catch (JSONException e){
            title = j.getString(context.getResources().getString(R.string.json_req_erro));
            message = j.getString(context.getResources().getString(R.string.json_req_erro_message));
            showMessage(title, message, context);
            PersonagemADO pado = new PersonagemADO(context);
            pado.inserirFavNaoEnviadoId(id);
            return true;
        }
    }

    private static void showMessage(String title, String message, Context context){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.create().show();
    }

    public static boolean favoritarExternoLista(Context context) throws InterruptedException, ExecutionException, JSONException {
        PersonagemADO pado = new PersonagemADO(context);
        ArrayList<Integer> lista = pado.buscarFavNaoEnviados();

        if (lista.size() > 0) {
            Toast.makeText(context, context.getResources().getString(R.string.send_fav), Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < lista.size(); i++) {
            String json;

            if((i+1) == lista.size() / 2) {
                json = new PostRequest().execute(lista.get(i), 1).get();
            }else{
                json = new PostRequest().execute(lista.get(i), 0).get();
            }

            JSONObject j = null;

            if(json != null) {
                j = new JSONObject(json);
            }else{
                Toast.makeText(context, context.getResources().getString(R.string.error_post_request), Toast.LENGTH_SHORT).show();
                return false;
            }

            String message = "", title = "";

            try {
                if(i == (lista.size() -1)){
                    title = j.getString("status");
                    message = j.getString("message");
                    showMessage(title, message, context);
                }

                pado.deletarFavNaoEnviadoId(lista.get(i));
                continue;
            }catch (JSONException e){
                title = j.getString("error");
                message = j.getString("error_message");
                showMessage(title, message, context);
                return false;
            }
        }

        return true;
    }

}
