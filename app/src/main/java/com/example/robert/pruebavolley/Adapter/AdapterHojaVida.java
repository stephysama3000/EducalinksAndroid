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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by redlinks on 6/4/17.
 */

public class AdapterHojaVida extends BaseExpandableListAdapter {

    private final Activity context;
    private final String[] tipoObserv;
    private final String[] detalleObserv;
    private final String[] profesor;
    private final String[] rol;
    private final String[] fecha;

    public AdapterHojaVida(Activity context, String[] tipoObserv, String[] detalleObserv, String[] profesor, String[] rol, String[] fecha) {
        this.context = context;
        this.tipoObserv = tipoObserv;
        this.detalleObserv = detalleObserv;
        this.profesor = profesor;
        this.rol = rol;
        this.fecha = fecha;
    }

    @Override
    public int getGroupCount() {
        return tipoObserv.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return tipoObserv[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return profesor[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        if(convertView == null){
            LayoutInflater layoutinflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutinflater.inflate(R.layout.lista_obs_parent,null);
        }
        TextView tipo = (TextView)convertView.findViewById(R.id.tipoObserv);
        TextView detalle = (TextView)convertView.findViewById(R.id.detObserv);
        ImageView imgDropdown = (ImageView)convertView.findViewById(R.id.imageDropdown);
        tipo.setText(tipoObserv[groupPosition]);
        detalle.setText(detalleObserv[groupPosition]);

        if (isExpanded) {
            imgDropdown.setImageResource(R.drawable.expdropdown);
        } else {
            imgDropdown.setImageResource(R.drawable.expdropup);
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutinflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutinflater.inflate(R.layout.lista_obs_child,null);
        }
        TextView profesorTxt = (TextView)convertView.findViewById(R.id.profesorObserv);
        TextView rolTxt = (TextView)convertView.findViewById(R.id.rolObserv);
        TextView fechaTxt = (TextView)convertView.findViewById(R.id.fechaObserv);

        profesorTxt.setText(profesor[groupPosition]);
        rolTxt.setText(rol[groupPosition]);
        fechaTxt.setText(fecha[groupPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /*private final Activity context;
    private final String[] materia;
    private final String[] detalleMateria;
    private final String[] fechas;
    //private final Bitmap[] imageProfesor;
    SharedPreferences settings;
    Bitmap b;

    public AdapterHojaVida(Activity context, String[] materia, String[] detalleMateria, String[] fechas) {
        super(context, R.layout.lista_observaciones, materia);
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
        View rowView = inflater.inflate(R.layout.lista_observaciones, null, true);
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

        return rowView;*/



}
