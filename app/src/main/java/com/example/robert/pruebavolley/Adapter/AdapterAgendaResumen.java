package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robert.pruebavolley.R;

public class AdapterAgendaResumen extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] materia;
    private final String[] detalleMateria;

    public AdapterAgendaResumen(Activity context, String[] materia, String[] detalleMateria){
        super(context, R.layout.listagenda, materia);
        this.context = context;
        this.materia = materia;
        this.detalleMateria = detalleMateria;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.agendares, null, true);
        TextView txtMateria = (TextView) rowView.findViewById(R.id.nombreMateria);
        TextView txtDetalleMateria = (TextView) rowView.findViewById(R.id.detalleMateria);
        txtMateria.setText(materia[position]);
        txtDetalleMateria.setText(detalleMateria[position]);

        return rowView;
    }
}