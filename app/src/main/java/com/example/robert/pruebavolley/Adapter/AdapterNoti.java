package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.robert.pruebavolley.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robert.pruebavolley.R;

public class AdapterNoti extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] tipoNoti;
    private final String[] fechaRegistro;
    private final String[] detalleNoti;

    public AdapterNoti(Activity context, String[] tipoNoti, String[] fechaRegistro, String[] detalleNoti){ //, Integer[] imageProfesor) {
        super(context, R.layout.lista_notificaciones, tipoNoti);
        this.context = context;
        this.tipoNoti = tipoNoti;
        this.fechaRegistro = fechaRegistro;
        this.detalleNoti = detalleNoti;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.lista_notificaciones, null, true);
        TextView txtTipo = (TextView) rowView.findViewById(R.id.tipoNoti);
        TextView txtFechaReg = (TextView) rowView.findViewById(R.id.fechaNoti);
        TextView txtDetalleNoti = (TextView) rowView.findViewById(R.id.detalleNoti);


        txtTipo.setText(tipoNoti[position].toUpperCase());
        txtFechaReg.setText(fechaRegistro[position]);
        txtDetalleNoti.setText(detalleNoti[position]);

        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#e6e7e8"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            //rowView.setAlpha(0.2f);
        }
        return rowView;
    }
}