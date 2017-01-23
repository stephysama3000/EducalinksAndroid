package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
public class AdapterMensajesEli extends ArrayAdapter<String> {
    private final Context context;
    private final String[] Emisor;
    private final String[] Asunto;
    private final String[] Mensaje;
    private final String[] Fecha;
    private final Bitmap[] images;
    SharedPreferences settings;
    Bitmap b;

    public AdapterMensajesEli(Context context, String[] Emisor, String[] Asunto, String[] Mensaje, String[] Fecha, Bitmap[] images) {
        super(context, R.layout.lista_mensajes_ent, Mensaje);
        this.context = context;
        this.Emisor = Emisor;
        this.Asunto = Asunto;
        this.Mensaje = Mensaje;
        this.images = images;
        this.Fecha = Fecha;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView= inflater.inflate(R.layout.lista_mensajes_ent, null, true);



        TextView emisor = (TextView) rowView.findViewById(R.id.nombreEmisor);
        TextView asunto = (TextView) rowView.findViewById(R.id.mensajeAsunto);
        TextView mensaje = (TextView) rowView.findViewById(R.id.mensajeResum);
        TextView fecha = (TextView) rowView.findViewById(R.id.fechaEnvio);
        ImageView imagenE = (ImageView) rowView.findViewById(R.id.emisor_image);

        emisor.setText(Emisor[position]);
        asunto.setText(Asunto[position]);
        mensaje.setText(Mensaje[position]);
        fecha.setText(Fecha[position]);
        imagenE.setImageBitmap(images[position]);
        return rowView;
    }
}
