package com.ridesforme.ridesforme.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridesforme.ridesforme.R;
import com.ridesforme.ridesforme.basicas.Carona;

import java.util.List;

/**
 * Created by Marcos Antônio on 30/09/2015.
 */
public class CaronaAdapter extends BaseAdapter{

    ViewHolder mViewHolder;
    Context mContexto;
    List<Carona> mCaronas;

    public CaronaAdapter(Context contexto, List<Carona> caronas){
        this.mContexto = contexto;
        this.mCaronas = caronas;
    }

    @Override
    public int getCount() {
        return mCaronas.size();
    }

    @Override
    public Object getItem(int position) {
        return mCaronas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContexto).inflate(R.layout.item_lista_carona, null);
            mViewHolder = new ViewHolder();
            mViewHolder.imgLogo = (ImageView)convertView.findViewById(R.id.imgLogo);
            mViewHolder.txtOrigem = (TextView)convertView.findViewById(R.id.txtItemCaronaOrigem);
            mViewHolder.txtDestino = (TextView)convertView.findViewById(R.id.txtItemCaronaDestino);
        }

        Carona carona = mCaronas.get(position);

        Drawable imgPerfil = mContexto.getResources().getDrawable(R.drawable.ic_perfil);
        mViewHolder.imgLogo.setImageDrawable(imgPerfil);
        mViewHolder.txtOrigem.setText(carona.getRuaOrigem());
        mViewHolder.txtDestino.setText(carona.getRuaDestino());

        return convertView;
    }

    static class ViewHolder{
        ImageView imgLogo;
        TextView txtOrigem;
        TextView txtDestino;
    }
}
