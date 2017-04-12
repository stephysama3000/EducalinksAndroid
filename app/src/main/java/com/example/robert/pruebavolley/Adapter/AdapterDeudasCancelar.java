package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.robert.pruebavolley.R;

/**
 * Created by redlinks on 6/2/17.
 */
public class AdapterDeudasCancelar extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] deudas;
    SharedPreferences settings;

    public AdapterDeudasCancelar(Activity context, String[] deudas) {
        super(context, R.layout.lista_deudas_a_cancelar, deudas);
        this.context = context;
        this.deudas = deudas;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView = inflater.inflate(R.layout.lista_deudas_a_cancelar, null, true);
        TextView txtMateria = (TextView) rowView.findViewById(R.id.lblListHeader);

        txtMateria.setText(deudas[position]);

        return rowView;
    }

}
