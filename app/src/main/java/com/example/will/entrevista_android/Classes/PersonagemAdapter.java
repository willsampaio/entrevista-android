package com.example.will.entrevista_android.Classes;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.will.entrevista_android.ADO.PersonagemADO;
import com.example.will.entrevista_android.Activities.PersonagemActivity;
import com.example.will.entrevista_android.Ferramentas.PostRequest;
import com.example.will.entrevista_android.Ferramentas.PostRequestFav;
import com.example.will.entrevista_android.R;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PersonagemAdapter extends BaseAdapter {

    private List<Personagem> personagensList;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    public int idAux, positionAux = -1;

    public PersonagemAdapter(List<Personagem> personagensList, Activity activity) {
        this.personagensList = personagensList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return personagensList.size();
    }

    @Override
    public Object getItem(int position) {
        return personagensList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return personagensList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            layoutInflater = LayoutInflater.from(activity);
            convertView = layoutInflater.inflate(R.layout.adapter_personagem_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) convertView.getTag();

        final Personagem p = (Personagem) getItem(position);
        viewHolder.setValues(p);

        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPersonagem(p);
            }
        });

        viewHolder.tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPersonagem(p);
            }
        });

        viewHolder.ivFavo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritarPersonagem(p);
            }
        });

        idAux = p.getId();
        positionAux = position;

        return convertView;
    }

    private void mostrarPersonagem(Personagem p){
        Intent intent = new Intent(activity, PersonagemActivity.class);
        intent.putExtra("id", p.getId());
        intent.putExtra("name", p.getName());
        intent.putExtra("height", p.getHeight());
        intent.putExtra("gender", p.getGender());
        intent.putExtra("mass", p.getMass());
        intent.putExtra("fav", p.isFav());
        activity.startActivity(intent);
    }

    private void favoritarPersonagem(Personagem p){
        if(p.isFav()){
            p.setFav(false);
            viewHolder.ivFavo.setImageResource(R.drawable.btn_star_big_off);
        }else{
            p.setFav(true);
            viewHolder.ivFavo.setImageResource(R.drawable.btn_star_big_on);

            try {
                PostRequestFav.favoritarExterno(p.getId(), activity);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        PersonagemADO pado = new PersonagemADO(activity);
        pado.favoritarPersonagem(p);
        notifyDataSetChanged();
    }





    //-------

    private class ViewHolder {
        private TextView tvName, tvInfo;
        private ImageView ivFavo;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.textViewNameAdp);
            tvInfo = (TextView) view.findViewById(R.id.textViewInfoAdp);
            ivFavo = (ImageView) view.findViewById(R.id.imageViewFavAdp);
        }

        public void setValues(Personagem p) {
            tvName.setText(p.getName());
            tvInfo.setText("Gender: " + p.getGender() + "  Height: " + p.getHeight() + "  Mass: " + p.getMass());

            if(p.isFav()){
                ivFavo.setImageResource(R.drawable.btn_star_big_on);
            }else {
                ivFavo.setImageResource(R.drawable.btn_star_big_off);
            }

            notifyDataSetChanged();
        }
    }

    //-------
}
