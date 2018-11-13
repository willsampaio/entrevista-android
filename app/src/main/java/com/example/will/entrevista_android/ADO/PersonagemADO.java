package com.example.will.entrevista_android.ADO;

import android.content.Context;
import android.database.Cursor;

import com.example.will.entrevista_android.Classes.Personagem;

import java.util.ArrayList;

public class PersonagemADO {

    private Personagem personagem;
    private Context context;
    private BancoDados db;
    private ArrayList<Personagem> listaPersonagem;

    public PersonagemADO(Context context) {
        this.context = context;
        db = new BancoDados(context);
        criarTabelaPersonagem();
        criarTabelaEspecie();
        criarTabelaFavNaoEnviado();
    }

    public void limparBanco(){
        db.getBanco().execSQL("DROP TABLE IF EXISTS especie;");
        db.getBanco().execSQL("DROP TABLE IF EXISTS personagem;");
        criarTabelaPersonagem();
        criarTabelaEspecie();
    }

    public void criarTabelaPersonagem() {
        db.getBanco().execSQL("CREATE TABLE IF NOT EXISTS personagem (id INTEGER NOT NULL UNIQUE,name TEXT,height TEXT,mass TEXT,hair_color TEXT,skin_color TEXT,eye_color TEXT,birth_year TEXT,gender TEXT,homeworld TEXT,fav TEXT,PRIMARY KEY(id));");
    }

    public void criarTabelaEspecie() {
        db.getBanco().execSQL("CREATE TABLE IF NOT EXISTS especie ( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, name TEXT, personagem INTEGER, FOREIGN KEY(personagem) REFERENCES personagem(id));");
    }

    public void criarTabelaFavNaoEnviado() {
        db.getBanco().execSQL("CREATE TABLE IF NOT EXISTS fav_nao_enviado (id INTEGER NOT NULL, PRIMARY KEY(id));");
    }

    public void inserirPersonagem(Personagem p) {
        try {
            db.getBanco().execSQL("INSERT INTO personagem VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new String[]{p.getId() + "", p.getName(), p.getHeight(), p.getMass(), p.getHair_color(),
                            p.getSkin_color(), p.getEye_color(), p.getBirth_year(), p.getGender(), p.getHomeworld()});

            if(p.getSpecies() != null) {
                for (String s : p.getSpecies()) {
                    db.getBanco().execSQL("INSERT INTO especie (name, personagem) VALUES (?, ?)",
                            new String[]{s, p.getId() + ""});
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletarPersonagem(Personagem p) {
        try {
            db.getBanco().execSQL("DELETE FROM especie WHERE personagem = ?",
                    new String[]{"" + p.getId()});

            db.getBanco().execSQL("DELETE FROM personagem WHERE id = ?",
                    new String[]{"" + p.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizarPersonagem(Personagem p) {
        try {
            db.getBanco().execSQL("UPDATE personagem SET name = ?, height = ?, mass = ?, hair_color = ?, " +
                            "skin_color = ?, eye_color = ?, birth_year = ?, gender = ?, homeworld = ?, fav = ? WHERE id = ?",
                    new String[]{p.getName(), p.getHeight(), p.getMass(), p.getHair_color(),
                            p.getSkin_color(), p.getEye_color(), p.getBirth_year(), p.getGender(), p.getHomeworld(), String.valueOf(p.isFav()).toLowerCase(), p.getId() + ""});

            db.getBanco().execSQL("DELETE FROM especie WHERE personagem = ?",
                    new String[]{"" + p.getId()});

            for (String s : p.getSpecies()) {
                db.getBanco().execSQL("INSERT INTO especie VALUES (NULL, ?, ?)",
                        new String[]{s, p.getId() + ""});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void favoritarPersonagem(Personagem p) {
        try {
            db.getBanco().execSQL("UPDATE personagem SET fav = ? WHERE id = ?",
                    new String[]{((Boolean)p.isFav()).toString().toLowerCase(), p.getId()+""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Personagem> getValuesPer(Cursor cursor) {
        listaPersonagem = new ArrayList<Personagem>();
        cursor.moveToFirst();
        while (cursor != null) {
            personagem = new Personagem();
            personagem.setId(cursor.getInt(cursor.getColumnIndex("id")));
            personagem.setName(cursor.getString(cursor.getColumnIndex("name")));
            personagem.setHeight(cursor.getString(cursor.getColumnIndex("height")));
            personagem.setMass(cursor.getString(cursor.getColumnIndex("mass")));
            personagem.setHair_color(cursor.getString(cursor.getColumnIndex("hair_color")));
            personagem.setSkin_color(cursor.getString(cursor.getColumnIndex("skin_color")));
            personagem.setEye_color(cursor.getString(cursor.getColumnIndex("eye_color")));
            personagem.setBirth_year(cursor.getString(cursor.getColumnIndex("birth_year")));
            personagem.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            personagem.setHomeworld(cursor.getString(cursor.getColumnIndex("homeworld")));
            personagem.setFav(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("fav"))));

            personagem.setSpecies(buscarEspecies(personagem.getId()));

            listaPersonagem.add(personagem);
            cursor.moveToNext();
        }
        return listaPersonagem;
    }

    public Personagem buscarPersonagemID(int id) {
        try {

            Cursor cursor = db.getBanco().rawQuery("SELECT * FROM personagem WHERE id = ?;",
                    new String[]{id+""});

            listaPersonagem = getValuesPer(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(listaPersonagem != null && listaPersonagem.size() == 1) {
                return listaPersonagem.get(0);
            }else{
                return null;
            }
        }
    }

//    public ArrayList<Personagem> buscarPersonagens(int maior_que, int menor_que) {
//        try {
//
//            Cursor cursor = db.getBanco().rawQuery("SELECT * FROM personagem WHERE id > ? AND id < ? ORDER BY id;",
//                    new String[]{maior_que + "", menor_que + ""});
//
//            listaPersonagem = getValuesPer(cursor);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            return listaPersonagem;
//        }
//    }

    public ArrayList<Personagem> buscarPersonagensMaior(int maior_que, int limit, boolean fav) {
        try {
            Cursor cursor;

            if(fav) {
                cursor = db.getBanco().rawQuery("SELECT * FROM personagem WHERE id > ? AND fav = 'true' ORDER BY id LIMIT ?;",
                        new String[]{maior_que + "", limit + ""});
            }else{
                cursor = db.getBanco().rawQuery("SELECT * FROM personagem WHERE id > ? ORDER BY id LIMIT ?;",
                        new String[]{maior_que + "", limit + ""});
            }

            listaPersonagem = getValuesPer(cursor);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return listaPersonagem;
        }
    }

    public ArrayList<Personagem> buscarPersonagensMenor(int menor_que, int limit, boolean fav) {
        try {
            Cursor cursor;

            if(fav) {
                cursor = db.getBanco().rawQuery("SELECT * FROM personagem WHERE id < ? AND fav = 'true' ORDER BY id LIMIT ?;",
                        new String[]{menor_que + "", limit + ""});
            }else{
                cursor = db.getBanco().rawQuery("SELECT * FROM personagem WHERE id < ? ORDER BY id LIMIT ?;",
                        new String[]{menor_que + "", limit + ""});
            }

            listaPersonagem = getValuesPer(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return listaPersonagem;
        }
    }

//    public ArrayList<Personagem> buscarPersonagensFavoritos(int maior_que, int menor_que) {
//        try {
//
//            Cursor cursor = db.getBanco().rawQuery("SELECT * FROM personagem WHERE id > ? AND id < ? AND fav = 'true' ORDER BY id;",
//                    new String[]{maior_que + "", menor_que + ""});
//
//            listaPersonagem = getValuesPer(cursor);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            return listaPersonagem;
//        }
//    }

    private ArrayList<String> getValuesEsp(Cursor cursor) {
        ArrayList<String> esps = new ArrayList<String>();
        cursor.moveToFirst();
        int i = 0;
        while (cursor != null && i < cursor.getCount()) {
            String s = "";
            s = cursor.getString(cursor.getColumnIndex("name"));
            esps.add(s);
            cursor.moveToNext();
            i++;
        }
        return esps;
    }

    private ArrayList<String> buscarEspecies(int id) {
        try {
            Cursor cursor = db.getBanco().rawQuery("SELECT * FROM especie WHERE personagem = ? ORDER BY id;",
                    new String[]{id + ""});

            return getValuesEsp(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void inserirFavNaoEnviadoId(int id) {
        try {
            db.getBanco().execSQL("INSERT INTO fav_nao_enviado (id) VALUES (?)",
                    new String[]{id+""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletarFavNaoEnviadoId(int id) {
        try {
            db.getBanco().execSQL("DELETE FROM fav_nao_enviado WHERE id = ?",
                    new String[]{id+""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> buscarFavNaoEnviados(){
        try {
            Cursor cursor = db.getBanco().rawQuery("SELECT * FROM fav_nao_enviado ORDER BY id;",
                    null);

            ArrayList<Integer> ids = new ArrayList<Integer>();
            cursor.moveToFirst();
            int i = 0;
            while (cursor != null && i < cursor.getCount()) {
                int id;
                id = cursor.getInt(cursor.getColumnIndex("id"));
                ids.add(id);
                cursor.moveToNext();
                i++;
            }

            return ids;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Integer>();
        }
    }


}
