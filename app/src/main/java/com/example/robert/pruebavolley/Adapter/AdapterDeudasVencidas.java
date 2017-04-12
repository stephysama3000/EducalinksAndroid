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

public class AdapterDeudasVencidas extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] deuda;
    private final String[] totalPendiente;
    SharedPreferences settings;
    Bitmap b;

    public AdapterDeudasVencidas(Activity context, String[] deuda, String[] totalPendiente) {
        super(context, R.layout.lista_deudas_vencidas, deuda);
        this.context = context;
        this.deuda = deuda;
        this.totalPendiente = totalPendiente;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView = inflater.inflate(R.layout.lista_deudas_vencidas, null, true);
        TextView txtdeuda = (TextView) rowView.findViewById(R.id.textoDeuda);
        TextView txtTotalPend = (TextView) rowView.findViewById(R.id.textoTotalPend);
        //ImageView imageProfesor1 = (ImageView) rowView.findViewById(R.id.prof_image);

        txtdeuda.setText(deuda[position]);
        txtTotalPend.setText("( $" + totalPendiente[position] + " )");
        //imageProfesor1.setImageBitmap(imageProfesor[position]);

        return rowView;
    }
}