package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.robert.pruebavolley.R;

/**
 * Created by redlinks on 31/3/17.
 */

public class AdapterTratamiento extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] medicamentos;
    private final String[] cantidad;

    SharedPreferences settings;
    TextView medic,cant;

    public AdapterTratamiento(Activity context, String[] medicamentos, String[] cantidad) {
        super(context, R.layout.lista_tratamiento, medicamentos);
        this.context = context;
        this.medicamentos = medicamentos;
        this.cantidad = cantidad;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView = inflater.inflate(R.layout.lista_tratamiento, null, true);
        medic = (TextView) rowView.findViewById(R.id.medicamentos);
        cant = (TextView) rowView.findViewById(R.id.cantidad);

        medic.setText(medicamentos[position]);
        cant.setText(cantidad[position]);


        return rowView;
    }


}
