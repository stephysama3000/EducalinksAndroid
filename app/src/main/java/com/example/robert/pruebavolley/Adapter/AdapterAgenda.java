package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robert.pruebavolley.Clases.ImageStorage;
import com.example.robert.pruebavolley.R;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class AdapterAgenda extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] materia;
    private final String[] detalleMateria;
    private final String[] fechas;
    //private final Bitmap[] imageProfesor;
    SharedPreferences settings;
    Bitmap b;

    public AdapterAgenda(Activity context, String[] materia, String[] detalleMateria, String[] fechas) {
        super(context, R.layout.listagenda, materia);
        this.context = context;
        this.materia = materia;
        this.detalleMateria = detalleMateria;
        //this.imageProfesor = imageProfesor;
        this.fechas = fechas;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView = inflater.inflate(R.layout.listagenda, null, true);
        TextView txtMateria = (TextView) rowView.findViewById(R.id.nombreMateria);
        TextView txtDetalleMateria = (TextView) rowView.findViewById(R.id.detalleMateria);
        TextView txtfechas = (TextView) rowView.findViewById(R.id.fechas);
        //ImageView imageProfesor1 = (ImageView) rowView.findViewById(R.id.prof_image);

        txtMateria.setText(materia[position].toUpperCase());
        txtDetalleMateria.setText(detalleMateria[position]);
        txtfechas.setText(fechas[position]);
        //imageProfesor1.setImageBitmap(imageProfesor[position]);

        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#e6e7e8"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            //rowView.setAlpha(0.2f);
        }

        return rowView;
    }
}