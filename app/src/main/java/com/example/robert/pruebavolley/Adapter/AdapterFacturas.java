package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.robert.pruebavolley.R;

/**
 * Created by redlinks on 6/6/16.
 */
public class AdapterFacturas extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] nombresAlumno;
    private final String[] NumFactura;
    private final String[] fechaEmision;
    private final String[] TotalNeto;
    private final String nomColegios;
    private final String usuario;
    private final String[] numeroFactura;

    SharedPreferences settings;
    Button descargar;

    public AdapterFacturas(Activity context, String[] nombresAlumno, String[] NumFactura, String[] fechaEmision, String[] TotalNeto , String nomColegios, String usuario, String[] numeroFactura ) {
        super(context, R.layout.listafacturas, NumFactura);
        this.context = context;
        this.nombresAlumno = nombresAlumno;
        this.NumFactura = NumFactura;
        this.fechaEmision = fechaEmision;
        this.TotalNeto = TotalNeto;
        this.nomColegios = nomColegios;
        this.usuario = usuario;
        this.numeroFactura = numeroFactura;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView = inflater.inflate(R.layout.listafacturas, null, true);
        TextView txtnombres = (TextView) rowView.findViewById(R.id.nombresAlumno);
        TextView txtnumeroFactura = (TextView) rowView.findViewById(R.id.numeroFactura);
        TextView txtfechaEmis = (TextView) rowView.findViewById(R.id.fechaEmision);
        descargar = (Button) rowView.findViewById(R.id.btndown);
        Button btntotal = (Button) rowView.findViewById(R.id.ValorTotal);

        txtnombres.setText(nombresAlumno[position].toUpperCase());
        txtnumeroFactura.setText(NumFactura[position]);
        txtfechaEmis.setText(fechaEmision[position]);
        btntotal.setText(TotalNeto[position]);

        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#e6e7e8"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            //rowView.setAlpha(0.2f);
        }

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("url","http://" + nomColegios + ".educalinks.com.ec/documentos/autorizados/" + nomColegios + "/" + usuario + "/" + numeroFactura[position] + ".PDF");
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + nomColegios + ".educalinks.com.ec/documentos/autorizados/" + nomColegios + "/" + usuario + "/" + numeroFactura[position] + ".PDF")));
            }
        });

        return rowView;
    }


}
