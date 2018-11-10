package com.example.will.entrevista_android.Classes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.will.entrevista_android.R;

import java.util.List;

public class PersonagemAdapter extends BaseAdapter {

    private List<Personagem> personagensList;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;

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

        Personagem p = (Personagem) getItem(position);
        viewHolder.setValues(p);
        return convertView;
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
        }
    }

    //-------
}
