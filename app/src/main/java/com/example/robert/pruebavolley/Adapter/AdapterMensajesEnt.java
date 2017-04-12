package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robert.pruebavolley.R;

/**
 * Created by redlinks on 18/2/16.
 */
public class AdapterMensajesEnt extends ArrayAdapter<String> {
    private final Context context;
    private final String[] Emisor;
    private final String[] Asunto;
    private final String[] Mensaje;
    private final String[] Fecha;
    //private final Bitmap[] images;
    private final String[] fechalect;
    SharedPreferences settings;
    Bitmap b;

    public AdapterMensajesEnt(Context context, String[] Emisor, String[] Asunto, String[] Mensaje, String[] Fecha, String[] fechalect) {
        super(context, R.layout.lista_mensajes_ent, Emisor);
        this.context = context;
        this.Emisor = Emisor;
        this.Asunto = Asunto;
        this.Mensaje = Mensaje;
        //this.images = images;
        this.Fecha = Fecha;
        this.fechalect = fechalect;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView= inflater.inflate(R.layout.lista_mensajes_ent, null, true);
        ImageView avisoNM=(ImageView)rowView.findViewById(R.id.avisoNM);


        if(fechalect[position].equals("null")){
            avisoNM.setVisibility(View.VISIBLE);
        }

        TextView emisor = (TextView) rowView.findViewById(R.id.nombreEmisor);
        TextView asunto = (TextView) rowView.findViewById(R.id.mensajeAsunto);
        TextView mensaje = (TextView) rowView.findViewById(R.id.mensajeResum);
        TextView fecha = (TextView) rowView.findViewById(R.id.fechaEnvio);
        //ImageView imagenE = (ImageView) rowView.findViewById(R.id.emisor_image);

        emisor.setText(Emisor[position].toUpperCase());
        asunto.setText(Asunto[position].toUpperCase());
        mensaje.setText(Mensaje[position]);
        fecha.setText(Fecha[position]);
        //imagenE.setImageBitmap(images[position]);

        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#e6e7e8"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            //rowView.setAlpha(0.2f);
        }
        return rowView;
    }
}
