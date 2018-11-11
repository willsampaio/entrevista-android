package com.example.will.entrevista_android.ADO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BancoDados {

    private SQLiteDatabase banco;
    private final String NOMEBANCO = "WIKISW";
    private final Context context;

    public BancoDados(Context context){
        this.context = context;
        banco = context.openOrCreateDatabase(NOMEBANCO, Context.MODE_PRIVATE, null);
    }

    public SQLiteDatabase getBanco() {
        return banco;
    }

    public void setBanco(SQLiteDatabase banco) {
        this.banco = banco;
    }

}
