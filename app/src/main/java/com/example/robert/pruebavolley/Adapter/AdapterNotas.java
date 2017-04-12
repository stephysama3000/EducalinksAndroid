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


public class AdapterNotas extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] Quimestre;
    private final String[] Parcial;
    private final String alumcodi;
    private final String cursParaCodi;
    private final String periCodi;
    private final String nomCol;
    private final String[] periDistCodi;



    SharedPreferences settings;
    Button descargar;

    public AdapterNotas(Activity context, String[] Quimestre, String[] Parcial, String alumcodi, String cursParaCodi, String periCodi, String nomCol, String[] periDistCodi) {
        super(context, R.layout.listanotas, Parcial);
        this.context = context;
        this.Quimestre = Quimestre;
        this.Parcial = Parcial;
        this.alumcodi = alumcodi;
        this.cursParaCodi = cursParaCodi;
        this.periCodi = periCodi;
        this.nomCol = nomCol;
        this.periDistCodi = periDistCodi;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView = inflater.inflate(R.layout.listanotas, null, true);
        TextView txtQuim = (TextView) rowView.findViewById(R.id.nombreQuim);
        TextView txtParcial = (TextView) rowView.findViewById(R.id.nombreParcial);

        descargar = (Button) rowView.findViewById(R.id.btndownNotas);

        txtQuim.setText(Quimestre[position].toUpperCase());
        txtParcial.setText(Parcial[position]);


        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#e6e7e8"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            //rowView.setAlpha(0.2f);
        }

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String disTipo = settings .getString("distipo", "null");
                if(disTipo.equals("G")){
                    String URL = "http://" + nomCol + ".educalinks.com.ec/admin/libretas/" + nomCol + "/" + periCodi + "/" + "lib_g_app.php?peri_dist_codi=" + periDistCodi[position] + "&alum_codi=" + alumcodi + "&curs_para_codi=" + cursParaCodi + "&peri_codi=" + periCodi;
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));
                }else if(disTipo.equals("I")){
                    String URL = "http://" + nomCol + ".educalinks.com.ec/admin/libretas/" + nomCol + "/" + periCodi + "/" + "lib_i_app.php?peri_dist_codi=" + periDistCodi[position] + "&alum_codi=" + alumcodi + "&curs_para_codi=" + cursParaCodi + "&peri_codi=" + periCodi;
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));
                }

/*                String URL = "http://" + nomCol + ".educalinks.com.ec/admin/libretas/" + nomCol + "/" + periCodi + "/" + "lib_g_app.php?peri_dist_codi=" + periDistCodi[position] + "&alum_codi=" + alumcodi + "&curs_para_codi=" + cursParaCodi + "&peri_codi=" + periCodi;
                Log.d("url",URL);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));*/
            }
        });

        return rowView;
    }


}
