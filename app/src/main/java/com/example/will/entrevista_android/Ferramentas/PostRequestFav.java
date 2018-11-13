package com.example.will.entrevista_android.Ferramentas;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.will.entrevista_android.ADO.PersonagemADO;
import com.example.will.entrevista_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class PostRequestFav {

    public static String favoritarExterno(int id, Context context) throws ExecutionException, InterruptedException, JSONException {
        String json = new PostRequest().execute(id).get();
        JSONObject j = null;

        if(json != null) {
            j = new JSONObject(json);
        }else{
            //showMessage("",context.getResources().getString(R.string.error_post_request), context);
            Toast.makeText(context, context.getResources().getString(R.string.error_post_request), Toast.LENGTH_SHORT).show();
            return null;
        }

        String message = "", title = "";

        try {
            title = j.getString("status");
            message = j.getString("message");
            showMessage(title, message, context);
            return message;
        }catch (JSONException e){
            title = j.getString("error");
            message = j.getString("error_message");
            showMessage(title, message, context);
            PersonagemADO pado = new PersonagemADO(context);
            pado.inserirFavNaoEnviadoId(id);
            return message;
        }
    }

    private static void showMessage(String title, String message, Context context){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.create().show();
    }

}
