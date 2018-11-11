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
    }

    public void limparBanco(){
        db.getBanco().execSQL("DROP TABLE IF EXISTS `especie`;");
        db.getBanco().execSQL("DROP TABLE IF EXISTS `personagem`;");
    }

    public void criarTabelaPersonagem() {
        db.getBanco().execSQL("CREATE TABLE IF NOT EXISTS `personagem` (\n" +
                "\t`id`\tINTEGER NOT NULL UNIQUE,\n" +
                "\t`name`\tTEXT NOT NULL,\n" +
                "\t`height`\tTEXT NOT NULL,\n" +
                "\t`mass`\tTEXT NOT NULL,\n" +
                "\t`hair_color`\tTEXT NOT NULL,\n" +
                "\t`skin_color`\tTEXT NOT NULL,\n" +
                "\t`eye_color`\tTEXT NOT NULL,\n" +
                "\t`birth_year`\tTEXT NOT NULL,\n" +
                "\t`gender`\tTEXT NOT NULL,\n" +
                "\t`homeworld`\tTEXT NOT NULL,\n" +
                "\t`fav`\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(`id`)\n" +
                ");");
    }

    public void criarTabelaEspecie() {
        db.getBanco().execSQL("CREATE TABLE IF NOT EXISTS `especie` (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`name`\tTEXT NOT NULL,\n" +
                "\t`personagem`\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(`personagem`) REFERENCES `personagem`(`id`)\n" +
                ");");
    }

    public void inserirPersonagem(Personagem p) {
        try {
            db.getBanco().execSQL("INSERT INTO personagem VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new String[]{p.getId() + "", p.getName(), p.getHeight(), p.getMass(), p.getHair_color(),
                            p.getSkin_color(), p.getEye_color(), p.getBirth_year(), p.getGender(), p.getHomeworld()});

            for (String s : p.getSpecies()) {
                db.getBanco().execSQL("INSERT INTO especie (name, personagem) VALUES (?, ?)",
                        new String[]{s, p.getId() + ""});
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
                            "skin_color = ?, eye_color = ?, birth_year = ?, gender = ? WHERE id = ?",
                    new String[]{p.getName(), p.getHeight(), p.getMass(), p.getHair_color(),
                            p.getSkin_color(), p.getEye_color(), p.getBirth_year(), p.getGender(), p.getHomeworld(), p.getId() + ""});

            db.getBanco().execSQL("DELETE FROM personagem WHERE id = ?",
                    new String[]{"" + p.getId()});

            for (String s : p.getSpecies()) {
                db.getBanco().execSQL("INSERT INTO especie VALUES (NULL, ?, ?)",
                        new String[]{s, p.getId() + ""});
            }

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
            personagem.setBirth_year(cursor.getString(cursor.getColumnIndex("name")));
            personagem.setEye_color(cursor.getString(cursor.getColumnIndex("height")));
            personagem.setGender(cursor.getString(cursor.getColumnIndex("mass")));
            personagem.setHair_color(cursor.getString(cursor.getColumnIndex("hair_color")));
            personagem.setHeight(cursor.getString(cursor.getColumnIndex("skin_color")));
            personagem.setMass(cursor.getString(cursor.getColumnIndex("eye_color")));
            personagem.setName(cursor.getString(cursor.getColumnIndex("birth_year")));
            personagem.setSkin_color(cursor.getString(cursor.getColumnIndex("gender")));
            personagem.setHomeworld(cursor.getString(cursor.getColumnIndex("homeworld")));
            personagem.setFav(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("fav"))));

            personagem.setSpecies(buscarEspecies(personagem.getId()));

            listaPersonagem.add(personagem);
            cursor.moveToNext();
        }
        return listaPersonagem;
    }

    public ArrayList<Personagem> buscarPersonagens(int menor_que, int maior_que) {
        try {

            Cursor cursor = db.getBanco().rawQuery("SELECT * FROM personagem WHERE id < ? AND id > ? ORDER BY id;",
                    new String[]{menor_que + "", maior_que + ""});

            listaPersonagem = getValuesPer(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return listaPersonagem;
        }
    }

    private ArrayList<String> getValuesEsp(Cursor cursor) {
        ArrayList<String> esps = new ArrayList<String>();
        cursor.moveToFirst();
        while (cursor != null) {
            String s = "";
            s = cursor.getString(cursor.getColumnIndex("name"));
            esps.add(s);
            cursor.moveToNext();
        }
        return esps;
    }

    private ArrayList<String> buscarEspecies(int id) {
        try {
            Cursor cursor = db.getBanco().rawQuery("SELECT * FROM especie WHERE personagem = ?;",
                    new String[]{id + ""});

            return getValuesEsp(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
